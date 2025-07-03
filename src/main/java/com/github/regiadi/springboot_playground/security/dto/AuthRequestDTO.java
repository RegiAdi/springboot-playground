package com.github.regiadi.springboot_playground.security.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthRequestDTO {
	@NotBlank(message = "Username cannot be blank")
	private String username;

	@NotBlank(message = "Password cannot be blank")
	private String password;

	// No-argument constructor for JSON deserialization
	public AuthRequestDTO() {
	}

	// All-arguments constructor for easier instantiation in code (e.g., tests)
	public AuthRequestDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}

	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}