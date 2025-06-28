package com.github.regiadi.springboot_playground.security.dto;

public class AuthResponseDTO {
	private final String jwt;

	public AuthResponseDTO(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}
}