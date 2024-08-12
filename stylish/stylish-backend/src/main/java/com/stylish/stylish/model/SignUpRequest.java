package com.stylish.stylish.model;

import com.stylish.stylish.exception.InvalidRequestBodyException;
import lombok.Getter;


@Getter
public class  SignUpRequest {
    private String name;
    private String email;
    private String password;

    public void validateNative() {
        // Validate name
        if (name == null || name.isBlank()) {
            throw new InvalidRequestBodyException("Name is required");
        }
        if (name.length() < 3 || name.length() > 20) {
            throw new InvalidRequestBodyException("Username must be between 3 and 20 characters");
        }

        // Validate email
        if (email == null || email.isBlank()) {
            throw new InvalidRequestBodyException("Email is required");
        }
        if (!email.matches("[^@]+@[^@]+\\.[^@.]+")) {
            throw new InvalidRequestBodyException("Email should be valid");
        }

        // Validate password
        if (password == null || password.isBlank()) {
            throw new InvalidRequestBodyException("Password is required");
        }
        if (password.length() < 6 || password.length() > 40) {
            throw new InvalidRequestBodyException("Password must be between 6 and 40 characters");
        }
    }
}
