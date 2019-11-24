package com.roche.orders.service;

import com.roche.orders.dao.ProductRepository;
import com.roche.orders.data.request.ProductRequestData;
import com.roche.orders.data.response.ProductResponseData;
import com.roche.orders.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseData createProduct(ProductRequestData productData) {
        try {
            Product product = new Product(
                    productData.getName(),
                    productData.getPrice(),
                    productData.getSku()
            );
            Product productSaved = productRepository.save(product);
            return new ProductResponseData(productSaved);
        } catch (Exception ex) {
            log.error("Error creating product", ex);
            throw new PersistenseException("Error creating product", ex);
        }
    }

    public ProductResponseData updateProduct(ProductRequestData productData) {
        Product existingProduct = findProduct(productData.getSku());
        try {
            existingProduct.setName(productData.getName());
            existingProduct.setPrice(productData.getPrice());
            Product productSaved = productRepository.save(existingProduct);
            return new ProductResponseData(productSaved);
        } catch (Exception ex) {
            log.error("Error updating product", ex);
            throw new PersistenseException("Error updating product", ex);
        }
    }

    public List<ProductResponseData> getProducts() {
        List<Product> productsList = new ArrayList<>();
        Iterable<Product> products = productRepository.findByIsActive(true);
        products.forEach(productsList::add);
        return productsList.stream().map(ProductResponseData::new).collect(toList());
    }

    public ProductResponseData getProduct(String sku) {
        Product product = findProduct(sku);
        return new ProductResponseData(product);
    }

    public Product findProduct(String sku) {
        return productRepository.findBySkuAndIsActive(sku, true)
                .orElseThrow(() -> new ProductNotFoundException(sku));
    }

    public void deleteProduct(String sku) {
        Product existingProduct = findProduct(sku);
        existingProduct.setIsActive(false);
        productRepository.save(existingProduct);
    }
}
