package com.arjun.library_service.bookstore.library.exception;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(String message) {
        super(message);
    }

    public static LoanNotFoundException forId(Long id) {
        return new LoanNotFoundException("Loan with id: " + id + " not found");
    }
}
