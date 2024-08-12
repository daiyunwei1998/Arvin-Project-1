package com.stylish.stylish.repository;

import java.util.List;
import java.util.stream.Collectors;

public enum Category {
    MEN(1, "men"),
    WOMEN(2, "women"),
    ACCESSORIES(3, "accessories");
    private int id;
    private String label;

    Category(int id, String label) {
        this.id = id;
        this.label = label;
    }
    public int getId() {
        return id;
    }
    public String getLabel() {
        return label;
    }

    // Method to get Size by ID
    public static Category getById(int id) {
        for (Category category : values()) {
            if (category.getId() == id) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid ID: " + id);
    }

    // Method to get ID by label
    public static int getIdByLabel(String label) {
        for (Category category : values()) {
            if (category.getLabel().equalsIgnoreCase(label)) {
                return category.getId();
            }
        }
        throw new IllegalArgumentException("Invalid label: " + label);
    }

    // Method to get Label by ID
    public static String getLabelbyId(int id) {
        for (Category category : values()) {
            if (category.getId() == id) {
                return category.getLabel();
            }
        }
        throw new IllegalArgumentException("Invalid ID: " + id);
    }
}
