package com.github.regiadi.springboot_playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringbootPlaygroundApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringbootPlaygroundApplication.class, args);
	}
}
