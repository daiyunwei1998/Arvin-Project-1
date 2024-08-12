package com.stylish.stylish.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RegisteredData {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("access_expired")
    private String accessExpired;

    @JsonProperty("user")
    private RegisteredUser registeredUser;
}
