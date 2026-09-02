package com.example.ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.entity.Product;


public interface ProductService {
    Product addproduct( ProductRequest request);

    Page<Product> getAllProducts(
        int page,
        int size
    );

    void deleteProduct(Long id);

    Product updateProduct(Long id,
        ProductRequest request
    );

    Product getProduct(Long id);

    List<Product> getProductByCategoryId(Long categoryId);

}
