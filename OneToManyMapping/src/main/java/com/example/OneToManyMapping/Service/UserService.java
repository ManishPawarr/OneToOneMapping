package com.example.OneToManyMapping.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.OneToManyMapping.Entity.Cart;
import com.example.OneToManyMapping.Entity.User;
import com.example.OneToManyMapping.Repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	public ResponseEntity<String> updateUser(Long userId, User updatedUser) {
		Optional<User> user = userRepo.findById(userId);
		if (user.isPresent()) {
			User existingUser = user.get();
			if (updatedUser.getUsername() != null) {
				existingUser.setUsername(updatedUser.getUsername());
			}
			if (updatedUser.getAge() != null) {
				existingUser.setAge(updatedUser.getAge());
			}

			if (updatedUser.getCart() != null) {
				List<Cart> existingCarts = existingUser.getCart();
				List<Cart> updatedCarts = updatedUser.getCart();

				if (updatedCarts.get(0) == null) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No cart " + userId);
				}

				for (Cart updatedCart : updatedCarts) {
					for (Cart existingCart : existingCarts) {
						if (updatedCart.getId().equals(existingCart.getId())) {
							if (updatedCart.getProductName() != null) {
								existingCart.setProductName(updatedCart.getProductName());
							}
							if (updatedCart.getPrice() != null) {
								existingCart.setPrice(updatedCart.getPrice());
							}
							break;
						}
					}
				}
			}
			userRepo.save(existingUser);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found with id " + userId);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error with id : " + userId);
	}

}
