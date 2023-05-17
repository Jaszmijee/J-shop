package com.example.jshop.cartsandorders.repository;

import com.example.jshop.cartsandorders.domain.cart.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ItemRepository extends CrudRepository<Item, Long> {

    @Override
    Item save(Item item);
}
