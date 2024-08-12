package com.stylish.stylish.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderRequest {

    private String prime;

    @JsonProperty("order")
    private Order order;

}
