package com.github.regiadi.springboot_playground.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

// Use @SpringBootTest to load the application context and inject beans like JwtUtil.
// We provide test properties to override the ones in application.properties.
@SpringBootTest(properties = {
		"jwt.secret=bXlTdXBlclNlY3JldEtleVRoYXRJc0xvbmdFbm91Z2hGb3JIUzI1NkFsZ29yaXRobQ==", // A valid Base64 encoded
																							// 256-bit secret
		"jwt.expiration=3600000" // 1 hour
})
@ExtendWith(TestDisplayNameExtension.class)
class JwtUtilTest {

	@Autowired
	private JwtUtil jwtUtil;

	private UserDetails userDetails;

	@BeforeEach
	void setUp() {
		userDetails = new User("testuser", "password", new ArrayList<>());
	}

	@Test
	@DisplayName("Should Generate a Valid JWT Token")
	void generateToken_shouldCreateValidToken() {
		// When
		String token = jwtUtil.generateToken(userDetails);

		// Then
		assertNotNull(token);
		assertFalse(token.isEmpty());
	}

	@Test
	@DisplayName("Should Extract Correct Username from Token")
	void extractUsername_shouldReturnCorrectUsername() {
		// Given
		String token = jwtUtil.generateToken(userDetails);

		// When
		String extractedUsername = jwtUtil.extractUsername(token);

		// Then
		assertEquals("testuser", extractedUsername);
	}

	@Test
	@DisplayName("Should Throw ExpiredJwtException for Expired Token")
	void extractUsername_shouldThrowExceptionForExpiredToken() throws InterruptedException {
		// Given
		// Override the expiration to a very short time (1 millisecond)
		long originalExpiration = (long) ReflectionTestUtils.getField(jwtUtil, "expiration");
		ReflectionTestUtils.setField(jwtUtil, "expiration", 1L);
		String token = jwtUtil.generateToken(userDetails);

		// When
		// Wait for the token to expire
		Thread.sleep(50);

		// Then
		assertThrows(ExpiredJwtException.class, () -> jwtUtil.extractUsername(token));

		// Reset expiration for other tests
		ReflectionTestUtils.setField(jwtUtil, "expiration", originalExpiration);
	}

	@Test
	@DisplayName("Should Throw IllegalArgumentException for Short Secret Key")
	void getSigningKey_shouldThrowExceptionForShortSecret() {
		// Given
		// A secret that is too short (less than 32 bytes after decoding)
		String originalSecret = (String) ReflectionTestUtils.getField(jwtUtil, "secret");
		String shortSecret = Base64.getEncoder().encodeToString("shortsecret".getBytes());
		ReflectionTestUtils.setField(jwtUtil, "secret", shortSecret);

		// When & Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> jwtUtil.generateToken(userDetails));

		assertTrue(exception.getMessage().contains("The configured JWT secret key is too short."));

		// Reset secret for other tests
		ReflectionTestUtils.setField(jwtUtil, "secret", originalSecret);
	}
}