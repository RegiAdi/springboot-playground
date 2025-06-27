package com.github.regiadi.springboot_playground.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	// Spring Data JPA will automatically implement this method based on
	// the method name
	Optional<User> findByUsername(String username);
}