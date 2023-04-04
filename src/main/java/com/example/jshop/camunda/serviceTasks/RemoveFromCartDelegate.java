package com.example.jshop.camunda.serviceTasks;

import com.example.jshop.cartsandorders.domain.cart.CartItemsDto;
import com.example.jshop.cartsandorders.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RemoveFromCartDelegate implements JavaDelegate {
    private final CartService cartService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("RemoveFromCartDelegate started" + execution.getVariable("activity"));

        Long cartId = Long.valueOf(execution.getProcessBusinessKey());
        Long productId = (Long) execution.getVariable("productId");
        int quantity = (Integer) execution.getVariable("quantity");

        CartItemsDto cartItemsDto = new CartItemsDto(productId, quantity);
        cartService.removeFromCartCamunda(cartId, cartItemsDto);
    }
}
