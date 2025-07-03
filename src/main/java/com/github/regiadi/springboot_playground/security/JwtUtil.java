package com.github.regiadi.springboot_playground.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 * Provides methods for generating, validating, and extracting information from
 * JWTs.
 */
@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration; // in milliseconds

	/**
	 * Generates a JWT for a given user.
	 * The username is used as the subject of the token.
	 *
	 * @param userDetails The user details from which to create the token.
	 * @return A JWT string.
	 */
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	/**
	 * Creates a JWT with the specified claims and subject.
	 * 
	 * @param claims  Additional claims to include in the token.
	 * @param subject The subject of the token (typically the username).
	 * @return The generated JWT string.
	 */
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.claims(claims)
				.subject(subject)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSigningKey())
				.compact();
	}

	/**
	 * Extracts the username (subject) from the JWT token.
	 *
	 * @param token The JWT from which to extract the username.
	 * @return The username from the token.
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * A generic method to extract a specific claim from the token.
	 *
	 * @param <T>            The type of the claim.
	 * @param token          The JWT from which to extract the claim.
	 * @param claimsResolver A function to apply to the claims to extract the
	 *                       desired value.
	 * @return The extracted claim.
	 */
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Parses the JWT and returns all its claims.
	 *
	 * @param token The JWT to parse.
	 * @return The claims (payload) contained in the token.
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	/**
	 * Creates a signing key from the configured Base64-encoded secret.
	 * The key is validated to ensure it meets the minimum length requirement for
	 * HMAC-SHA algorithms (256 bits).
	 *
	 * @return A {@link SecretKey} used for signing and verifying JWTs.
	 * @throws IllegalArgumentException if the decoded secret key is less than 256
	 *                                  bits (32 bytes).
	 */
	private SecretKey getSigningKey() { // Change return type from Key to SecretKey
		byte[] keyBytes = Decoders.BASE64.decode(secret);

		if (keyBytes.length < 32) { // 32 bytes * 8 bits/byte = 256 bits
			throw new IllegalArgumentException(
					"The configured JWT secret key is too short. It must be at least 256 bits (32 bytes) long after Base64 decoding.");
		}

		return Keys.hmacShaKeyFor(keyBytes); // This method already returns SecretKey
	}

}