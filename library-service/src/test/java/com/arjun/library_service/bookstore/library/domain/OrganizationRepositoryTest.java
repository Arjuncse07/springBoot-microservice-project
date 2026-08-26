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
class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    void shouldLoadSeedOrganization() {
        OrganizationEntity organization =
                organizationRepository.findBySlug("central-library").orElseThrow();
        assertThat(organization.getName()).isEqualTo("Central Library");
        assertThat(organization.getLoanPeriodDays()).isEqualTo(14);
        assertThat(organization.getMaxRenewals()).isEqualTo(2);
        assertThat(organization.getFinePerDay()).isEqualByComparingTo(new BigDecimal("5.00"));
        assertThat(organization.getGatePolicy()).isEqualTo(GatePolicy.WARNING);
    }
}
