package com.roche.orders.data.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roche.orders.model.Product;

import java.io.Serializable;
import java.util.Date;

public class ProductResponseData implements Serializable {
    @JsonProperty
    private String sku;

    @JsonProperty
    private String name;

    @JsonProperty
    private Long price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date createdAt;

    public ProductResponseData() {
        //
    }

    public ProductResponseData(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.sku = product.getSku();
        this.createdAt = product.getCreatedAt();
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getSku() {
        return sku;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
