package com.github.regiadi.springboot_playground;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

	@GetMapping("/ping")
	public String ping() {
		return String.format("pong!");
	}

	@GetMapping("/health")
	public HealthStatus healthCheck() {
		return new HealthStatus(
				"healthy!",
				LocalDateTime.now(),
				"1.0.0-SNAPSHOT");
	}
}
