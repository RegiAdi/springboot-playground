package com.github.regiadi.springboot_playground;

import java.time.LocalDateTime;

public class HealthStatus {
	private final String status;
	private final LocalDateTime timestamp;
	private final String applicationVersion;

	public HealthStatus(
			String status,
			LocalDateTime timestamp,
			String applicationVersion) {
		this.status = status;
		this.timestamp = timestamp;
		this.applicationVersion = applicationVersion;
	}

	public String getStatus() {
		return status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}
}