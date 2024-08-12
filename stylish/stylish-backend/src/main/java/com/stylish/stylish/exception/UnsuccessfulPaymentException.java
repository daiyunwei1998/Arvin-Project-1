package com.stylish.stylish.exception;

public class UnsuccessfulPaymentException extends RuntimeException{

    public UnsuccessfulPaymentException(String message) {
        super(message);
    }
}
