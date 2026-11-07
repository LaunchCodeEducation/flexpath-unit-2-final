package org.example.controllers;

import org.example.daos.OrderItemDao;
import org.example.models.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/order-items")
@PreAuthorize("isAuthenticated()")  //Bonus Step 1: Secure all endpoints
public class OrderItemController {

    @Autowired
    private OrderItemDao orderItemDao;

    @GetMapping
    public List<OrderItem> list(@RequestParam(required = false) Long orderId) {
        //Bonus Step 4: Optional query parameter for filtering by orderId
        if (orderId != null) {
            return orderItemDao.getOrderItemsByOrderId(orderId);
        } else {
            return orderItemDao.getAllOrderItems();
        }
    }

    @GetMapping("/{id}")
    public OrderItem get(@PathVariable long id) {
        OrderItem orderItem = orderItemDao.getOrderItemById(id);
        if (orderItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found.");
        }
        return orderItem;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderItem add(@RequestBody OrderItem orderItem) {
        return orderItemDao.createOrderItem(orderItem);
    }

    @PutMapping("/{id}")
    public OrderItem update(@PathVariable long id, @RequestBody OrderItem orderItem) {
        OrderItem existing = orderItemDao.getOrderItemById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found.");
        }
        return orderItemDao.updateOrderItem(id, orderItem);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable long id) {
        int rowsAffected = orderItemDao.deleteOrderItem(id);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found.");
        }
        return rowsAffected;
    }
}
