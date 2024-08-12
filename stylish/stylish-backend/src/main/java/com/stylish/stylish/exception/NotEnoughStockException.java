package com.stylish.stylish.exception;

public class NotEnoughStockException extends RuntimeException{
    private String message;
    public NotEnoughStockException(String message) {
        super(message);
    }
}
