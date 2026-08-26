package com.arjun.library_service.bookstore.library.copy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCopyRequest(
        @NotBlank String barcode,
        @NotBlank String productCode,
        @NotNull Long organizationId,
        String location,
        String conditionNotes) {}
