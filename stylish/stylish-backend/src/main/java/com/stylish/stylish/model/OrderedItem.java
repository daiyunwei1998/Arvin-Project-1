package com.stylish.stylish.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderedItem {
    @NotBlank
    private long id; // product id
    private String name;
    private BigDecimal price;
    @NotBlank
    private Color color;
    @NotBlank
    private String size;
    private int qty;
}