package com.arjun.library_service.bookstore.library.loan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.arjun.library_service.bookstore.library.domain.CopyEntity;
import com.arjun.library_service.bookstore.library.domain.CopyRepository;
import com.arjun.library_service.bookstore.library.domain.CopyStatus;
import com.arjun.library_service.bookstore.library.domain.LoanEntity;
import com.arjun.library_service.bookstore.library.domain.LoanRepository;
import com.arjun.library_service.bookstore.library.domain.LoanStatus;
import com.arjun.library_service.bookstore.library.domain.OrganizationEntity;
import com.arjun.library_service.bookstore.library.exception.CopyNotAvailableException;
import com.arjun.library_service.bookstore.library.exception.InvalidLoanStateException;
import com.arjun.library_service.bookstore.library.exception.RenewalNotAllowedException;
import com.arjun.library_service.bookstore.library.loan.dto.LoanResponse;
import com.arjun.library_service.bookstore.library.organization.OrganizationService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CopyRepository copyRepository;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private LoanService loanService;

    @Test
    void shouldCheckoutAvailableCopy() {
        CopyEntity copy = availableCopy("BC-P100");
        OrganizationEntity organization = organizationWithLoanPeriod(14);

        when(copyRepository.findByBarcode("BC-P100")).thenReturn(Optional.of(copy));
        when(organizationService.getById(1L)).thenReturn(organization);
        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(invocation -> {
            LoanEntity loan = invocation.getArgument(0);
            loan.setId(1L);
            return loan;
        });
        when(copyRepository.save(copy)).thenReturn(copy);

        LoanResponse response = loanService.checkout(42L, "BC-P100");

        assertThat(response.userId()).isEqualTo(42L);
        assertThat(response.barcode()).isEqualTo("BC-P100");
        assertThat(response.status()).isEqualTo(LoanStatus.ACTIVE);
        assertThat(copy.getStatus()).isEqualTo(CopyStatus.LOANED);
    }

    @Test
    void shouldRejectCheckoutWhenCopyNotAvailable() {
        CopyEntity copy = availableCopy("BC-P100");
        copy.setStatus(CopyStatus.LOANED);
        when(copyRepository.findByBarcode("BC-P100")).thenReturn(Optional.of(copy));

        assertThatThrownBy(() -> loanService.checkout(42L, "BC-P100"))
                .isInstanceOf(CopyNotAvailableException.class);
    }

    @Test
    void shouldReturnActiveLoan() {
        LoanEntity loan = activeLoan(1L, "BC-P100");
        CopyEntity copy = availableCopy("BC-P100");
        copy.setStatus(CopyStatus.LOANED);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(copyRepository.findByBarcode("BC-P100")).thenReturn(Optional.of(copy));
        when(loanRepository.save(loan)).thenReturn(loan);
        when(copyRepository.save(copy)).thenReturn(copy);

        LoanResponse response = loanService.returnLoan(1L);

        assertThat(response.status()).isEqualTo(LoanStatus.RETURNED);
        assertThat(response.returnedAt()).isNotNull();
        assertThat(copy.getStatus()).isEqualTo(CopyStatus.AVAILABLE);
        verify(loanRepository).save(loan);
    }

    @Test
    void shouldRejectReturnForNonActiveLoan() {
        LoanEntity loan = activeLoan(1L, "BC-P100");
        loan.setStatus(LoanStatus.RETURNED);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        assertThatThrownBy(() -> loanService.returnLoan(1L)).isInstanceOf(InvalidLoanStateException.class);
    }

    @Test
    void shouldRenewActiveLoan() {
        LoanEntity loan = activeLoan(1L, "BC-P100");
        loan.setDueAt(LocalDateTime.now().plusDays(5));
        loan.setRenewalCount(0);
        OrganizationEntity organization = organizationWithLoanPeriod(14);
        organization.setMaxRenewals(2);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(organizationService.getById(1L)).thenReturn(organization);
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanResponse response = loanService.renewLoan(1L);

        assertThat(response.renewalCount()).isEqualTo(1);
        assertThat(loan.getDueAt()).isAfter(LocalDateTime.now().plusDays(18));
    }

    @Test
    void shouldRejectRenewalWhenMaxReached() {
        LoanEntity loan = activeLoan(1L, "BC-P100");
        loan.setDueAt(LocalDateTime.now().plusDays(5));
        loan.setRenewalCount(2);
        OrganizationEntity organization = organizationWithLoanPeriod(14);
        organization.setMaxRenewals(2);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(organizationService.getById(1L)).thenReturn(organization);

        assertThatThrownBy(() -> loanService.renewLoan(1L)).isInstanceOf(RenewalNotAllowedException.class);
    }

    @Test
    void shouldRejectRenewalWhenOverdue() {
        LoanEntity loan = activeLoan(1L, "BC-P100");
        loan.setDueAt(LocalDateTime.now().minusDays(1));

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        assertThatThrownBy(() -> loanService.renewLoan(1L)).isInstanceOf(RenewalNotAllowedException.class);
    }

    private CopyEntity availableCopy(String barcode) {
        CopyEntity copy = new CopyEntity();
        copy.setBarcode(barcode);
        copy.setProductCode("P100");
        copy.setStatus(CopyStatus.AVAILABLE);
        copy.setOrganizationId(1L);
        return copy;
    }

    private OrganizationEntity organizationWithLoanPeriod(int days) {
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(1L);
        organization.setLoanPeriodDays(days);
        return organization;
    }

    private LoanEntity activeLoan(Long id, String barcode) {
        LoanEntity loan = new LoanEntity();
        loan.setId(id);
        loan.setUserId(42L);
        loan.setBarcode(barcode);
        loan.setBorrowedAt(LocalDateTime.now().minusDays(1));
        loan.setDueAt(LocalDateTime.now().plusDays(13));
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setOrganizationId(1L);
        return loan;
    }
}
