package com.github.regiadi.springboot_playground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.github.regiadi.springboot_playground.security.JwtAuthenticationFilter;
import com.github.regiadi.springboot_playground.security.CustomAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public SecurityFilterChain filterChain(
			HttpSecurity http,
			JwtAuthenticationFilter jwtAuthenticationFilter,
			CustomAuthenticationEntryPoint customAuthenticationEntryPoint)
			throws Exception {
		http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
				// Set session management to stateless
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.exceptionHandling(exception -> exception
						.authenticationEntryPoint(customAuthenticationEntryPoint))

				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/monitor/**").permitAll() // Allow public access to monitoring endpoints
						.requestMatchers("/api/auth/login").permitAll() // Allow login endpoint without authentication
						.requestMatchers(HttpMethod.POST, "/api/users").permitAll() // Allow creating users without
																					// authentication
						.requestMatchers("/api/users/**").authenticated() // Protect other user endpoints
						.requestMatchers("/api/products/**").authenticated() // Protect product endpoints
						.anyRequest().authenticated() // All other requests require authentication
				)
				// Add the JWT filter before Spring Security's default
				// UsernamePasswordAuthenticationFilter
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		// Remove httpBasic as we are using JWT
		// .httpBasic(org.springframework.security.config.Customizer.withDefaults());
		return http.build();
	}
}