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
public class CancelOrderDelegate implements JavaDelegate {

    private final CartService cartService;

    public void execute(DelegateExecution execution) throws Exception {
        log.info("CancelOrderDelegate started");

        Long orderId = (Long) execution.getVariable("orderId");

        cartService.cancelOrder(orderId);
    }
}
