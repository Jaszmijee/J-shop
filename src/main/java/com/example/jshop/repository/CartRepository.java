package com.example.jshop.repository;

import com.example.jshop.domain.cart.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface CartRepository extends CrudRepository<Cart, Long> {

    @Override
    Cart save(Cart cart);

    @Override
    Optional<Cart> findById(Long aLong);


}

