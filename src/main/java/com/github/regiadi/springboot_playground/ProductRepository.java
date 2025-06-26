package com.github.regiadi.springboot_playground;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository // Optional, but good practice for clarity
public interface ProductRepository extends JpaRepository<Product, UUID> {
	/**
	 * Finds a list of products by their exact name.
	 * Spring Data JPA automatically implements this method based on its name.
	 *
	 * @param name The name of the product to search for.
	 * @return A list of products matching the given name.
	 */
	List<Product> findByName(String name);
}