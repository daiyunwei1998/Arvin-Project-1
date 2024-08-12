package com.stylish.stylish.repository;

import com.stylish.stylish.model.Order;
import com.stylish.stylish.model.Recipient;

import java.util.List;

public interface OrderDAO {
    public long addOrder(Order order);

    long addRecipient(Recipient recipient);

    public  void updateOrderStatus(long orderId, int statusCode);

    List<Order> getOrdersTotal();
}
