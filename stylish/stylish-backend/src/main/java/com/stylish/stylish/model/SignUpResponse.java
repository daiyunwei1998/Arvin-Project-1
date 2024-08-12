package com.stylish.stylish.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonPropertyOrder({ "access_token", "access_expired", "user" })
public class SignUpResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("access_expired")
    private int accessExpired;
    private User user;
}
