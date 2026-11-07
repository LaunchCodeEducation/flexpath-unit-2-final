package org.example.daos;

import org.example.models.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<OrderItem> getAllOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM order_items;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            orderItems.add(mapRowToOrderItem(results));
        }
        return orderItems;
    }

    //Required for Bonus Step 4
    public List<OrderItem> getOrderItemsByOrderId(long orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, orderId);
        while (results.next()) {
            orderItems.add(mapRowToOrderItem(results));
        }
        return orderItems;
    }

    public OrderItem getOrderItemById(long id) {
        OrderItem orderItem = null;
        String sql = "SELECT * FROM order_item_id WHERE order_item_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            orderItem = mapRowToOrderItem(results);
        }
        return orderItem;
    }

    public OrderItem createOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?) RETURNING order_item_id;";
        Long newId = jdbcTemplate.queryForObject(sql, Long.class, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity());
        return getOrderItemById(newId);
    }

    public OrderItem updateOrderItem(long id, OrderItem orderItem) {
        String sql = "UPDATE order_items WHERE order_item_id = ?, product_id = ?, quantity = ? WHERE order_item_id = ?;";
        jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity(), id);
        return getOrderItemById(id);
    }

    public int deleteOrderItem(long id) {
        String sql = "DELETE FROM order_items WHERE order_item_id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    private OrderItem mapRowToOrderItem(SqlRowSet row) {
        OrderItem item = new OrderItem();
        item.setOrderItemId(row.getLong("order_item_id"));
        item.setOrderId(row.getInt("order_id"));
        item.setProductId(row.getInt("product_id"));
        item.setQuantity(row.getInt("quantity"));
        return item;
    }

}
