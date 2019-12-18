package com.denismak.orderservicedemo.rest;

import com.denismak.orderservicedemo.bean.Order;
import com.denismak.orderservicedemo.exception.OrderNotFoundException;
import com.denismak.orderservicedemo.repository.OrderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    private final static String ORDER_API_URL = "/orders";

    private final OrderRepository repository;

    public OrderController(final OrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping(ORDER_API_URL)
    public List<Order> getOrders() {
        return repository.findAll();
    }

    @GetMapping(ORDER_API_URL + "/{id}")
    public Order getOrder(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @PostMapping(ORDER_API_URL)
    public Order createOrder(@RequestBody Order order) {
        return repository.save(order);
    }
}
