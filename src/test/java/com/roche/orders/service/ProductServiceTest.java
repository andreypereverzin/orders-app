package com.roche.orders.service;

import com.roche.orders.dao.ProductRepository;
import com.roche.orders.data.request.ProductRequestData;
import com.roche.orders.data.response.ProductResponseData;
import com.roche.orders.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final String PROD_1 = "Prod1";
    private static final String SKU_1 = "SKU1";
    private static final String PROD_2 = "Prod2";
    private static final String SKU_2 = "SKU2";

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private ProductRequestData productRequestData1;

    private Product product1;

    private Product product2;

    @BeforeEach
    public void setUp() {
        productService = new ProductService(productRepository);
        productRequestData1 = new ProductRequestData(PROD_1, 200L, SKU_1);
        product1 = new Product(PROD_1, 200L, SKU_1);
        product2 = new Product(PROD_2, 300L, SKU_2);
    }

    @Test
    void createProduct_shouldCreateProduct() {
        //given
        given(productRepository.save(any(Product.class))).willReturn(product1);

        // when
        ProductResponseData res = productService.createProduct(productRequestData1);

        // then
        assertThat(res.getName()).isEqualTo(PROD_1);
        assertThat(res.getPrice()).isEqualTo(200L);
        assertThat(res.getSku()).isEqualTo(SKU_1);
    }

    @Test
    void createProduct_shouldThrowPersistenceExceptionIfRepositoryThrowsException() {
        //given
        given(productRepository.save(any(Product.class))).willThrow(new RuntimeException());

        // when
        PersistenseException exception = catchThrowableOfType(() ->
                        productService.createProduct(productRequestData1),
                PersistenseException.class
        );

        // then
        assertThat(exception.getMessage()).isEqualTo("Error creating product");
    }

    @Test
    void updateProduct_shouldUpdateProduct() {
        ProductRequestData productRequestData2 = new ProductRequestData(PROD_1, 210L, SKU_1);
        given(productRepository.findBySkuAndIsActive(SKU_1, true)).willReturn(Optional.of(product1));
        given(productRepository.save(any(Product.class))).willReturn(product1);

        // when
        ProductResponseData res = productService.updateProduct(productRequestData2);

        // then
        assertThat(res.getName()).isEqualTo(PROD_1);
        assertThat(res.getPrice()).isEqualTo(210L);
        assertThat(res.getSku()).isEqualTo(SKU_1);
    }

    @Test
    void updateProduct_shouldThrowProductNotFoundExceptionExceptionIfProductDoesNotExist() {
        // given
        given(productRepository.findBySkuAndIsActive(SKU_1, true)).willReturn(Optional.empty());

        // when
        ProductNotFoundException exception = catchThrowableOfType(() ->
                        productService.updateProduct(productRequestData1),
                ProductNotFoundException.class
        );

        // then
        assertThat(exception.getMessage()).isEqualTo("Product SKU1 not found");
    }

    @Test
    void getProducts_shouldReturnProducts() {
        //given
        given(productRepository.findByIsActive(true)).willReturn(asList(product1, product2));

        // when
        List<ProductResponseData> res = productService.getProducts();

        // then
        assertThat(res.size()).isEqualTo(2);
        List<String> skus = res.stream().map(ProductResponseData::getSku).collect(toList());
        assertThat(skus).containsOnly(SKU_1, SKU_2);
    }

    @Test
    void getProduct_shouldReturnProduct() {
        //given
        given(productRepository.findBySkuAndIsActive(SKU_1, true)).willReturn(Optional.of(product1));

        // when
        ProductResponseData res = productService.getProduct(SKU_1);

        // then
        assertThat(res.getName()).isEqualTo(PROD_1);
        assertThat(res.getPrice()).isEqualTo(200L);
        assertThat(res.getSku()).isEqualTo(SKU_1);
    }

    @Test
    void getProduct_shouldThrowProductNotFoundExceptionExceptionIfProductDoesNotExist() {
        // given
        given(productRepository.findBySkuAndIsActive(SKU_1, true)).willReturn(Optional.empty());

        // when
        ProductNotFoundException exception = catchThrowableOfType(() ->
                        productService.getProduct(SKU_1),
                ProductNotFoundException.class
        );

        // then
        assertThat(exception.getMessage()).isEqualTo("Product SKU1 not found");
    }

    @Test
    void findProduct_shouldReturnProduct() {
        //given
        given(productRepository.findBySkuAndIsActive(SKU_1, true)).willReturn(Optional.of(product1));

        // when
        Product res = productService.findProduct(SKU_1);

        // then
        assertThat(res.getName()).isEqualTo(PROD_1);
        assertThat(res.getPrice()).isEqualTo(200L);
        assertThat(res.getSku()).isEqualTo(SKU_1);
    }

    @Test
    void findProduct_shouldThrowProductNotFoundExceptionExceptionIfProductDoesNotExist() {
        // given
        given(productRepository.findBySkuAndIsActive(SKU_1, true)).willReturn(Optional.empty());

        // when
        ProductNotFoundException exception = catchThrowableOfType(() ->
                        productService.findProduct(SKU_1),
                ProductNotFoundException.class
        );

        // then
        assertThat(exception.getMessage()).isEqualTo("Product SKU1 not found");
    }

    @Test
    void deleteProduct_shouldReturnProduct() {
        //given
        given(productRepository.findBySkuAndIsActive(SKU_1, true)).willReturn(Optional.of(product1));

        // when
        productService.deleteProduct(SKU_1);

        // then
        // no exception thrown
    }

    @Test
    void deleteProduct_shouldThrowProductNotFoundExceptionExceptionIfProductDoesNotExist() {
        // given
        given(productRepository.findBySkuAndIsActive(SKU_1, true)).willReturn(Optional.empty());

        // when
        ProductNotFoundException exception = catchThrowableOfType(() ->
                        productService.deleteProduct(SKU_1),
                ProductNotFoundException.class
        );

        // then
        assertThat(exception.getMessage()).isEqualTo("Product SKU1 not found");
    }
}
