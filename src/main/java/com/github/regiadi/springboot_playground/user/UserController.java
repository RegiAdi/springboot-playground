package com.github.regiadi.springboot_playground.user;

import com.github.regiadi.springboot_playground.user.dto.CreateUserRequestDTO;
import com.github.regiadi.springboot_playground.user.dto.UpdateUserRequestDTO;
import com.github.regiadi.springboot_playground.user.dto.UserMapper;
import com.github.regiadi.springboot_playground.user.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	private final UserMapper userMapper;

	@Autowired
	public UserController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}

	/**
	 * Creates a new user.
	 * POST /api/users
	 */
	@PostMapping
	public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO userDTO) {
		User userToCreate = userMapper.toUser(userDTO);
		User savedUser = userService.createUser(userToCreate);
		return new ResponseEntity<>(userMapper.toUserResponseDTO(savedUser), HttpStatus.CREATED);
	}

	/**
	 * Retrieves all users.
	 * GET /api/users
	 */
	@GetMapping
	public List<UserResponseDTO> getAllUsers() {
		return userService.findAllUsers().stream().map(userMapper::toUserResponseDTO)
				.collect(Collectors.toList());
	}

	/**
	 * Retrieves a single user by its ID.
	 * GET /api/users/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
		return userService.findUserById(id).map(user -> ResponseEntity.ok(userMapper.toUserResponseDTO(user)))
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Updates an existing user.
	 * PUT /api/users/{id}
	 */
	@PutMapping("/{id}")
	public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id,
			@Valid @RequestBody UpdateUserRequestDTO userDetailsDTO) {
		return userService.updateUser(id, userDetailsDTO)
				.map(updatedUser -> ResponseEntity.ok(userMapper.toUserResponseDTO(updatedUser)))
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
		if (userService.findUserById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		userService.deleteUserById(id);
		return ResponseEntity.noContent().build();
	}
}