package com.stylish.stylish.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylish.stylish.dto.OrderProcessTask;
import com.stylish.stylish.model.Order;
import com.stylish.stylish.repository.OrderDAO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final OrderDAO orderDAO;
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String REDIS_LIST_KEY = "orderProcessTasks";

    public ReportService(OrderDAO orderDAO, RedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.orderDAO = orderDAO;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<?> reportOrders() {
        List<Order> orderList = orderDAO.getOrdersTotal();
        Map<Long, BigDecimal> aggregatedTotals = orderList.stream()
                .collect(Collectors.groupingBy(
                        Order::getUserId,  // Group by user_id
                        Collectors.reducing(
                                BigDecimal.ZERO,    // Identity value for reduction
                                Order::getTotal,    // Extract BigDecimal total from Order
                                BigDecimal::add     // Accumulate totals
                        )
                ));

        List<Map<String, Object>> result = aggregatedTotals.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("user_id", entry.getKey());
                    map.put("total_payment", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", result));
    }

    public ResponseEntity<?> publishOrderProcessTask(String email) {

        OrderProcessTask newTask = new OrderProcessTask(email);

        try {
            // Serialize the DTO to JSON
            String jsonMessage = objectMapper.writeValueAsString(newTask);

            // Send the JSON to Redis
            redisTemplate.opsForList().leftPush(REDIS_LIST_KEY, jsonMessage);

            return ResponseEntity.ok("successfully sent");

        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
