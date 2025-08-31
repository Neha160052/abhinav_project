package com.abhinav.abhinavProject.exception;

public class AccountActiveException extends RuntimeException {
    public AccountActiveException(String message) {
        super(message);
    }
}
