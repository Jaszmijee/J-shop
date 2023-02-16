package com.example.jshop.repository;

import com.example.jshop.domain.cart.Cart;
import com.example.jshop.domain.cart.CartStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CartRepository extends CrudRepository<Cart, Long> {

    @Override
    Cart save(Cart cart);

    void deleteByCartStatus(CartStatus cartStatus);

@Query(value = "SELECT * from carts " +
        "where status = 'PROCESSING' " +
        "AND DATEDIFF(CURDATE(), created) = 3",
    nativeQuery = true)
    List<Cart> selectByProcessingTime();
}
