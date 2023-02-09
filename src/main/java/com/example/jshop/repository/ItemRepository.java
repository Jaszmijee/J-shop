package com.example.jshop.repository;


import com.example.jshop.domain.cart.Item;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ItemRepository extends CrudRepository<Item, Long> {

    @Override
    Item save(Item item);
}
