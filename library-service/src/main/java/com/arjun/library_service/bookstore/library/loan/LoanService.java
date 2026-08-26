package com.arjun.library_service.bookstore.library.loan;

import com.arjun.library_service.bookstore.library.domain.CopyEntity;
import com.arjun.library_service.bookstore.library.domain.CopyRepository;
import com.arjun.library_service.bookstore.library.domain.CopyStatus;
import com.arjun.library_service.bookstore.library.domain.LoanEntity;
import com.arjun.library_service.bookstore.library.domain.LoanRepository;
import com.arjun.library_service.bookstore.library.domain.LoanStatus;
import com.arjun.library_service.bookstore.library.domain.OrganizationEntity;
import com.arjun.library_service.bookstore.library.exception.CopyNotAvailableException;
import com.arjun.library_service.bookstore.library.exception.CopyNotFoundException;
import com.arjun.library_service.bookstore.library.exception.InvalidLoanStateException;
import com.arjun.library_service.bookstore.library.exception.LoanNotFoundException;
import com.arjun.library_service.bookstore.library.exception.RenewalNotAllowedException;
import com.arjun.library_service.bookstore.library.loan.dto.LoanResponse;
import com.arjun.library_service.bookstore.library.organization.OrganizationService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final CopyRepository copyRepository;
    private final OrganizationService organizationService;

    public LoanService(
            LoanRepository loanRepository, CopyRepository copyRepository, OrganizationService organizationService) {
        this.loanRepository = loanRepository;
        this.copyRepository = copyRepository;
        this.organizationService = organizationService;
    }

    @Transactional
    public LoanResponse checkout(Long userId, String barcode) {
        CopyEntity copy = copyRepository
                .findByBarcode(barcode)
                .orElseThrow(() -> CopyNotFoundException.forBarcode(barcode));
        if (copy.getStatus() != CopyStatus.AVAILABLE) {
            throw CopyNotAvailableException.forBarcode(barcode);
        }

        OrganizationEntity organization = organizationService.getById(copy.getOrganizationId());

        LoanEntity loan = new LoanEntity();
        loan.setUserId(userId);
        loan.setBarcode(barcode);
        loan.setDueAt(LocalDateTime.now().plusDays(organization.getLoanPeriodDays()));
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setOrganizationId(copy.getOrganizationId());

        copy.setStatus(CopyStatus.LOANED);

        LoanEntity savedLoan = loanRepository.save(loan);
        copyRepository.save(copy);
        return LoanResponse.from(savedLoan);
    }

    @Transactional
    public LoanResponse returnLoan(Long loanId) {
        LoanEntity loan = loanRepository.findById(loanId).orElseThrow(() -> LoanNotFoundException.forId(loanId));
        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw InvalidLoanStateException.forId(loanId);
        }

        loan.setReturnedAt(LocalDateTime.now());
        loan.setStatus(LoanStatus.RETURNED);

        CopyEntity copy = copyRepository
                .findByBarcode(loan.getBarcode())
                .orElseThrow(() -> CopyNotFoundException.forBarcode(loan.getBarcode()));
        copy.setStatus(CopyStatus.AVAILABLE);

        copyRepository.save(copy);
        return LoanResponse.from(loanRepository.save(loan));
    }

    @Transactional
    public LoanResponse renewLoan(Long loanId) {
        LoanEntity loan = loanRepository.findById(loanId).orElseThrow(() -> LoanNotFoundException.forId(loanId));

        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw InvalidLoanStateException.forId(loanId);
        }
        if (loan.getDueAt().isBefore(LocalDateTime.now())) {
            throw RenewalNotAllowedException.forLoan(loanId, "loan is overdue");
        }

        OrganizationEntity organization = organizationService.getById(loan.getOrganizationId());
        if (loan.getRenewalCount() >= organization.getMaxRenewals()) {
            throw RenewalNotAllowedException.forLoan(loanId, "max renewals exceeded");
        }

        loan.setDueAt(loan.getDueAt().plusDays(organization.getLoanPeriodDays()));
        loan.setRenewalCount(loan.getRenewalCount() + 1);

        return LoanResponse.from(loanRepository.save(loan));
    }

    public List<LoanResponse> listLoans(Long userId, LoanStatus status) {
        List<LoanEntity> loans;
        if (userId != null && status != null) {
            loans = loanRepository.findByUserIdAndStatus(userId, status);
        } else if (userId != null) {
            loans = loanRepository.findByUserId(userId);
        } else if (status != null) {
            loans = loanRepository.findAll().stream()
                    .filter(loan -> loan.getStatus() == status)
                    .toList();
        } else {
            loans = loanRepository.findAll();
        }
        return loans.stream().map(LoanResponse::from).toList();
    }

    public List<LoanResponse> listOverdue() {
        return loanRepository.findByDueAtBeforeAndStatus(LocalDateTime.now(), LoanStatus.ACTIVE).stream()
                .map(LoanResponse::from)
                .toList();
    }
}
