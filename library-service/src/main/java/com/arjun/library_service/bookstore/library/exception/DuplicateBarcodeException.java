package com.arjun.library_service.bookstore.library.exception;

public class DuplicateBarcodeException extends RuntimeException {

    public DuplicateBarcodeException(String message) {
        super(message);
    }

    public static DuplicateBarcodeException forBarcode(String barcode) {
        return new DuplicateBarcodeException("Copy with barcode: " + barcode + " already exists");
    }
}
