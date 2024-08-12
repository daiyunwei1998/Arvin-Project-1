package com.stylish.stylish.service;

import com.stylish.stylish.dto.DataResponse;
import com.stylish.stylish.exception.UnsuccessfulPaymentException;
import com.stylish.stylish.model.*;
import com.stylish.stylish.repository.OrderDAO;
import com.stylish.stylish.repository.PaymentDAO;
import com.stylish.stylish.repository.ProductDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class OrderService {
    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final PaymentService paymentService;
    private final PaymentDAO paymentDAO;

    public OrderService(OrderDAO orderDAO, ProductDAO productDAO, PaymentService paymentService, PaymentDAO paymentDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.paymentService = paymentService;
        this.paymentDAO = paymentDAO;
    }

    @Transactional
    public ResponseEntity<?> checkout(OrderRequest orderRequest, long orderId) throws UnsuccessfulPaymentException {
            processOrderAndPayment(orderRequest.getOrder(), orderId, orderRequest.getPrime());
            return ResponseEntity.ok(new DataResponse(Map.of("number", orderId)));
    }


    public void updateOrderStatusToFailed(long orderId) {
        orderDAO.updateOrderStatus(orderId, OrderStatus.FAILED.getValue());
    }

    @Transactional
    private void processOrderAndPayment(Order order, long orderId, String prime) {
        // 1. Reduce stock
        for (OrderedItem orderedItem : order.getList()) {
            productDAO.reduceStockByColorAndSize(orderedItem.getId(),
                    orderedItem.getColor().getCode(), orderedItem.getSize(), orderedItem.getQty());
        }

        // 2. Handle payment
        PaymentResponse response = paymentService.sendPaymentRequest(prime, order, orderId);

        if (response.getStatus() != 0) {
            throw new UnsuccessfulPaymentException("Payment unsuccessful: " + response.getMsg());
        }

        // 3. Update order status to PAID
        orderDAO.updateOrderStatus(orderId, OrderStatus.PAID.getValue());

        // 4. Save payment record
        paymentDAO.savePaymentRecord(response, orderId);
    }
}