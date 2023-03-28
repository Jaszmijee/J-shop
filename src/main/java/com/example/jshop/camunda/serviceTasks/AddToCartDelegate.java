package com.example.jshop.camunda.serviceTasks;

import com.example.jshop.cartsandorders.domain.cart.CartItemsDto;
import com.example.jshop.cartsandorders.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddToCartDelegate {

    @Autowired
    CartService cartService;

    @Autowired
    RuntimeService runtimeService;

   public void executeAddTCart(DelegateExecution execution) throws Exception {
        log.info("AddToCartDelegate started");
        CartItemsDto cartItemsDto = new CartItemsDto((Long) execution.getVariable("productId"), (Integer) execution.getVariable("quantity"));
        cartService.addToCart((Long) runtimeService.getVariable(execution.getProcessInstanceId(), "cartId"), cartItemsDto);
    }
}
