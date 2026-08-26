package com.arjun.library_service.bookstore.library.exception;

public class CopyNotAvailableException extends RuntimeException {

    public CopyNotAvailableException(String message) {
        super(message);
    }

    public static CopyNotAvailableException forBarcode(String barcode) {
        return new CopyNotAvailableException("Copy with barcode: " + barcode + " is not available for checkout");
    }
}
