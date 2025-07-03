package com.github.regiadi.springboot_playground.security.dto;

/**
 * A record representing the authentication response containing the JWT.
 */
public record AuthResponseDTO(String jwt) {
}