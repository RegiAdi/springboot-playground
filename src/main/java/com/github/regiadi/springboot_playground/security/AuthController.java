package com.github.regiadi.springboot_playground.security;

import com.github.regiadi.springboot_playground.security.dto.AuthRequestDTO;
import com.github.regiadi.springboot_playground.security.dto.AuthResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling user authentication.
 * Provides an endpoint for users to log in and receive a JWT token.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtUtil jwtUtil;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
			JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	/**
	 * Authenticates a user and returns a JWT token upon successful authentication.
	 * If authentication fails (e.g., bad credentials), a 401 Unauthorized
	 * response is sent,
	 * as configured in
	 * {@link com.github.regiadi.springboot_playground.config.SecurityConfig}.
	 * <p>
	 * HTTP Method: {@code POST} <br>
	 * Path: {@code /api/auth/login}
	 *
	 * @param authRequest The request body containing the user's credentials
	 *                    (username and password).
	 * @return A {@link ResponseEntity} containing the JWT in an
	 *         {@link AuthResponseDTO} and an HTTP 200 OK status.
	 */
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> createAuthenticationToken(@RequestBody AuthRequestDTO authRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthResponseDTO(jwt));
	}
}