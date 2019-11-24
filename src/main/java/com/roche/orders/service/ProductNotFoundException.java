package com.roche.orders.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "product not found")
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String sku) {
        super(String.format("Product %s not found", sku));
    }
}
