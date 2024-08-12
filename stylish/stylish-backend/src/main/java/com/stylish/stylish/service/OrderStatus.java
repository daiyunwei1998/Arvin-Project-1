package com.stylish.stylish.service;

public enum OrderStatus {
    UNPAID(0),
    PAID(1),
    FAILED(2);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderStatus fromValue(int value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new UnknownOrderStatus("Unknown order status value: " + value);
    }

    public static class UnknownOrderStatus extends IllegalArgumentException {
        public UnknownOrderStatus(String message) {
            super(message);
        }
    }
}
