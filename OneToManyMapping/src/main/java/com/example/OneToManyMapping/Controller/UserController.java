package com.example.OneToManyMapping.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.OneToManyMapping.Entity.Cart;
import com.example.OneToManyMapping.Entity.User;
import com.example.OneToManyMapping.Repository.UserRepository;
import com.example.OneToManyMapping.Service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserRepository uRepo;

	@Autowired
	private UserService service;

	@GetMapping("/getAll")
	public List<User> getList() {
		return uRepo.findAll();
	}

//	@GetMapping("/getAll")
//	@Transactional
//	public List<User> getAllUsersWithCarts() {
//		List<User> users = uRepo.findAll();
//		// Trigger lazy loading of carts within the transaction
//		users.forEach(user -> user.getCart().size());
//		return users;
//	}

	@PostMapping("/add")
	public ResponseEntity<String> addList(@RequestBody User user) {
		List<Cart> carts = user.getCart();
		for (Cart cart : carts) {
			cart.setUser(user);
		}
		uRepo.save(user);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Added Successfully");
	}

	@GetMapping("/get/{id}")
	public Optional<User> getIdUser(@PathVariable Long id) {
		return uRepo.findById(id);
	}

	@PutMapping("/change/{id}")
	public ResponseEntity<String> update(@PathVariable Long id, @RequestBody User user) {
		service.updateUser(id, user);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Updated id : " + id);
	}

	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		uRepo.deleteById(id);
		return "Deleted Successfully";
	}

	@DeleteMapping("/delete/cart/{userId}")
	public String deleteCart(@PathVariable Long userId) {
		Optional<User> optionalUser = uRepo.findById(userId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			List<Cart> carts = user.getCart();
			carts.clear();
			uRepo.save(user);
			return "Deleted carts for user with ID: " + userId;
		} else {
			return "User not found with ID: " + userId;
		}
	}

}
