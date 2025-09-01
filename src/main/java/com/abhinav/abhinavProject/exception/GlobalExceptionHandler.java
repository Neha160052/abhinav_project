package com.abhinav.abhinavProject.exception;

import com.abhinav.abhinavProject.utils.MessageUtil;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    MessageUtil messageUtil;

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Validation Excpetion",ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> validationExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                new ApiResponse(HttpStatus.BAD_REQUEST.value(), messageUtil.getMessage("validation.failed"), errors)
        );
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiResponse> handleMissingCredentials(AuthenticationCredentialsNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), messageUtil.getMessage("auth.credentials.missing"))
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), messageUtil.getMessage("user.notfound"))
        );
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ApiResponse> handleAddressNotFound(AddressNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), messageUtil.getMessage("address.notfound"))
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCatNotFound(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), messageUtil.getMessage("category.notFound"))
        );
    }

    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<ApiResponse> handleAccountInactive(AccountInactiveException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), messageUtil.getMessage("account.inactive"))
        );
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ApiResponse> handlePasswordMismatch(PasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), messageUtil.getMessage("password.mismatch"))
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), messageUtil.getMessage("token.invalid"))
        );
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiResponse> handleExpiredToken(TokenExpiredException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), messageUtil.getMessage("token.expired"))
        );
    }

    @ExceptionHandler(AccountActiveException.class)
    public ResponseEntity<ApiResponse> handleAccountActive(AccountActiveException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), messageUtil.getMessage("account.active"))
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), messageUtil.getMessage("bad.credentials"))
        );
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<ApiResponse> handleAccountStatus(AccountStatusException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), messageUtil.getMessage("account.status"))
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        String message = messageUtil.getMessage("media.unsupported") + ex.getSupportedMediaTypes();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
                new ApiResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ex.getMessage(), message)
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> respond(RuntimeException e) {
        return ResponseEntity
                .internalServerError()
                .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), messageUtil.getMessage("server.error"))
                );
    }
}
