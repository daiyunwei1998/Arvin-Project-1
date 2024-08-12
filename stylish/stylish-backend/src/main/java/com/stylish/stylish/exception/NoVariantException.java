package com.stylish.stylish.exception;

import org.springframework.dao.EmptyResultDataAccessException;

public class NoVariantException extends RuntimeException {
    String message;
    public NoVariantException(String message) {
        super(message);
    }
}
