package com.arjun.catelog_service;

import org.springframework.boot.SpringApplication;

public class TestCatelogServiceApplication {

	public static void main(String[] args) {
		SpringApplication
				.from(CatelogServiceApplication::main)
				.with(TestcontainersConfiguration.class)
				.run(args);
	}

}
