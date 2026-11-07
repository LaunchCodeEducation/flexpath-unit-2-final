package org.example.daos;

import org.example.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            orders.add(mapRowToOrder(results));
        }
        return orders;
    }

    //Required for Bonus step 3
    public List<Order> getOrdersByUsername(String username) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while (results.next()) {
            orders.add(mapRowToOrder(results));
        }
        return orders;
    }

    public Order getOrderById(long id) {
        Order order = null;
        String sql = "SELECT * FROM orders WHERE order_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            order = mapRowToOrder(results);
        }
        return order;
    }

    public Order createOrder(Order order) {
        String sql = "INSERT INTO orders(username, shipping_address, shipping_city, shipping_state, shipping_zip, status) VALUES (?, ?, ?, ?, ?, ?) RETURNING order_id;";
        long id = 0;
        Long newId = jdbcTemplate.queryForObject(sql, Long.class, order.getUsername(), order.getShippingAddress(), order.getShippingCity(), order.getShippingState(), order.getShippingCity(), order.getShippingZip(), order.getStatus(), id);
        return getOrderById(id);
    }

    public Order updateOrder(long id, Order order) {
        String sql = "UPDATE orders SET username = ?, shipping_address = ?, shipping_city = ?, shipping_state = ?, shipping_zip = ?, status = ? WHERE order_id = ?;";
        jdbcTemplate.update(sql, order.getUsername(), order.getShippingAddress(), order.getShippingCity(), order.getShippingState(), order.getShippingZip(), order.getStatus(), id);
        return getOrderById(id);
    }

    public int deleteOrder(long id) {
        String sql = "DELETE FROM orders WHERE order_id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    private Order mapRowToOrder(SqlRowSet row) {
        Order order = new Order();
        order.setUsername(row.getString("username"));
        order.setShippingAddress(row.getString("shipping_address"));
        order.setShippingCity(row.getString("shipping_city"));
        order.setShippingState(row.getString("shipping_state"));
        order.setShippingZip(row.getString("shipping_zip"));
        order.setStatus(row.getString("status"));
        return order;
    }
}
