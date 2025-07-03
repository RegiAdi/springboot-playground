package com.github.regiadi.springboot_playground.security;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * A JUnit 5 extension that logs the display name of a test before it is
 * executed and its status (PASSED/FAILED) after execution.
 * This provides clear, readable output in the console for tracking test
 * execution.
 */
public class TestDisplayNameExtension implements BeforeEachCallback, AfterEachCallback {
	@Override
	public void beforeEach(ExtensionContext context) {
		System.out.println(); // Add a blank line for readability
		System.out.println("===== Running test: " + context.getDisplayName() + " =====");
	}

	@Override
	public void afterEach(ExtensionContext context) {
		// Use ifPresentOrElse to handle both success and failure cases cleanly.
		context.getExecutionException().ifPresentOrElse(
				exception -> {
					System.out.println("===== Test FAILED: " + context.getDisplayName() + " =====");
					// Print the failure reason to standard error for better visibility.
					System.err.println("  └─ Failure reason: " + exception.getMessage());
				},
				() -> System.out.println("===== Test PASSED: " + context.getDisplayName() + " ====="));

		System.out.println(); // Add a blank line for readability

	}
}