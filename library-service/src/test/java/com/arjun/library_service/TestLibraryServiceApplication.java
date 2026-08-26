package com.arjun.library_service;

import org.springframework.boot.SpringApplication;

public class TestLibraryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(LibraryServiceApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
