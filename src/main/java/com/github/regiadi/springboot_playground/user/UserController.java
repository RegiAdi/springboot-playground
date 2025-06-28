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

/**
 * REST controller for managing user-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting users.
 */
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
	 * Creates a new user based on the provided data. This endpoint is typically
	 * public.
	 * <p>
	 * HTTP Method: {@code POST} <br>
	 * Path: {@code /api/users}
	 * 
	 * @param userDTO The request body containing the new user's details. Must be
	 *                valid.
	 * @return A {@link ResponseEntity} with the created user's data and an HTTP 201
	 *         Created status.
	 */
	@PostMapping
	public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO userDTO) {
		User userToCreate = userMapper.toUser(userDTO);
		User savedUser = userService.createUser(userToCreate);
		return new ResponseEntity<>(userMapper.toUserResponseDTO(savedUser), HttpStatus.CREATED);
	}

	/**
	 * Retrieves a list of all users. This is a protected endpoint.
	 * <p>
	 * HTTP Method: {@code GET} <br>
	 * Path: {@code /api/users}
	 * 
	 * @return A list of {@link UserResponseDTO} objects representing all users.
	 */
	@GetMapping
	public List<UserResponseDTO> getAllUsers() {
		return userService.findAllUsers().stream().map(userMapper::toUserResponseDTO)
				.collect(Collectors.toList());
	}

	/**
	 * Retrieves a single user by their unique ID. This is a protected endpoint.
	 * <p>
	 * HTTP Method: {@code GET} <br>
	 * Path: {@code /api/users/{id}}
	 * 
	 * @param id The UUID of the user to retrieve.
	 * @return A {@link ResponseEntity} containing the user's data if found (HTTP
	 *         200 OK), or a 404 Not Found response.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
		return userService.findUserById(id).map(user -> ResponseEntity.ok(userMapper.toUserResponseDTO(user)))
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Updates an existing user's details. This is a protected endpoint.
	 * <p>
	 * HTTP Method: {@code PUT} <br>
	 * Path: {@code /api/users/{id}}
	 * 
	 * @param id             The UUID of the user to update.
	 * @param userDetailsDTO The request body containing the fields to update. Must
	 *                       be valid.
	 * @return A {@link ResponseEntity} with the updated user's data if found (HTTP
	 *         200 OK), or a 404 Not Found response.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id,
			@Valid @RequestBody UpdateUserRequestDTO userDetailsDTO) {
		return userService.updateUser(id, userDetailsDTO)
				.map(updatedUser -> ResponseEntity.ok(userMapper.toUserResponseDTO(updatedUser)))
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Deletes a user by their unique ID. This is a protected endpoint.
	 * <p>
	 * HTTP Method: {@code DELETE} <br>
	 * Path: {@code /api/users/{id}}
	 * 
	 * @param id The UUID of the user to delete.
	 * @return A {@link ResponseEntity} with an HTTP 204 No Content status on
	 *         success, or a 404 Not Found response if the user does not exist.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
		if (userService.findUserById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		userService.deleteUserById(id);
		return ResponseEntity.noContent().build();
	}
}