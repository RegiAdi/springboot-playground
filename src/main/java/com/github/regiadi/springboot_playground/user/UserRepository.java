package com.github.regiadi.springboot_playground.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	// You can add custom query methods here if needed, e.g., findByUsername(String
	// username)
}