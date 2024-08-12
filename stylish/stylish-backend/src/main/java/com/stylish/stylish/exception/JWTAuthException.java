package com.stylish.stylish.exception;

import org.springframework.security.core.AuthenticationException;

public class JWTAuthException {
    public static class EmptyJwtTokenException extends AuthenticationException {
        public EmptyJwtTokenException(String msg) {
            super(msg);
        }
    }

    public static class InvalidJwtTokenException extends AuthenticationException {
        public InvalidJwtTokenException(String msg) {
            super(msg);
        }
    }
}
