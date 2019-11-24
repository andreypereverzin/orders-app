package com.roche.orders.controller;

import com.roche.orders.OrdersApplication;
import com.roche.orders.data.request.OrderRequestData;
import com.roche.orders.data.request.ProductRequestData;
import com.roche.orders.data.response.OrderResponseData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OrdersApplication.class,
        webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTests
{
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void should_create_get_and_update_order() {
        ProductRequestData product1 = new ProductRequestData("Prod1", 200L, "SKU223");
        ResponseEntity<String> responseEntity1 = this.restTemplate
                .postForEntity("http://localhost:" + port + "/roche-orders/products", product1, String.class);
        assertEquals(201, responseEntity1.getStatusCodeValue());

        ProductRequestData product2 = new ProductRequestData("Prod2", 300L, "SKU224");
        ResponseEntity<String> responseEntity2 = this.restTemplate
                .postForEntity("http://localhost:" + port + "/roche-orders/products", product2, String.class);
        assertEquals(201, responseEntity2.getStatusCodeValue());

        OrderRequestData order1 = new OrderRequestData("order1","aa@bb.cc", asList("SKU223"));
        ResponseEntity<String> responseEntity3 = this.restTemplate
                .postForEntity("http://localhost:" + port + "/roche-orders/orders", order1, String.class);
        assertEquals(201, responseEntity3.getStatusCodeValue());
        String url1 = responseEntity3.getHeaders().get("Location").get(0);

        ResponseEntity<OrderResponseData> responseEntity4 = this.restTemplate.getForEntity(url1, OrderResponseData.class);
        assertEquals(200, responseEntity4.getStatusCodeValue());
        assertEquals("order1", responseEntity4.getBody().getOrderId());
        assertEquals("aa@bb.cc", responseEntity4.getBody().getEmail());
        assertEquals(1, responseEntity4.getBody().getProducts().size());
        assertEquals(200L, responseEntity4.getBody().getTotal());

        OrderRequestData order2 = new OrderRequestData("order1","aa@bb.cc", asList("SKU223", "SKU224"));
        this.restTemplate.put("http://localhost:" + port + "/roche-orders/orders", order2);

        ResponseEntity<OrderResponseData> responseEntity5 = this.restTemplate.getForEntity(url1, OrderResponseData.class);
        assertEquals(200, responseEntity5.getStatusCodeValue());
        assertEquals("order1", responseEntity5.getBody().getOrderId());
        assertEquals("aa@bb.cc", responseEntity5.getBody().getEmail());
        assertEquals(2, responseEntity5.getBody().getProducts().size());
        assertEquals(500L, responseEntity5.getBody().getTotal());
    }

    @Test
    public void should_return_not_found_for_non_existing_order() {
        ResponseEntity<Object> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/roche-orders/orders/xxx-xxx", Object.class);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
}
