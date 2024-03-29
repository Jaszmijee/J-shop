package com.example.jshop.warehouseandproducts.repository;

import java.util.List;
import java.util.Optional;
import com.example.jshop.warehouseandproducts.domain.product.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Override
    Product save(Product product);

    Product findByProductName(String name);

    @Override
    Optional<Product> findById(Long productId);

    Optional<Product> findByProductID(Long productId);

    void deleteByProductID(Long productId);

    @Override
    List<Product> findAll();
}
