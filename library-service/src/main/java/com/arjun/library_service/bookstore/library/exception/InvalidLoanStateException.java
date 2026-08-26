package com.arjun.library_service.bookstore.library.exception;

public class InvalidLoanStateException extends RuntimeException {

    public InvalidLoanStateException(String message) {
        super(message);
    }

    public static InvalidLoanStateException forId(Long id) {
        return new InvalidLoanStateException("Loan with id: " + id + " is not in a valid state for this operation");
    }
}
