package com.arjun.library_service.bookstore.library.card;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.arjun.library_service.bookstore.library.card.dto.AccessCardResponse;
import com.arjun.library_service.bookstore.library.card.dto.CardStatusResponse;
import com.arjun.library_service.bookstore.library.card.dto.IssueCardRequest;
import com.arjun.library_service.bookstore.library.domain.AccessCardEntity;
import com.arjun.library_service.bookstore.library.domain.AccessCardRepository;
import com.arjun.library_service.bookstore.library.domain.CardStatus;
import com.arjun.library_service.bookstore.library.domain.CardType;
import com.arjun.library_service.bookstore.library.domain.OrganizationEntity;
import com.arjun.library_service.bookstore.library.organization.OrganizationService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private AccessCardRepository accessCardRepository;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private CardIdGenerator cardIdGenerator;

    @Mock
    private QrCodeService qrCodeService;

    @InjectMocks
    private CardService cardService;

    @Test
    void shouldIssueCardWithBrandedQr() {
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(1L);
        organization.setLogoUrl("https://example.com/logo.png");

        IssueCardRequest request =
                new IssueCardRequest(1L, CardType.PERMANENT, 1L, new BigDecimal("500.00"), null, null);

        when(organizationService.getById(1L)).thenReturn(organization);
        when(cardIdGenerator.nextPermanentCardId()).thenReturn("CARD-2026-0001");
        when(qrCodeService.buildPayload("CARD-2026-0001", 1L, 1L))
                .thenReturn("{\"cardId\":\"CARD-2026-0001\",\"userId\":1,\"orgId\":1}");
        when(accessCardRepository.save(any(AccessCardEntity.class))).thenAnswer(invocation -> {
            AccessCardEntity card = invocation.getArgument(0);
            return card;
        });
        when(qrCodeService.generateBase64Png(any(), eq("https://example.com/logo.png")))
                .thenReturn("base64png");

        AccessCardResponse response = cardService.issueCard(request);

        assertThat(response.cardId()).isEqualTo("CARD-2026-0001");
        assertThat(response.status()).isEqualTo(CardStatus.ACTIVE);
        assertThat(response.qrCodeImageBase64()).isEqualTo("base64png");
    }

    @Test
    void shouldBlockCard() {
        AccessCardEntity card = new AccessCardEntity();
        card.setCardId("CARD-2026-0001");
        card.setStatus(CardStatus.ACTIVE);
        card.setCardType(CardType.PERMANENT);
        card.setOrganizationId(1L);

        when(accessCardRepository.findByCardId("CARD-2026-0001")).thenReturn(java.util.Optional.of(card));
        when(accessCardRepository.save(card)).thenReturn(card);

        AccessCardResponse response = cardService.blockCard("CARD-2026-0001");

        assertThat(response.status()).isEqualTo(CardStatus.BLOCKED);
        verify(accessCardRepository).save(card);
    }

    @Test
    void shouldReturnCanEnterFalseWhenBlocked() {
        AccessCardEntity card = new AccessCardEntity();
        card.setCardId("CARD-2026-0001");
        card.setStatus(CardStatus.BLOCKED);
        card.setCardType(CardType.PERMANENT);

        when(accessCardRepository.findByCardId("CARD-2026-0001")).thenReturn(java.util.Optional.of(card));

        CardStatusResponse response = cardService.getStatus("CARD-2026-0001");

        assertThat(response.canEnter()).isFalse();
        assertThat(response.status()).isEqualTo(CardStatus.BLOCKED);
    }
}
