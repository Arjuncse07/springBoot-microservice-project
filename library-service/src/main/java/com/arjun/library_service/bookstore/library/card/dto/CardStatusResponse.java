package com.arjun.library_service.bookstore.library.card.dto;

import com.arjun.library_service.bookstore.library.domain.CardStatus;
import com.arjun.library_service.bookstore.library.domain.CardType;
import java.time.LocalDate;

public record CardStatusResponse(
        String cardId, CardStatus status, CardType cardType, LocalDate validUntil, boolean canEnter) {}
