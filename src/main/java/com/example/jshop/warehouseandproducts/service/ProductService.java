package com.example.jshop.warehouseandproducts.service;

import com.example.jshop.warehouseandproducts.domain.product.Product;
import com.example.jshop.errorhandlers.exceptions.ProductNotFoundException;
import com.example.jshop.warehouseandproducts.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product findProductById(Long productId) throws ProductNotFoundException {
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }

    public void deleteProductById(Long productId) {
        productRepository.deleteByProductID(productId);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
