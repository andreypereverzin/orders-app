package com.roche.orders.dao;

import com.roche.orders.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    Optional<Order> findByOrderId(String orderId);

    List<Order> findAllByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(Date from, Date to);
}
