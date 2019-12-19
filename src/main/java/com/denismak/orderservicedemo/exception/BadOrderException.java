package com.denismak.orderservicedemo.exception;

import com.denismak.orderservicedemo.bean.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadOrderException extends RuntimeException {
    public BadOrderException(Order order) {
        super("Order " + order + " is invalid. Both customer and product fields are required");
    }
}
