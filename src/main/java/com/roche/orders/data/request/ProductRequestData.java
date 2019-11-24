package com.roche.orders.data.request;

public class ProductRequestData {

    private String name;

    private Long price;

    private String sku;

    public ProductRequestData(String name, Long price, String sku) {
        this.name = name;
        this.price = price;
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
