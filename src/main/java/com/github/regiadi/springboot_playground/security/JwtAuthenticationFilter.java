package com.github.regiadi.springboot_playground.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter that intercepts incoming HTTP requests to validate JWTs.
 * This filter extends {@link OncePerRequestFilter} to ensure it is executed
 * once per request.
 * <p>
 * It performs the following steps:
 * <ol>
 * <li>Extracts the JWT from the 'Authorization' header.</li>
 * <li>Parses the token to get the username.</li>
 * <li>If a username is found and no user is currently authenticated in the
 * {@link SecurityContextHolder},
 * it loads the user details using {@link UserDetailsService}.</li>
 * <li>Validates the token's signature and expiration against the user
 * details.</li>
 * <li>If the token is valid, it creates an
 * {@link UsernamePasswordAuthenticationToken} and sets it
 * in the {@link SecurityContextHolder}, effectively authenticating the user for
 * the duration of the request.</li>
 * </ol>
 * This filter is added before the standard
 * {@link org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter}
 * in the security configuration.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	/**
	 * Constructs a new {@code JwtAuthenticationFilter}.
	 *
	 * @param jwtUtil            The utility class for JWT operations (generation,
	 *                           validation, extraction).
	 * @param userDetailsService The service to load user-specific data.
	 */
	public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Performs the actual filtering logic for JWT authentication.
	 *
	 * @param request     The incoming {@link HttpServletRequest}.
	 * @param response    The outgoing {@link HttpServletResponse}.
	 * @param filterChain The {@link FilterChain} to pass the request along to the
	 *                    next filter.
	 * @throws ServletException If a servlet-specific error occurs.
	 * @throws IOException      If an I/O error occurs.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");

		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			try {
				username = jwtUtil.extractUsername(jwt);
			} catch (Exception e) {
				// Log exception, e.g., token expired, invalid signature
				logger.warn("JWT Token error: " + e.getMessage());
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			if (jwtUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}