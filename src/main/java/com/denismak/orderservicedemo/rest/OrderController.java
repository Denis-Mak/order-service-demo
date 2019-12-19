package com.denismak.orderservicedemo.rest;

import com.denismak.orderservicedemo.bean.Order;
import com.denismak.orderservicedemo.exception.BadOrderException;
import com.denismak.orderservicedemo.exception.OrderNotFoundException;
import com.denismak.orderservicedemo.exception.UnexpectedException;
import com.denismak.orderservicedemo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    private final static String ORDER_API_URL = "/api/orders";

    private final OrderRepository repository;

    @Autowired
    public OrderController(final OrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping(ORDER_API_URL)
    public List<Order> getOrders() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new UnexpectedException();
        }
    }

    @GetMapping(ORDER_API_URL + "/{id}")
    public Order getOrder(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @PostMapping(ORDER_API_URL)
    public Order createOrder(@RequestBody Order order) {
        if (StringUtils.isEmpty(order.getCustomer()) || StringUtils.isEmpty(order.getProduct())) {
            throw new BadOrderException(order);
        }
        try {
            return repository.save(order);
        } catch (Exception e) {
            throw new UnexpectedException();
        }
    }
}
