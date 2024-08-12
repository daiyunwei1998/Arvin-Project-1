package com.stylish.stylish.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Order {
    private String shipping;
    private String payment;
    private BigDecimal subtotal;
    private BigDecimal freight;
    private BigDecimal total;
    private Recipient recipient;
    private List<OrderedItem> list;
    @JsonProperty("user_id")
    private long userId;

}