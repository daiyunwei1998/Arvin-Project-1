package com.stylish.stylish.exception;

public class InvalidRequestBodyException extends RuntimeException  {
    public InvalidRequestBodyException(String message) {
        super(message);
    }
}
