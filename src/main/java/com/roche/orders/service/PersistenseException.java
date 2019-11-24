package com.roche.orders.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "data access error")
public class PersistenseException extends RuntimeException {
    public PersistenseException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
