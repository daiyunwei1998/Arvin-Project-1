package com.stylish.stylish.repository;

import com.stylish.stylish.model.PaymentResponse;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PaymentDAOImpl implements PaymentDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PaymentDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void savePaymentRecord(PaymentResponse paymentResponse, long orderId) {
        String sql = "INSERT INTO payment (order_id, amount, rec_trade_id, bank_transaction_id, card_identifier) " +
                "VALUES (:orderId, :amount, :recTradeId, :bankTransactionId, :cardIdentifier)";

        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("amount", paymentResponse.getAmount());
        params.put("recTradeId", paymentResponse.getRecTradeId());
        params.put("bankTransactionId", paymentResponse.getBankTransactionId());
        params.put("cardIdentifier", paymentResponse.getCardIdentifier());

        jdbcTemplate.update(sql, params);
    }
}
