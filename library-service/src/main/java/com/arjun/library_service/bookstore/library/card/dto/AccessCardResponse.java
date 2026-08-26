package com.arjun.library_service.bookstore.library.card.dto;

import com.arjun.library_service.bookstore.library.domain.AccessCardEntity;
import com.arjun.library_service.bookstore.library.domain.CardStatus;
import com.arjun.library_service.bookstore.library.domain.CardType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AccessCardResponse(
        String cardId,
        Long userId,
        CardType cardType,
        CardStatus status,
        LocalDateTime issuedAt,
        LocalDate validFrom,
        LocalDate validUntil,
        String qrCodeData,
        String qrCodeImageBase64,
        BigDecimal securityDeposit,
        Long organizationId) {

    public static AccessCardResponse from(AccessCardEntity card, String qrCodeImageBase64) {
        return new AccessCardResponse(
                card.getCardId(),
                card.getUserId(),
                card.getCardType(),
                card.getStatus(),
                card.getIssuedAt(),
                card.getValidFrom(),
                card.getValidUntil(),
                card.getQrCodeData(),
                qrCodeImageBase64,
                card.getSecurityDeposit(),
                card.getOrganizationId());
    }
}
