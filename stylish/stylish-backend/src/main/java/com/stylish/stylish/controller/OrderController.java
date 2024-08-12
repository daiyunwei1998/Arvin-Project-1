package com.stylish.stylish.controller;

import com.stylish.stylish.errors.ErrorResponse;
import com.stylish.stylish.exception.UnsuccessfulPaymentException;
import com.stylish.stylish.model.OrderRequest;
import com.stylish.stylish.repository.OrderDAO;
import com.stylish.stylish.repository.PaymentDAO;
import com.stylish.stylish.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController()
@RequestMapping("/api/${apiVersion}/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderDAO orderDAO;

    public OrderController(OrderService orderService, OrderDAO orderDAO) {
        this.orderService = orderService;
        this.orderDAO = orderDAO;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@Valid @RequestBody OrderRequest orderRequest) {
        log.info(orderRequest);
        long orderId;
        try {
            orderId =  orderDAO.addOrder(orderRequest.getOrder());
        }   catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
        }

        try {
            return orderService.checkout(orderRequest,orderId );
        } catch (UnsuccessfulPaymentException e) {
            orderService.updateOrderStatusToFailed(orderId);
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
        }
    }

}
