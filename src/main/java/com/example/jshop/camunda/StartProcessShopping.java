package com.example.jshop.camunda;

import com.example.jshop.cartsandorders.service.CartService;
import com.example.jshop.errorhandlers.exceptions.CartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StartProcessShopping {

    private final CartService cartService;
    private final ProcessEngine processEngine;

    public void createProcessInstance(Long cartId) throws CartNotFoundException {

        String businessKey = String.valueOf(cartId);

        Map<String, Object> variables = new HashMap<>();
        variables.put("cartId", cartId);

        ProcessInstance processInstance = processEngine.getRuntimeService().
                createProcessInstanceByKey("Shopping").businessKey(businessKey).setVariablesLocal(variables)
                .execute();

        String processInstanceId = processInstance.getProcessInstanceId();

        cartService.setUpProcessInstance(cartId, processInstanceId);
    }
}
