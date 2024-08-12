package com.stylish.stylish.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.stylish.stylish.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@JsonPropertyOrder({ "access_token", "access_expired", "user" })
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonIgnore
    private String refreshToken;  // TODO : add this functionality
    @JsonProperty("access_expired")
    @Value("${jwt.access.expired}")
    private int accessExpired;
    private User user;
}

