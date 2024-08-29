package com.restspringtest.Exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
    
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionsResponses> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionsResponses exceptionsResponses = new ExceptionsResponses(Instant.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionsResponses, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedMathOperationExceptions.class)
    public final ResponseEntity<ExceptionsResponses> handleBadRequestExceptions(Exception ex, WebRequest request) {
        ExceptionsResponses exceptionsResponses = new ExceptionsResponses(Instant.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionsResponses, HttpStatus.BAD_REQUEST);
    }
}
