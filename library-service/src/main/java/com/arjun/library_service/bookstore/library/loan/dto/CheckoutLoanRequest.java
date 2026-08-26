package com.arjun.library_service.bookstore.library.loan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutLoanRequest(@NotNull Long userId, @NotBlank String barcode) {}
