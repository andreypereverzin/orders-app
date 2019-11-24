package com.roche.orders.controller;

import com.roche.orders.data.request.ProductRequestData;
import com.roche.orders.data.response.ProductResponseData;
import com.roche.orders.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Create a product")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created product"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping(path = "/products", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequestData product) {
        log.debug("Started creating product");

        ProductResponseData productCreated = productService.createProduct(product);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{sku}")
                .buildAndExpand(productCreated.getSku())
                .toUri();

        log.debug("Created product {}", productCreated);

        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "Update a product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated product"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PutMapping(path = "/products", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> updateProduct(@RequestBody ProductRequestData product) {
        log.debug("Started updating product {}", product);

        ProductResponseData productUpdated = productService.updateProduct(product);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get list of products", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved products")
    })
    @GetMapping(path = "/products", produces = "application/json")
    public ResponseEntity<List<ProductResponseData>> getProducts() {
        log.debug("Started getting products");

        List<ProductResponseData> products = productService.getProducts();

        log.debug("Got products {}", products.size());

        return ResponseEntity.ok(products);
    }

    @ApiOperation(value = "Get a product", response = ProductResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved product"),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @GetMapping(path = "/products/{sku}", produces = "application/json")
    public ResponseEntity<ProductResponseData> getProduct(@PathVariable String sku) {
        log.debug("Started getting product {}", sku);

        ProductResponseData product = productService.getProduct(sku);

        log.debug("Got product {}", product);

        return ResponseEntity.ok(product);
    }

    @ApiOperation(value = "Delete a product", response = ProductResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted product"),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @DeleteMapping(path = "/products/{sku}", produces = "application/json")
    public ResponseEntity<Void> deleteProduct(@PathVariable String sku) {
        log.debug("Started deleting product {}", sku);

        productService.deleteProduct(sku);

        log.debug("Deleted product {}", sku);

        return ResponseEntity.ok().build();
    }
}
