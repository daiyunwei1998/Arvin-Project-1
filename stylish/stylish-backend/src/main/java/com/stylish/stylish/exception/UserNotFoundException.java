package com.stylish.stylish.exception;

public class UserNotFoundException extends RuntimeException {
    private String userId;

    public UserNotFoundException(String message) {
        super(message);
    }
}