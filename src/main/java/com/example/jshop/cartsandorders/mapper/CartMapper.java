package com.example.jshop.cartsandorders.mapper;

import com.example.jshop.cartsandorders.domain.cart.Cart;
import com.example.jshop.cartsandorders.domain.cart.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartMapper {


    @Autowired
    ItemMapper itemMapper;
    public CartDto mapCartToCartDto(Cart cart){
        return new CartDto(
                cart.getCartID(),
                cart.getListOfItems().stream().map(items -> itemMapper.mapToItemDto(items)).toList(),
                cart.getCartStatus(),
                cart.getCalculatedPrice()
        );
    }
}
