package com.arjun.library_service.bookstore.library.exception;

public class CopyNotFoundException extends RuntimeException {

    public CopyNotFoundException(String message) {
        super(message);
    }

    public static CopyNotFoundException forBarcode(String barcode) {
        return new CopyNotFoundException("Copy with barcode: " + barcode + " not found");
    }
}
