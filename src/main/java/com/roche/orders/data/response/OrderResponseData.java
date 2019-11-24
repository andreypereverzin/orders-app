package com.roche.orders.data.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roche.orders.model.Order;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderResponseData {

    @JsonProperty
    private String orderId;

    @JsonProperty
    private String email;

    @JsonProperty
    private List<ProductResponseData> products;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date createdAt;

    public OrderResponseData() {
        //
    }

    public OrderResponseData(Order order) {
        this.orderId = order.getOrderId();
        this.email = order.getEmail();
        this.products = order.getProducts().stream().map(ProductResponseData::new).collect(toList());
        this.createdAt = order.getCreatedAt();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    public List<ProductResponseData> getProducts() {
        return products;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty
    public Long getTotal() {
        return products.stream().mapToLong(p -> p.getPrice()).sum();
    };
}
