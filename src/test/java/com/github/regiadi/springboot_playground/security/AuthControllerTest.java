package com.github.regiadi.springboot_playground.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.regiadi.springboot_playground.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import com.github.regiadi.springboot_playground.security.dto.AuthRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
// We import SecurityConfig to apply our security rules and exception handling
// to the test environment.
@Import({ SecurityConfig.class, CustomAuthenticationEntryPoint.class })
@ExtendWith(TestDisplayNameExtension.class)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private UserDetailsService userDetailsService;

	@MockBean
	private JwtUtil jwtUtil;

	@Test
	@DisplayName("Should Return JWT When Credentials Are Valid")
	void createAuthenticationToken_shouldReturnJwt_whenCredentialsAreValid() throws Exception {
		// Given
		AuthRequestDTO authRequest = new AuthRequestDTO("testuser", "password");
		UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
		String dummyJwt = "dummy.jwt.token";

		// Mock the authentication manager to simulate a successful authentication.
		// It should return a fully authenticated Authentication object.
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "password",
				userDetails.getAuthorities());
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);

		when(jwtUtil.generateToken(userDetails)).thenReturn(dummyJwt);

		// When & Then
		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jwt", is(dummyJwt)));
	}

	@Test
	@DisplayName("Should Return 401 Unauthorized When Credentials Are Invalid")
	void createAuthenticationToken_shouldReturnUnauthorized_whenCredentialsAreInvalid() throws Exception {
		AuthRequestDTO authRequest = new AuthRequestDTO("testuser", "wrongpassword");
		doThrow(new BadCredentialsException("Invalid credentials")).when(authenticationManager)
				.authenticate(any(UsernamePasswordAuthenticationToken.class));

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authRequest)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error", is("Unauthorized")))
				.andExpect(jsonPath("$.message", is("Invalid credentials")));
	}

	@Test
	@DisplayName("Should Return 400 Bad Request When Credentials Are Blank")
	void createAuthenticationToken_shouldReturnBadRequest_whenCredentialsAreBlank() throws Exception {
		// Given
		AuthRequestDTO authRequest = new AuthRequestDTO("", "");

		// When & Then
		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error", is("Validation Failed")))
				.andExpect(jsonPath("$.details.username", is("Username cannot be blank")))
				.andExpect(jsonPath("$.details.password", is("Password cannot be blank")));
	}
}