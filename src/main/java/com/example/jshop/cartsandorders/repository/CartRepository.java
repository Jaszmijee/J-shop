package com.example.jshop.cartsandorders.repository;

import com.example.jshop.cartsandorders.domain.cart.Cart;
import com.example.jshop.cartsandorders.domain.cart.CartStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CartRepository extends CrudRepository<Cart, Long> {

    @Override
    Cart save(Cart cart);

    void deleteByCartStatus(CartStatus cartStatus);

    @Query(value = "SELECT * from carts " +
            "where status = 'PROCESSING' ",
        //    "AND DATEDIFF(CURDATE(), created) >= 3",
            nativeQuery = true)
    List<Cart> selectByProcessingTime();

   @Override
   List<Cart> findAll();
}
