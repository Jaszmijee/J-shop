package com.example.jshop.cartsandorders.mapper;

import java.math.BigDecimal;
import com.example.jshop.cartsandorders.domain.cart.Item;
import com.example.jshop.cartsandorders.domain.cart.ItemDto;
import org.springframework.stereotype.Service;

@Service
public class ItemMapper {

    public ItemDto mapToItemDto(Item item) {
        return new ItemDto(
            item.getProduct().getProductID(),
            item.getProduct().getProductName(),
            item.getQuantity(),
            item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
