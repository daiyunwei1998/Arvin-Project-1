package com.stylish.stylish.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String prime;

    @JsonProperty("partner_key")
    private String partnerKey;

    @JsonProperty("merchant_id")
    private String merchantId;

    private String details;
    private BigDecimal amount;
    private Cardholder cardholder;
    private boolean remember;

    @Data
    public static class Cardholder {
        @JsonProperty("phone_number")
        private String phoneNumber;
        private String name;
        private String email;
        private String address;

    }
}

