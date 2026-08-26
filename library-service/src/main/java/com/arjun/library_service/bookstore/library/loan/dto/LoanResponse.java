package com.arjun.library_service.bookstore.library.loan.dto;

import com.arjun.library_service.bookstore.library.domain.LoanEntity;
import com.arjun.library_service.bookstore.library.domain.LoanStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoanResponse(
        Long id,
        Long userId,
        String barcode,
        LocalDateTime borrowedAt,
        LocalDateTime dueAt,
        LocalDateTime returnedAt,
        LoanStatus status,
        int renewalCount,
        BigDecimal fineAmount,
        boolean finePaid,
        Long organizationId) {

    public static LoanResponse from(LoanEntity loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getUserId(),
                loan.getBarcode(),
                loan.getBorrowedAt(),
                loan.getDueAt(),
                loan.getReturnedAt(),
                loan.getStatus(),
                loan.getRenewalCount(),
                loan.getFineAmount(),
                loan.isFinePaid(),
                loan.getOrganizationId());
    }
}
