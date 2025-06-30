package com.github.regiadi.springboot_playground.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

	private final LocalDateTime timestamp;
	private final int status;
	private final String error;
	private final String message;
	private final String path;
	private Map<String, String> details;

	public ErrorResponse(
			LocalDateTime timestamp,
			int status,
			String error,
			String message,
			String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public int getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}

	public Map<String, String> getDetails() {
		return details;
	}

}