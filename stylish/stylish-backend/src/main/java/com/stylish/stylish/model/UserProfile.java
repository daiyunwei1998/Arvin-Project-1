package com.stylish.stylish.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.Map;

@Getter
@Setter
@ToString
public class UserProfile {
    private String provider;
    private String name;
    private String email;
    private String picture;


    public UserProfile(Map<String, String> parameters) {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            try {
                setField(this, fieldName, fieldValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error setting field " + fieldName, e);
            }
        }
    }

    /* Simplicity is better than complex,
        but reflection is too cool not to give it a shot  */
    private static void setField(Object object, String fieldName, Object fieldValue) throws IllegalAccessException {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, fieldValue);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("No field named " + fieldName);
        }
    }

}

