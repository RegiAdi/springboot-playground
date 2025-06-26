package com.github.regiadi.springboot_playground.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service // Marks this class as a Spring service component
public class ProductService {

	private final ProductRepository productRepository;

	@Autowired // Injects the ProductRepository
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Retrieves all products.
	 * 
	 * @return A list of all products.
	 */
	public List<Product> findAllProducts() {
		return productRepository.findAll();
	}

	/**
	 * Retrieves a product by its ID.
	 * 
	 * @param id The UUID of the product.
	 * @return An Optional containing the product if found, or empty otherwise.
	 */
	public Optional<Product> findProductById(UUID id) {
		return productRepository.findById(id);
	}

	/**
	 * Saves a new product or updates an existing one.
	 * 
	 * @param product The product to save.
	 * @return The saved product.
	 */
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	/**
	 * Deletes a product by its ID.
	 * 
	 * @param id The UUID of the product to delete.
	 */
	public void deleteProductById(UUID id) {
		productRepository.deleteById(id);
	}

	/**
	 * Finds products by their name.
	 * 
	 * @param name The name of the product to search for.
	 * @return A list of products matching the given name.
	 */
	public List<Product> findProductsByName(String name) {
		return productRepository.findByName(name);
	}
}