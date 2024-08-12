package com.stylish.stylish.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderProcessTask {
    private String email;
    private LocalDateTime timestamp;

    // Default constructor
    public OrderProcessTask() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with email parameter
    public OrderProcessTask(String email) {
        this.email = email;
        this.timestamp = LocalDateTime.now();
    }

}
