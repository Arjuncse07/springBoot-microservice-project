package com.arjun.library_service.bookstore.library.card;

import com.arjun.library_service.bookstore.library.card.dto.AccessCardResponse;
import com.arjun.library_service.bookstore.library.card.dto.CardStatusResponse;
import com.arjun.library_service.bookstore.library.card.dto.IssueCardRequest;
import com.arjun.library_service.bookstore.library.domain.AccessCardEntity;
import com.arjun.library_service.bookstore.library.domain.AccessCardRepository;
import com.arjun.library_service.bookstore.library.domain.CardStatus;
import com.arjun.library_service.bookstore.library.domain.OrganizationEntity;
import com.arjun.library_service.bookstore.library.exception.AccessCardNotFoundException;
import com.arjun.library_service.bookstore.library.organization.OrganizationService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {

    private final AccessCardRepository accessCardRepository;
    private final OrganizationService organizationService;
    private final CardIdGenerator cardIdGenerator;
    private final QrCodeService qrCodeService;

    public CardService(
            AccessCardRepository accessCardRepository,
            OrganizationService organizationService,
            CardIdGenerator cardIdGenerator,
            QrCodeService qrCodeService) {
        this.accessCardRepository = accessCardRepository;
        this.organizationService = organizationService;
        this.cardIdGenerator = cardIdGenerator;
        this.qrCodeService = qrCodeService;
    }

    @Transactional
    public AccessCardResponse issueCard(IssueCardRequest request) {
        OrganizationEntity organization = organizationService.getById(request.organizationId());

        String cardId = cardIdGenerator.nextPermanentCardId();
        String qrPayload = qrCodeService.buildPayload(cardId, request.userId(), request.organizationId());

        AccessCardEntity card = new AccessCardEntity();
        card.setCardId(cardId);
        card.setUserId(request.userId());
        card.setCardType(request.cardType());
        card.setStatus(CardStatus.ACTIVE);
        card.setValidFrom(request.validFrom() != null ? request.validFrom() : LocalDate.now());
        card.setValidUntil(request.validUntil());
        card.setQrCodeData(qrPayload);
        card.setSecurityDeposit(
                request.securityDeposit() != null ? request.securityDeposit() : BigDecimal.ZERO);
        card.setOrganizationId(request.organizationId());

        AccessCardEntity saved = accessCardRepository.save(card);
        String qrImage = qrCodeService.generateBase64Png(qrPayload, organization.getLogoUrl());
        return AccessCardResponse.from(saved, qrImage);
    }

    public AccessCardResponse getById(String cardId) {
        AccessCardEntity card = accessCardRepository
                .findByCardId(cardId)
                .orElseThrow(() -> AccessCardNotFoundException.forId(cardId));
        OrganizationEntity organization = organizationService.getById(card.getOrganizationId());
        String qrImage = qrCodeService.generateBase64Png(card.getQrCodeData(), organization.getLogoUrl());
        return AccessCardResponse.from(card, qrImage);
    }

    @Transactional
    public AccessCardResponse blockCard(String cardId) {
        AccessCardEntity card = accessCardRepository
                .findByCardId(cardId)
                .orElseThrow(() -> AccessCardNotFoundException.forId(cardId));
        card.setStatus(CardStatus.BLOCKED);
        AccessCardEntity saved = accessCardRepository.save(card);
        return AccessCardResponse.from(saved, null);
    }

    public CardStatusResponse getStatus(String cardId) {
        AccessCardEntity card = accessCardRepository
                .findByCardId(cardId)
                .orElseThrow(() -> AccessCardNotFoundException.forId(cardId));
        boolean canEnter = card.getStatus() == CardStatus.ACTIVE
                && (card.getValidUntil() == null || !card.getValidUntil().isBefore(LocalDate.now()));
        return new CardStatusResponse(
                card.getCardId(), card.getStatus(), card.getCardType(), card.getValidUntil(), canEnter);
    }
}
