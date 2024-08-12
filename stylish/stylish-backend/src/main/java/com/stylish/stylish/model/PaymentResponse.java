package com.stylish.stylish.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentResponse {
    private int status;
    private String msg;
    private int amount;

    @JsonProperty("rec_trade_id")
    private String recTradeId;

    @JsonProperty("bank_transaction_id")
    private String bankTransactionId;

    @JsonProperty("card_identifier")
    private String cardIdentifier;
}
