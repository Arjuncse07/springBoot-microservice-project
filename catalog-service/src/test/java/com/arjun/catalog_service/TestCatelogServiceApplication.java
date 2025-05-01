package com.arjun.catalog_service;

import org.springframework.boot.SpringApplication;

public class TestCatelogServiceApplication {

	public static void main(String[] args) {
		SpringApplication
				.from(CatalogServiceApplication::main)
				.with(TestcontainersConfiguration.class)
				.run(args);
	}

}
