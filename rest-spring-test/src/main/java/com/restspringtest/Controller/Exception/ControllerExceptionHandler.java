package com.restspringtest.Controller.Exception;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.restspringtest.Services.Exception.DatabaseException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandartError> handleDatabaseExceptionId(DatabaseException e,
            HttpServletRequest request) {
        String error;
        HttpStatus status;

        if (e.getMessage().contains("Email already registered")) {
            error = "Email already registered";
            status = HttpStatus.BAD_REQUEST;
        } else {
            error = "Resource not found";
            status = HttpStatus.NOT_FOUND;
        }

        StandartError err = new StandartError(Instant.now(), status.value(), error, e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationError> ConstraintViolationException(ConstraintViolationException ex,
            HttpServletRequest request) {
        String error = "Validation Error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.toList());

        ValidationError err = new ValidationError(Instant.now(), status.value(), error, errors,
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> MethodArgumentNotValidException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String error = "Validation Error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ValidationError err = new ValidationError(Instant.now(), status.value(), error, errors,
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ValidationError> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String error = "Validation Error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = Collections.singletonList("Type mismatch " + ex.getName());

        ValidationError err = new ValidationError(Instant.now(), status.value(), error, errors, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ValidationError> handleNotFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        String error = "Validation Error";
        HttpStatus status = HttpStatus.NOT_FOUND;
        List<String> errors = Collections.singletonList("No handler found");

        ValidationError err = new ValidationError(Instant.now(), status.value(), error, errors, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}