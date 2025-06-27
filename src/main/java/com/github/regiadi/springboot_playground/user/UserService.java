package com.github.regiadi.springboot_playground.user;

import com.github.regiadi.springboot_playground.user.dto.UpdateUserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Retrieves all users.
	 * 
	 * @return A list of all users.
	 */
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	/**
	 * Retrieves a user by its ID.
	 * 
	 * @param id The UUID of the user.
	 * @return An Optional containing the user if found, or empty otherwise.
	 */
	public Optional<User> findUserById(UUID id) {
		return userRepository.findById(id);
	}

	/**
	 * Creates a new user, hashing their password before saving.
	 * 
	 * @param user The user to save.
	 * @return The saved user.
	 */
	public User createUser(User user) {
		// Encode the password before saving
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		return userRepository.save(user);
	}

	/**
	 * Updates an existing user from a DTO.
	 *
	 * @param id  The UUID of the user to update.
	 * @param dto The DTO containing the fields to update.
	 * @return An Optional containing the updated user if found, or empty otherwise.
	 */
	public Optional<User> updateUser(UUID id, UpdateUserRequestDTO dto) {
		return userRepository.findById(id).map(existingUser -> {
			if (dto.getUsername() != null)
				existingUser.setUsername(dto.getUsername());
			if (dto.getEmail() != null)
				existingUser.setEmail(dto.getEmail());
			if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
				existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
			}
			return userRepository.save(existingUser);
		});
	}

	public void deleteUserById(UUID id) {
		userRepository.deleteById(id);
	}
}