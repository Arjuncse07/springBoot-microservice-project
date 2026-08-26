package com.arjun.library_service.bookstore.library.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(com.arjun.library_service.TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    @Test
    void shouldFindLoansByUserIdAndStatus() {
        LoanEntity activeLoan = new LoanEntity();
        activeLoan.setUserId(42L);
        activeLoan.setBarcode("BC-P100");
        activeLoan.setDueAt(LocalDateTime.now().plusDays(14));
        activeLoan.setStatus(LoanStatus.ACTIVE);
        activeLoan.setOrganizationId(1L);
        loanRepository.save(activeLoan);

        LoanEntity returnedLoan = new LoanEntity();
        returnedLoan.setUserId(42L);
        returnedLoan.setBarcode("BC-P101");
        returnedLoan.setDueAt(LocalDateTime.now().minusDays(1));
        returnedLoan.setReturnedAt(LocalDateTime.now());
        returnedLoan.setStatus(LoanStatus.RETURNED);
        returnedLoan.setOrganizationId(1L);
        loanRepository.save(returnedLoan);

        List<LoanEntity> activeLoans = loanRepository.findByUserIdAndStatus(42L, LoanStatus.ACTIVE);
        assertThat(activeLoans).hasSize(1);
        assertThat(activeLoans.getFirst().getBarcode()).isEqualTo("BC-P100");
    }

    @Test
    void shouldFindOverdueActiveLoans() {
        LoanEntity overdueLoan = new LoanEntity();
        overdueLoan.setUserId(7L);
        overdueLoan.setBarcode("BC-P102");
        overdueLoan.setDueAt(LocalDateTime.now().minusDays(2));
        overdueLoan.setStatus(LoanStatus.ACTIVE);
        overdueLoan.setOrganizationId(1L);
        loanRepository.save(overdueLoan);

        LoanEntity futureLoan = new LoanEntity();
        futureLoan.setUserId(8L);
        futureLoan.setBarcode("BC-P103");
        futureLoan.setDueAt(LocalDateTime.now().plusDays(5));
        futureLoan.setStatus(LoanStatus.ACTIVE);
        futureLoan.setOrganizationId(1L);
        loanRepository.save(futureLoan);

        List<LoanEntity> overdueLoans =
                loanRepository.findByDueAtBeforeAndStatus(LocalDateTime.now(), LoanStatus.ACTIVE);
        assertThat(overdueLoans).extracting(LoanEntity::getBarcode).contains("BC-P102").doesNotContain("BC-P103");
    }
}
