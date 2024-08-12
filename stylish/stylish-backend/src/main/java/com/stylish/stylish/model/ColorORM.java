package com.stylish.stylish.model;

import jakarta.persistence.*;

@Table(name = "new_colors")
public class ColorORM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "color_name")
    private String name;
    private String code;

    public ColorORM(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
