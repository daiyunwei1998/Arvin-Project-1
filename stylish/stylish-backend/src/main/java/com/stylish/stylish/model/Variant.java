package com.stylish.stylish.model;

public class Variant {
    private String colorCode;
    private String size;
    private Integer stock;

    public Variant(String colorCode, String size, Integer stock) {
        this.colorCode = colorCode;
        this.size = size;
        this.stock = stock;
    }

    public Variant(){};

    // Getters and Setters
    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}