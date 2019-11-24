package com.roche.orders.service;

import com.roche.orders.dao.OrderRepository;
import com.roche.orders.data.request.OrderRequestData;
import com.roche.orders.data.response.OrderResponseData;
import com.roche.orders.model.Order;
import com.roche.orders.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public OrderResponseData createOrder(OrderRequestData orderData) {
        try {
            Order order = new Order();
            order.setOrderId(orderData.getOrderId());
            order.setEmail(orderData.getEmail());
            order.setProducts(getOrderProducts(orderData.getProductSkus()));
            Order orderSaved = orderRepository.save(order);
            return new OrderResponseData(orderSaved);
        } catch (Exception ex) {
            log.error("Error creating order", ex);
            throw new PersistenseException("Error creating order", ex);
        }
    }

    public OrderResponseData updateOrder(OrderRequestData orderData) {
        Order existingOrder = findOrder(orderData.getOrderId());
        try {
            existingOrder.setEmail(orderData.getEmail());
            existingOrder.setProducts(getOrderProducts(orderData.getProductSkus()));
            Order orderSaved = orderRepository.save(existingOrder);
            return new OrderResponseData(orderSaved);
        } catch (Exception ex) {
            log.error("Error updating order", ex);
            throw new PersistenseException("Error updating order", ex);
        }
    }

    public List<OrderResponseData> getOrders(Date from, Date to) {
        List<Order> ordersList = new ArrayList<>();
        Iterable<Order> orders = orderRepository.findAllByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(from, to);
        orders.forEach(ordersList::add);
        return ordersList.stream().map(OrderResponseData::new).collect(toList());
    }

    public OrderResponseData getOrder(String orderId) {
        Order order = findOrder(orderId);
        return new OrderResponseData(order);
    }

    private Order findOrder(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private List<Product> getOrderProducts(List<String> productSkus) {
        return productSkus.stream()
                .map(sku -> productService.findProduct(sku))
                .collect(toList());
    }
}
