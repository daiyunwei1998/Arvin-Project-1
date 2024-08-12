package com.stylish.stylish.exception;

public class InvalidCredentialsException extends RuntimeException {
    private String userId;

    public InvalidCredentialsException(String message) {
        super(message);
    }

}
