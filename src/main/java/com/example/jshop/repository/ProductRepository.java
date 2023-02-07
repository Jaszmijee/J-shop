package com.example.jshop.repository;

import com.example.jshop.domain.product.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ProductRepository extends CrudRepository<Product, Long> {

    Product save(Product product);

    @Override
    Optional<Product> findById(Long productId);

    void deleteByProductID(Long productId);

    @Override
    List<Product> findAll();
}
