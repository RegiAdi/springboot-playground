package com.github.regiadi.springboot_playground.security.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * A record representing the authentication request payload.
 * Using a record reduces boilerplate and ensures immutability.
 */
public record AuthRequestDTO(
		@NotBlank(message = "Username cannot be blank") String username,
		@NotBlank(message = "Password cannot be blank") String password) {
}