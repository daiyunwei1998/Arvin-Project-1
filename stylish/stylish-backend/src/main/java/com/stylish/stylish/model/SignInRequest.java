package com.stylish.stylish.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@ToString
public class SignInRequest {
    private long id;
    @NotBlank(message = "provider is required")
    private String provider;
    @Email
    private String email;
    private String password;
    @JsonProperty("access_token")
    private String accessToken;
}
