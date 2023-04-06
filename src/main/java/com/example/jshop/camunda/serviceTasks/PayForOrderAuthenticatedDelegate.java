package com.example.jshop.camunda.serviceTasks;

import com.example.jshop.cartsandorders.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayForOrderAuthenticatedDelegate implements JavaDelegate {

    private final CartService cartService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("PayForOrderAuthenticatedDelegate started");

        Long orderId = (Long) execution.getVariable("orderId");

        cartService.payForOrderCamunda(orderId);
    }
}
