package com.stylish.stylish.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Color {
    private int id;
    private String name;
    private String code;

    public Color() {

    }

    public Color(String name, String code) {
        this.name = name;
        this.code = code;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Color color = (Color) obj;
        return Objects.equals(code, color.code) && Objects.equals(name, color.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }
}