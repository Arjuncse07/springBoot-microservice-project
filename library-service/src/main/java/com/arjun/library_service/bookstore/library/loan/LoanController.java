package com.arjun.library_service.bookstore.library.loan;

import com.arjun.library_service.bookstore.library.domain.LoanStatus;
import com.arjun.library_service.bookstore.library.loan.dto.CheckoutLoanRequest;
import com.arjun.library_service.bookstore.library.loan.dto.LoanResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library/loans")
@Tag(name = "Loans", description = "Loan checkout and return")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Checkout a copy")
    LoanResponse checkout(@Valid @RequestBody CheckoutLoanRequest request) {
        return loanService.checkout(request.userId(), request.barcode());
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Return a loaned copy")
    LoanResponse returnLoan(@PathVariable Long id) {
        return loanService.returnLoan(id);
    }

    @PostMapping("/{id}/renew")
    @Operation(summary = "Renew an active loan")
    LoanResponse renewLoan(@PathVariable Long id) {
        return loanService.renewLoan(id);
    }

    @GetMapping("/overdue")
    @Operation(summary = "List overdue active loans")
    List<LoanResponse> listOverdue() {
        return loanService.listOverdue();
    }

    @GetMapping
    @Operation(summary = "List loans with optional filters")
    List<LoanResponse> listLoans(
            @RequestParam(required = false) Long userId, @RequestParam(required = false) LoanStatus status) {
        return loanService.listLoans(userId, status);
    }
}
