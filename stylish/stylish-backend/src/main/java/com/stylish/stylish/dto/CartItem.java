package com.stylish.stylish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long id;
    private String color;
    private String size;
    private int quantity;
    private String picture;
    private double price;
    private String name;
    private double total;
    private String code;
}
