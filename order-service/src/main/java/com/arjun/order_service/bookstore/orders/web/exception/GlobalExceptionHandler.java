package com.arjun.order_service.bookstore.orders.web.exception;

import com.arjun.order_service.bookstore.orders.domain.InvalidOrderException;
import com.arjun.order_service.bookstore.orders.domain.OrdersNotFoundException;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final URI NOT_FOUND_TYPE = URI.create("https://api.bookstore.com/errors/not-found");
    private static final URI ISE_FOUND_TYPE = URI.create("https://api.bookstore.com/errors/server-error");
    private static final URI BAD_REQUEST_TYPE = URI.create("https://api.bookstore.com/errors/bad-request");
    private static final String SERVICE_NAME = "order-service";

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnhandledException(Exception e) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(ISE_FOUND_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "Generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(OrdersNotFoundException.class)
    ProblemDetail handleOrderNotFoundException(OrdersNotFoundException ordersNotFoundException){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ordersNotFoundException.getMessage());
        problemDetail.setTitle("Order not Found");
        problemDetail.setType(NOT_FOUND_TYPE);
        problemDetail.setProperty("service",SERVICE_NAME);
        problemDetail.setProperty("error_category","Generic");
        problemDetail.setProperty("timestamp",Instant.now());
        return problemDetail;

    }

    /* Handling Invalid Order Exception */
    @ExceptionHandler(InvalidOrderException.class)
    ProblemDetail handleInvalidOrderException(InvalidOrderException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Invalid Order Creation Request");
        problemDetail.setType(BAD_REQUEST_TYPE);
        problemDetail.setProperty("service",SERVICE_NAME);
        problemDetail.setProperty("error_category","Generic");
        problemDetail.setProperty("timestamp",Instant.now());
        return problemDetail;
    }



    /* Handling Framework Level Exceptions */
    /* Handling MethodArgumentNotValidException using customized manner */
    @Override
    @Nullable protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode httpStatusCode, WebRequest webRequest){

        /* Here simply bind the errorMessages into errorList and manipulate them using ProblemDetail  */
        List<String> errorList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach( (error) -> {
            String errorMessage = error.getDefaultMessage();
            errorList.add(errorMessage);
        });


        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"Invalid request payload");
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(BAD_REQUEST_TYPE);
        problemDetail.setProperty("errors",errorList);
        problemDetail.setProperty("service",SERVICE_NAME);
        problemDetail.setProperty("error_category","Generic");
        problemDetail.setProperty("timestamp",Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }




}
