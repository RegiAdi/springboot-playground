package com.github.regiadi.springboot_playground.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class to enable JPA Auditing.
 * This allows automatic population of fields like createdAt and updatedAt.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}