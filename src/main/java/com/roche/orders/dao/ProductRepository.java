package com.roche.orders.dao;

import com.roche.orders.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends CrudRepository <Product, Long> {
    Optional<Product> findBySkuAndIsActive(String sku, Boolean isActive);
    Iterable<Product> findByIsActive(Boolean isActive);
}
