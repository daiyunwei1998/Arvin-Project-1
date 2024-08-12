package com.stylish.stylish.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RegisteredUserDTO {

    @JsonProperty("data")
    private RegisteredData registeredData; // helper nested class
}

