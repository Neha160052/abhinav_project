package com.abhinav.abhinavProject.exception;

import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> validationExceptionHandler(ValidationException e) {
        return ResponseEntity.badRequest().body("Validation Exception");
    }
}
