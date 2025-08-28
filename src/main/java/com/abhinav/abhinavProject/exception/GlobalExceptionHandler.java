package com.abhinav.abhinavProject.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validationExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), "Validation Failed", errors)
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), "Username or password is incorrect")
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredJwt(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ExceptionResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), "JWT token is expired")
        );
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtSignature(SignatureException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ExceptionResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), "JWT signature is invalid")
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ExceptionResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), "User does not have required privileges")
        );
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<ExceptionResponse> handleAccountStatus(AccountStatusException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ExceptionResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), "User Account is locked or inactive")
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> respond(RuntimeException e) {
        return ResponseEntity
                .internalServerError()
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), "Internal Server Error")
                );
    }
}
