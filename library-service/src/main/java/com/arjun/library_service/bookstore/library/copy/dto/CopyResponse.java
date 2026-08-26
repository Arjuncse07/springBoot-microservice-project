package com.arjun.library_service.bookstore.library.copy.dto;

import com.arjun.library_service.bookstore.library.domain.CopyEntity;
import com.arjun.library_service.bookstore.library.domain.CopyStatus;
import java.time.LocalDateTime;

public record CopyResponse(
        String barcode,
        String productCode,
        CopyStatus status,
        String location,
        String conditionNotes,
        Long organizationId,
        LocalDateTime createdAt) {

    public static CopyResponse from(CopyEntity copy) {
        return new CopyResponse(
                copy.getBarcode(),
                copy.getProductCode(),
                copy.getStatus(),
                copy.getLocation(),
                copy.getConditionNotes(),
                copy.getOrganizationId(),
                copy.getCreatedAt());
    }
}
