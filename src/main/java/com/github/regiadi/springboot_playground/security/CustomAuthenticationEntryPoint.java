package com.github.regiadi.springboot_playground.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.regiadi.springboot_playground.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Custom implementation of {@link AuthenticationEntryPoint}.
 * <p>
 * This class is triggered whenever an unauthenticated user tries to access a
 * protected resource.
 * It overrides the default Spring Security behavior to return a custom,
 * structured JSON error response
 * with a 401 Unauthorized status.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException)
			throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(),
				"Unauthorized", authException.getMessage(), request.getRequestURI());

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}