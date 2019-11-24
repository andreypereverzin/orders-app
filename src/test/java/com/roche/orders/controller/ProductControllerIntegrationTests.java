package com.roche.orders.controller;

import com.roche.orders.OrdersApplication;
import com.roche.orders.data.request.ProductRequestData;
import com.roche.orders.data.response.ProductResponseData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OrdersApplication.class,
        webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTests
{
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void should_create_get_update_and_delete_product() {
        ProductRequestData product = new ProductRequestData("Prod1", 200L, "SKU123");
        ResponseEntity<String> responseEntity1 = this.restTemplate
                .postForEntity("http://localhost:" + port + "/roche-orders/products", product, String.class);
        assertEquals(201, responseEntity1.getStatusCodeValue());
        String url1 = responseEntity1.getHeaders().get("Location").get(0);

        ResponseEntity<ProductResponseData> responseEntity2 = this.restTemplate
                .getForEntity(url1, ProductResponseData.class);
        assertEquals(200, responseEntity2.getStatusCodeValue());
        assertEquals("Prod1", responseEntity2.getBody().getName());
        assertEquals(200L, responseEntity2.getBody().getPrice());
        assertEquals("SKU123", responseEntity2.getBody().getSku());

        ProductRequestData productUpdate = new ProductRequestData("Prod1", 210L, "SKU123");
        this.restTemplate
                .put("http://localhost:" + port + "/roche-orders/products", productUpdate, String.class);

        ResponseEntity<ProductResponseData> responseEntity3 = this.restTemplate
                .getForEntity("http://localhost:" + port + "/roche-orders/products/SKU123", ProductResponseData.class);
        assertEquals(200, responseEntity3.getStatusCodeValue());
        assertEquals("Prod1", responseEntity3.getBody().getName());
        assertEquals(210L, responseEntity3.getBody().getPrice());
        assertEquals("SKU123", responseEntity2.getBody().getSku());

        this.restTemplate.delete(url1);

        ResponseEntity<Object> responseEntity4 = this.restTemplate
                .getForEntity("http://localhost:" + port + "/roche-orders/products/SKU123", Object.class);
        assertEquals(404, responseEntity4.getStatusCodeValue());
    }

    @Test
    public void should_return_not_found_for_non_existing_product() {
        ResponseEntity<Object> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/roche-orders/products/SKU124", Object.class);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
}
