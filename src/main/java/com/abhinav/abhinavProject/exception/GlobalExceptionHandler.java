package com.abhinav.abhinavProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> validationExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Validation Failed", errors)
        );
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiResponse> handleMissingCredentials(AuthenticationCredentialsNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), "Incomplete authentication")
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), "User does not exist.")
        );
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ApiResponse> handleAddressNotFound(AddressNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), "The requested address does not exist")
        );
    }


    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<ApiResponse> handleAccountInactive(AccountInactiveException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), "Account is not active")
        );
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ApiResponse> handlePasswordMismatch(PasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), "Passwords do not match")
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), "Username or password is incorrect")
        );
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<ApiResponse> handleAccountStatus(AccountStatusException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), "User Account is locked or inactive")
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        String message = "Media type not supported. Supported types are: " + ex.getSupportedMediaTypes();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
                new ApiResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ex.getMessage(), message)
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> respond(RuntimeException e) {
        return ResponseEntity
                .internalServerError()
                .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), "Internal Server Error")
                );
    }
}
