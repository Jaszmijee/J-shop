package com.example.jshop.mapper;

import com.example.jshop.domain.cart.Item;
import com.example.jshop.domain.cart.ItemDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ItemMapper {

    public ItemDto mapToItemDto(Item item){
        return new ItemDto(
                item.getProduct().getProductID(),
                item.getProduct().getProductName(),
                item.getQuantity(),
                item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
