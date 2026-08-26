package com.arjun.library_service.bookstore.library.exception;

public class RenewalNotAllowedException extends RuntimeException {

    public RenewalNotAllowedException(String message) {
        super(message);
    }

    public static RenewalNotAllowedException forLoan(Long loanId, String reason) {
        return new RenewalNotAllowedException("Loan " + loanId + " cannot be renewed: " + reason);
    }
}
