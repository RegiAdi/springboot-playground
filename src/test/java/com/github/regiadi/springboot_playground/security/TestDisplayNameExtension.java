package com.github.regiadi.springboot_playground.security;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * A JUnit 5 extension that logs the display name of a test before it is
 * executed and its status (PASSED/FAILED) after execution.
 * This provides clear, readable, and colorful output in the console for
 * tracking test execution.
 */
public class TestDisplayNameExtension implements BeforeEachCallback, AfterEachCallback {

	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";

	@Override
	public void beforeEach(ExtensionContext context) {
		System.out.println(); // Add a blank line for readability
		System.out.println(ANSI_YELLOW + "===== Running test: " + context.getDisplayName() + " =====" + ANSI_RESET);
	}

	@Override
	public void afterEach(ExtensionContext context) {
		// Use ifPresentOrElse to handle both success and failure cases cleanly.
		context.getExecutionException().ifPresentOrElse(
				exception -> {
					System.out.println(
							ANSI_RED + "===== Test FAILED: " + context.getDisplayName() + " =====" + ANSI_RESET);
					// Print the failure reason. Using System.out with red color to ensure order.
					System.out.println(ANSI_RED + "  └─ Failure reason: " + exception.getMessage() + ANSI_RESET);
				},
				() -> System.out.println(
						ANSI_GREEN + "===== Test PASSED: " + context.getDisplayName() + " =====" + ANSI_RESET));

		System.out.println(); // Add a blank line for readability

	}
}