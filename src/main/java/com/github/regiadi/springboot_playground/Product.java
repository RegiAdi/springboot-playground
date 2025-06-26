package com.github.regiadi.springboot_playground;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity // Marks this class as a JPA entity
public class Product {

	@Id // Designates the primary key
	@GeneratedValue // Let the persistence provider generate the value
	@UuidGenerator(style = UuidGenerator.Style.TIME) // Use a time-based UUID (ULID-like)
	private UUID id;
	private String name;
	private double price;

	// Default constructor is required by JPA
	public Product() {
	}

	public Product(String name, double price) {
		this.name = name;
		this.price = price;
	}

	// Getters and Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product{" +
				"id=" + (id != null ? id.toString() : "null") +
				", name='" + name + '\'' +
				", price=" + price +
				'}';
	}
}