package com.github.regiadi.springboot_playground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(
			UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/monitor/**").permitAll() // Allow public access to monitoring endpoints
						.requestMatchers(HttpMethod.POST, "/api/users").permitAll() // Allow creating users without
																					// authentication
						.requestMatchers("/api/users/**").authenticated() // Protect other user endpoints
						.requestMatchers("/api/products/**").authenticated() // Protect product endpoints
						.anyRequest().authenticated() // All other requests require authentication
				)
				.httpBasic(org.springframework.security.config.Customizer.withDefaults()); // Enable HTTP Basic
																							// authentication
		return http.build();
	}
}