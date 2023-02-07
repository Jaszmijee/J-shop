package com.example.jshop.service;

import com.example.jshop.domain.cart.Cart;
import com.example.jshop.domain.cart.CartItemsDto;
import com.example.jshop.domain.cart.CartStatus;
import com.example.jshop.domain.cart.Item;
import com.example.jshop.domain.product.Product;
import com.example.jshop.domain.warehouse.Warehouse;
import com.example.jshop.exception.CartNotFoundException;
import com.example.jshop.exception.ItemNotAvailableException;
import com.example.jshop.exception.ItemNotFoundEXception;
import com.example.jshop.exception.NotEnoughItemsException;
import com.example.jshop.repository.CartRepository;
import com.example.jshop.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    WarehouseService warehouseService;

    private Cart findCartById(Long cartId) throws CartNotFoundException {
        return cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
    }


    public Cart createCart() {
        Cart newCart = new Cart();
        newCart.setCartStatus(CartStatus.EMPTY);
        cartRepository.save(newCart);
        return newCart;
    }

    private void findIfQuantityInWarehouseIsEnough(Warehouse warehouse, int quantity) throws ItemNotAvailableException, NotEnoughItemsException {
        if (warehouse.getProductQuantity() == 0) {
            throw new ItemNotAvailableException();
        }
        if (warehouse.getProductQuantity() < quantity) {
            throw new NotEnoughItemsException();
        }
    }

    private void updateProductInWarehouse(Warehouse warehouse, Integer productQuantity) {
        warehouse.setProductQuantity(warehouse.getProductQuantity() - productQuantity);
        warehouseService.save(warehouse);
    }

    private BigDecimal calculateCurrentCartValue(Cart cart) {
        if (cart.getCartStatus() == CartStatus.PROCESSING) {
            return new BigDecimal(String.valueOf(cart.getListOfItems().stream()
                    .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                    .reduce(BigDecimal.ZERO, (sum, item) -> sum = sum.add(item))));
        } else return BigDecimal.ZERO;
    }

    private void validateCartForProcessing(Cart cart) throws CartNotFoundException {
        if (cart.getCartStatus() == CartStatus.PAID) {
            throw new CartNotFoundException();
        }
    }

/*    public Cart addToCart(Long cartId, CartItemsDto cartItemsDto) throws CartNotFoundException, ItemNotAvailableException, NotEnoughItemsException {
        Cart cartToUpdate = findCartById(cartId);
        validateCartForProcessing(cartToUpdate);
        Warehouse warehouse = warehouseService.findItemByID(cartItemsDto.getProductId());
        findIfQuantityInWarehouseIsEnough(warehouse, cartItemsDto.getQuantity());
        Product product = warehouse.getProduct();
        Item item = new Item(product, cartItemsDto.getQuantity());
        cartToUpdate.getListOfItems().add(item);
        cartToUpdate.setCalculatedPrice(calculateCurrentCartValue(cartToUpdate));
        cartToUpdate.setCartStatus(CartStatus.PROCESSING);
        updateProductInWarehouse(warehouse, cartItemsDto.getQuantity());
        cartRepository.save(cartToUpdate);
        return cartToUpdate;*/


   /* public Cart removeFromCart(Long cartId, CartItemsDto cartItemsDto) throws CartNotFoundException {
        Cart cartToUpdate = findCartById(cartId);
        validateCartForProcessing(cartToUpdate);
        Warehouse warehouse = warehouseService.findItemByID(cartItemsDto.getProductId());
        Product product = warehouse.getProduct();
        updateProductInWarehouse(warehouse, -(cartItemsDto.getQuantity()));


        cartToUpdate.getListOfItems().remove(item);
        cartToUpdate.getListOfProducts().computeIfPresent(product, (key, quantity) -> quantity - cartItemsDto.getQuantity());
        if (cartToUpdate.getListOfProducts().get(product) <= 0) {
            cartToUpdate.getListOfProducts().remove(product);
        }
        if (cartToUpdate.getListOfProducts().isEmpty()) {
            cartToUpdate.setCartStatus(CartStatus.EMPTY);
        }
        cartToUpdate.setCalculatedPrice(calculateCurrentCartValue(cartToUpdate));
        cartRepository.save(cartToUpdate);
        return cartToUpdate;
    }

    public void removeCart(Long cartId) throws CartNotFoundException {
        Cart cart = findCartById(cartId);
        validateCartForProcessing(cart);
        if (!(cart.getListOfProducts().isEmpty())) {
            for (Map.Entry<Product, Integer> items : cart.getListOfProducts().entrySet()) {
                Warehouse warehouse = warehouseService.findItemByID(items.getKey().getProductID());
                warehouse.setProductQuantity(warehouse.getProductQuantity() + items.getValue());
                warehouseService.save(warehouse);
            }
            cartRepository.deleteById(cartId);
        }
    }*/

    public void finalizeCart(Long cartId) {
    }
}

