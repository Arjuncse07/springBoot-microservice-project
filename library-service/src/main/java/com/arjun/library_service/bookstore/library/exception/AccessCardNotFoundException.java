package com.arjun.library_service.bookstore.library.exception;

public class AccessCardNotFoundException extends RuntimeException {

    private AccessCardNotFoundException(String message) {
        super(message);
    }

    public static AccessCardNotFoundException forId(String cardId) {
        return new AccessCardNotFoundException("Access card with id: " + cardId + " not found");
    }
}
