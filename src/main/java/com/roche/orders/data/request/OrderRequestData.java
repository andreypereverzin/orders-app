package com.roche.orders.data.request;

import java.util.ArrayList;
import java.util.List;

public class OrderRequestData {

    private String orderId;

    private String email;

    private List<String> productSkus = new ArrayList<>();

    public OrderRequestData(String orderId, String email, List<String> productSkus) {
        this.orderId = orderId;
        this.email = email;
        this.productSkus = productSkus;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getProductSkus() {
        return productSkus;
    }

    @Override
    public String toString() {
        return "Order{" +
                ", orderId='" + orderId + '\'' +
                ", email='" + email + '\'' +
                ", productSkus=" + productSkus +
                '}';
    }
}
