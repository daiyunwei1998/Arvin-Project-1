package com.stylish.stylish.repository;

import com.stylish.stylish.model.Order;
import com.stylish.stylish.model.OrderedItem;
import com.stylish.stylish.model.Recipient;
import com.stylish.stylish.service.OrderStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Log4j2
public class OrderDAOImpl implements OrderDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProductDAO productDAO;

    @Autowired
    public OrderDAOImpl(NamedParameterJdbcTemplate jdbcTemplate, ProductDAO productDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.productDAO = productDAO;
    }
    @Override
    @Transactional
    public long addOrder(Order order) {
        // insert order into database and return orderID
        // 1. add recipient
        long recipientId = addRecipient(order.getRecipient());
        // 2. add order details
        String sql = "INSERT INTO orders (shipping, payment, subtotal, freight, total, recipient_id) " +
                "VALUES (:shipping, :payment, :subtotal, :freight, :total,:recipientId)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("shipping", order.getShipping());
        params.addValue("payment", order.getPayment());
        params.addValue("subtotal", order.getSubtotal());
        params.addValue("freight", order.getFreight());
        params.addValue("total", order.getTotal());
        params.addValue("recipientId", recipientId);

        long orderId;
        try {
            jdbcTemplate.update(sql, params, keyHolder, new String[] {"id"});
            orderId = keyHolder.getKey().longValue();
        } catch (DataAccessException e) {
            log.error("Insert order details failed");
            throw new RuntimeException("Failed to insert recipient", e);
        }
        // 3. add list of ordered items
        for (OrderedItem orderedItem: order.getList()) {
            addOrderedItem(orderedItem, orderId);
        }
        // 4. set status to unpaid
        updateOrderStatus(orderId, OrderStatus.UNPAID.getValue());
        // 5. return order Id
        return orderId;
    }

    @Override
    public long addRecipient(Recipient recipient) {
        // insert recipient into database and return recipientID
        String sql = "INSERT INTO recipients (name, phone, email, address, time) " +
                "VALUES (:name, :phone, :email, :address, :time)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", recipient.getName());
        params.addValue("phone", recipient.getPhone());
        params.addValue("email", recipient.getEmail());
        params.addValue("address", recipient.getAddress());
        params.addValue("time", recipient.getTime());

        try {
            jdbcTemplate.update(sql, params, keyHolder, new String[] {"id"});
            return keyHolder.getKey().longValue();
        } catch (DataAccessException e) {
            log.error("Insert recipient failed");
            throw new RuntimeException("Failed to insert recipient", e);
        }
    }

    private void addOrderedItem(OrderedItem orderedItem, long orderId) {
        // insert recipient into database and return recipientID
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, variant_id) " +
                "VALUES (:order_id, :product_id, :quantity, :variant_id)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("order_id", orderId);
        params.addValue("product_id", orderedItem.getId());
        params.addValue("quantity", orderedItem.getQty());
        try {
            params.addValue("variant_id", productDAO.getVariantId(orderedItem.getId(), orderedItem.getColor().getCode(), orderedItem.getSize())); // get variant id by color + size
            jdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            log.error("Insert order item failed");
            throw new RuntimeException("Failed to insert order item", e);
        }
    }


    @Override
    public void updateOrderStatus(long orderId, int statusCode) {
        String sql = "UPDATE orders SET status = :statusCode WHERE order_id = :orderId";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("orderId", orderId);
        params.addValue("statusCode", statusCode);

        try {
            int rowsAffected = jdbcTemplate.update(sql, params);
            if (rowsAffected > 0) {
                log.info("Order status updated successfully for orderId: {}", orderId);
            } else {
                log.warn("No order found with orderId: {}", orderId);
            }
        } catch (DataAccessException e) {
            log.error("Failed to update order status for orderId: {}", orderId, e);
            throw new RuntimeException("Failed to update order status", e);
        }
    }

    @Override
    public List<Order> getOrdersTotal() {
        String sql = "SELECT user_id, total FROM orders";

        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    private static class OrderRowMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setTotal(rs.getBigDecimal("total"));
            order.setUserId(rs.getLong("user_id"));
            return order;
        }
    }
}