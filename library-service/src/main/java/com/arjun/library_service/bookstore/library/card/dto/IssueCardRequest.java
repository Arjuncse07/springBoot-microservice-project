package com.arjun.library_service.bookstore.library.card.dto;

import com.arjun.library_service.bookstore.library.domain.CardType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record IssueCardRequest(
        Long userId,
        @NotNull CardType cardType,
        @NotNull Long organizationId,
        BigDecimal securityDeposit,
        LocalDate validFrom,
        LocalDate validUntil) {}
