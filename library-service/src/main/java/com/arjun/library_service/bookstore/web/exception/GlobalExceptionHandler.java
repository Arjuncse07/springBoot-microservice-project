package com.arjun.library_service.bookstore.web.exception;

import com.arjun.library_service.bookstore.library.exception.AccessCardNotFoundException;
import com.arjun.library_service.bookstore.library.exception.CopyNotAvailableException;
import com.arjun.library_service.bookstore.library.exception.CopyNotFoundException;
import com.arjun.library_service.bookstore.library.exception.DuplicateBarcodeException;
import com.arjun.library_service.bookstore.library.exception.InvalidLoanStateException;
import com.arjun.library_service.bookstore.library.exception.LoanNotFoundException;
import com.arjun.library_service.bookstore.library.exception.OrganizationNotFoundException;
import com.arjun.library_service.bookstore.library.exception.ProductNotFoundException;
import com.arjun.library_service.bookstore.library.exception.RenewalNotAllowedException;
import java.net.URI;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final URI NOT_FOUND_TYPE = URI.create("https://api.bookstore.com/errors/not-found");
    private static final URI CONFLICT_TYPE = URI.create("https://api.bookstore.com/errors/conflict");
    private static final URI VALIDATION_TYPE = URI.create("https://api.bookstore.com/errors/validation");
    private static final URI FORBIDDEN_TYPE = URI.create("https://api.bookstore.com/errors/forbidden");
    private static final URI ISE_TYPE = URI.create("https://api.bookstore.com/errors/server-error");
    private static final String SERVICE_NAME = "library-service";

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnhandledException(Exception exception) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(ISE_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "Generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler({
        CopyNotFoundException.class,
        ProductNotFoundException.class,
        OrganizationNotFoundException.class,
        LoanNotFoundException.class,
        AccessCardNotFoundException.class
    })
    ProblemDetail handleNotFoundException(RuntimeException exception) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, "Not Found", NOT_FOUND_TYPE, exception.getMessage());
    }

    @ExceptionHandler({DuplicateBarcodeException.class, CopyNotAvailableException.class, InvalidLoanStateException.class})
    ProblemDetail handleConflictException(RuntimeException exception) {
        return buildProblemDetail(HttpStatus.CONFLICT, "Conflict", CONFLICT_TYPE, exception.getMessage());
    }

    @ExceptionHandler(RenewalNotAllowedException.class)
    ProblemDetail handleRenewalNotAllowed(RenewalNotAllowedException exception) {
        return buildProblemDetail(HttpStatus.FORBIDDEN, "Forbidden", FORBIDDEN_TYPE, exception.getMessage());
    }

    @Override
    protected org.springframework.http.ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            org.springframework.http.HttpHeaders headers,
            org.springframework.http.HttpStatusCode status,
            org.springframework.web.context.request.WebRequest request) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Validation Error");
        problemDetail.setType(VALIDATION_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("timestamp", Instant.now());
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            problemDetail.setProperty(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return org.springframework.http.ResponseEntity.badRequest().body(problemDetail);
    }

    private ProblemDetail buildProblemDetail(
            HttpStatus status, String title, URI type, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(type);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "Business");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
