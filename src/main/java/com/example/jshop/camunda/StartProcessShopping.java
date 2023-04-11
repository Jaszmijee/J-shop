package com.example.jshop.camunda;

import com.example.jshop.cartsandorders.service.CartService;
import com.example.jshop.errorhandlers.exceptions.CartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
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
        variables.put("cartValue", 0L);

        ProcessInstance processInstance = processEngine.getRuntimeService().
                createProcessInstanceByKey("Shopping").businessKey(businessKey).setVariables(variables)
                .execute();

    //    processEngine.getDecisionService().evaluateDecisionByKey("Decision_0jgczgt").decisionDefinitionWithoutTenantId().evaluate();

        String processInstanceId = processInstance.getProcessInstanceId();

        cartService.setUpProcessInstance(cartId, processInstanceId);
    }

}