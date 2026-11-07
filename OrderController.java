package org.example.controllers;

import org.example.daos.OrderDao;
import org.example.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@PreAuthorize("isAuthorized()")  //Bonus Step 1: Secure Endpoints
public class OrderController {

    @Autowired
    private OrderDao orderDao;

    @GetMapping
    public List<Order> list(@RequestParam(required = false) String username) {
        //Bonus Step 3: Optional query parameter for filtering by username
        if (username != null) {
            return orderDao.getOrdersByUsername(username);
        } else {
            return orderDao.getAllOrders();
        }
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable long id) {
        Order order = orderDao.getOrderById(id);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
        }
        return order;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Order add(@RequestBody Order order, Principal principal) {
        //Bonus Step 2: Constrain order creation using Principal
        order.setUsername(principal.getName());
        return orderDao.createOrder(order);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable long id, @RequestBody Order order) {
        Order existing = orderDao.getOrderById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
        }

        //Bonus Step 2: Constrain order update using Principal
        Product principal = new Product();
        order.setUsername(principal.getName());
        return orderDao.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable long id) {
        int rowsAffected = orderDao.deleteOrder(id);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
        }
        return rowsAffected;
    }
}
