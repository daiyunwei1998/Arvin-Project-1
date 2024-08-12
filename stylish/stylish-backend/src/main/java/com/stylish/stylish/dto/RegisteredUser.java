package com.stylish.stylish.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stylish.stylish.model.User;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RegisteredUser {

    public RegisteredUser(User user) {
        this.setId(user.getId());
        this.setProvider(user.getProvider());
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setPicture(user.getPicture());
    }

    @JsonProperty("id")
    private long id;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("picture")
    private String picture;
}
