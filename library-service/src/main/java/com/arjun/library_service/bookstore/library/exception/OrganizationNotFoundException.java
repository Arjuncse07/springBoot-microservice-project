package com.arjun.library_service.bookstore.library.exception;

public class OrganizationNotFoundException extends RuntimeException {

    public OrganizationNotFoundException(String message) {
        super(message);
    }

    public static OrganizationNotFoundException forId(Long id) {
        return new OrganizationNotFoundException("Organization with id: " + id + " not found");
    }
}
