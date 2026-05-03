package com.example.kitchenservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

// Tell Spring Boot NOT to look for a database
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class KitchenServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(KitchenServiceApplication.class, args);
	}
}