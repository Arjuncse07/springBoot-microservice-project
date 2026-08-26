package com.arjun.library_service.bookstore.library.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(com.arjun.library_service.TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccessCardRepositoryTest {

    @Autowired
    private AccessCardRepository accessCardRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    void shouldSaveAndFindAccessCard() {
        OrganizationEntity organization = organizationRepository
                .findBySlug("central-library")
                .orElseThrow();

        AccessCardEntity card = new AccessCardEntity();
        card.setCardId("CARD-TEST-0001");
        card.setUserId(99L);
        card.setCardType(CardType.PERMANENT);
        card.setStatus(CardStatus.ACTIVE);
        card.setQrCodeData("{\"cardId\":\"CARD-TEST-0001\"}");
        card.setSecurityDeposit(new BigDecimal("100.00"));
        card.setOrganizationId(organization.getId());

        accessCardRepository.save(card);

        AccessCardEntity found = accessCardRepository.findByCardId("CARD-TEST-0001").orElseThrow();
        assertThat(found.getUserId()).isEqualTo(99L);
        assertThat(found.getStatus()).isEqualTo(CardStatus.ACTIVE);
    }

    @Test
    void shouldGenerateNextSequence() {
        Long seq = accessCardRepository.nextCardSequence();
        assertThat(seq).isPositive();
    }
}
