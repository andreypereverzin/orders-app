package com.roche.orders.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "order not found")
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super(String.format("Order %s not found", orderId));
    }
}
