package com.stylish.stylish.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message) {
        super(message);
    }
}
