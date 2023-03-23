package com.example.jshop.carts_and_orders.repository;

import com.example.jshop.carts_and_orders.domain.cart.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ItemRepository extends CrudRepository<Item, Long> {
    @Override
    Item save(Item item);
}
