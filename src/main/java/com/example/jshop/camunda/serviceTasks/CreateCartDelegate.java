package com.example.jshop.camunda.serviceTasks;

import com.example.jshop.cartsandorders.service.CartService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateCartDelegate implements JavaDelegate {

    @Autowired
    CartService cartService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long cartId = cartService.createCart().getCartID();
        execution.setVariable("cartId", cartId);
    }
}
