package com.roche.orders.service;

import com.roche.orders.dao.OrderRepository;
import com.roche.orders.data.request.OrderRequestData;
import com.roche.orders.data.request.ProductRequestData;
import com.roche.orders.data.response.OrderResponseData;
import com.roche.orders.model.Order;
import com.roche.orders.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.util.DateUtil.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    private static final String EMAIL = "aa@bb.cc";
    private static final String PROD_1 = "Prod1";
    private static final String PROD_2 = "Prod2";
    private static final String ORDER_1 = "order1";
    private static final String ORDER_2 = "order2";
    private static final String SKU_1 = "SKU1";
    private static final String SKU_2 = "SKU2";

    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private OrderRepository orderRepository;

    private OrderRequestData orderRequestData1;

    private ProductRequestData productRequestData1;

    private Order order1;

    private Order order2;

    private Product product1;

    private Product product2;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(orderRepository, productService);
        productRequestData1 = new ProductRequestData(PROD_1, 200L, SKU_1);
        product1 = new Product(PROD_1, 200L, SKU_1);
        product2 = new Product(PROD_2, 300L, SKU_2);
        orderRequestData1 = new OrderRequestData(ORDER_1, EMAIL, asList(SKU_1));
        order1 = new Order(ORDER_1, EMAIL, asList(product1));
        order2 = new Order(ORDER_2, EMAIL, asList(product2));
    }

    @Test
    void createOrder_shouldCreateOrder() {
        //given
        given(orderRepository.save(any(Order.class))).willReturn(order1);

        // when
        OrderResponseData res = orderService.createOrder(orderRequestData1);

        // then
        assertThat(res.getOrderId()).isEqualTo(ORDER_1);
        assertThat(res.getEmail()).isEqualTo(EMAIL);
        assertThat(res.getTotal()).isEqualTo(200L);
    }

    @Test
    void createOrder_shouldThrowPersistenceExceptionIfRepositoryThrowsException() {
        //given
        given(orderRepository.save(any(Order.class))).willThrow(new RuntimeException());

        // when
        PersistenseException exception = catchThrowableOfType(() ->
                        orderService.createOrder(orderRequestData1),
                PersistenseException.class
        );

        // then
        assertThat(exception.getMessage()).isEqualTo("Error creating order");
    }

    @Test
    void updateOrder_shouldUpdateOrder() {
        OrderRequestData orderRequestData2 = new OrderRequestData(ORDER_1, "aa@bb.dd", asList(SKU_1));
        given(orderRepository.findByOrderId(ORDER_1)).willReturn(Optional.of(order1));
        given(orderRepository.save(any(Order.class))).willReturn(new Order(ORDER_1, "aa@bb.dd", asList(product1)));

        // when
        OrderResponseData res = orderService.updateOrder(orderRequestData2);

        // then
        assertThat(res.getOrderId()).isEqualTo(ORDER_1);
        assertThat(res.getEmail()).isEqualTo("aa@bb.dd");
    }

    @Test
    void updateOrder_shouldThrowOrderNotFoundExceptionExceptionIfOrderDoesNotExist() {
        // given
        given(orderRepository.findByOrderId(ORDER_1)).willReturn(Optional.empty());

        // when
        OrderNotFoundException exception = catchThrowableOfType(() ->
                        orderService.updateOrder(orderRequestData1),
                OrderNotFoundException.class
        );

        // then
        assertThat(exception.getMessage()).isEqualTo("Order order1 not found");
    }

    @Test
    void getOrders_shouldReturnOrders() {
        //given
        given(orderRepository.findAllByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
                any(Date.class),any(Date.class))
        ).willReturn(asList(order1, order2));

        // when
        List<OrderResponseData> res = orderService.getOrders(now(), now());

        // then
        assertThat(res.size()).isEqualTo(2);
        List<String> orderIds = res.stream().map(OrderResponseData::getOrderId).collect(toList());
        assertThat(orderIds).containsOnly(ORDER_1, ORDER_2);
    }

    @Test
    void getOrder_shouldReturnOrder() {
        //given
        given(orderRepository.findByOrderId(ORDER_1)).willReturn(Optional.of(order1));

        // when
        OrderResponseData res = orderService.getOrder(ORDER_1);

        // then
        assertThat(res.getOrderId()).isEqualTo(ORDER_1);
        assertThat(res.getEmail()).isEqualTo(EMAIL);
        assertThat(res.getTotal()).isEqualTo(200L);
        assertThat(res.getProducts()).hasSize(1);
    }

    @Test
    void getOrder_shouldThrowOrderNotFoundExceptionExceptionIfOrderDoesNotExist() {
        // given
        given(orderRepository.findByOrderId(ORDER_1)).willReturn(Optional.empty());

        // when
        OrderNotFoundException exception = catchThrowableOfType(() ->
                        orderService.getOrder(ORDER_1),
                OrderNotFoundException.class
        );

        // then
        assertThat(exception.getMessage()).isEqualTo("Order order1 not found");
    }
}
