package com.stylish.stylish.repository;

import java.util.List;
import java.util.stream.Collectors;

public enum Size {
    S(1, "S"),
    M(2, "M"),
    L(3, "L");
    private int id;
    private String label;

    Size(int id, String label) {
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
    public static Size getById(int id) {
        for (Size size : values()) {
            if (size.getId() == id) {
                return size;
            }
        }
        throw new IllegalArgumentException("Invalid ID: " + id);
    }

    // Method to get Size by Label
    public static Size getByLabel(String label) {
        for (Size size : values()) {
            if (size.getLabel().equals(label)) {
                return size;
            }
        }
        throw new IllegalArgumentException("Invalid size label: " + label);
    }

    public static String getLabelById(int id) {
        for (Size size : values()) {
            if (size.getId() == id) {
                return size.getLabel();
            }
        }
        throw new IllegalArgumentException("Invalid size id: " + id);
    }


    public static int getIdByLabel(String label) {
        for (Size size : values()) {
            if (size.getLabel().equals(label)) {
                return size.getId();
            }
        }
        throw new IllegalArgumentException("Invalid size label: " + label);
    }


    // map array of ids to array of sizes
    public static List<String> getSizeList(List<Integer> ids) {
        // Map IDs to Size enums
        List<String> sizes = ids.stream()
                .map(Size::getById)
                .map(Size::getLabel)
                .collect(Collectors.toList());
        return sizes;
    }
}
