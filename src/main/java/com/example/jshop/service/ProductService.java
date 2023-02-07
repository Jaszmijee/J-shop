package com.example.jshop.service;

import com.example.jshop.domain.product.Product;
import com.example.jshop.exception.ProductNotFoundException;
import com.example.jshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product findProductById(Long productId) throws ProductNotFoundException {
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }

    public void deleteById(Long productId) {
        productRepository.deleteByProductID(productId);
    }

    public List<Product> findAllProducts() {
       return productRepository.findAll();
    }
}
