package com.stylish.stylish.repository;

import com.stylish.stylish.model.PaymentResponse;

public interface PaymentDAO {
    void savePaymentRecord(PaymentResponse paymentResponse, long orderId);
}
