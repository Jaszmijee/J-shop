package com.example.jshop.camunda;

import java.util.HashMap;
import java.util.Map;
import com.example.jshop.cartsandorders.service.CartService;
import com.example.jshop.errorhandlers.exceptions.CartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

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

        String processInstanceId = processInstance.getProcessInstanceId();

        cartService.setUpProcessInstance(cartId, processInstanceId);
    }

}
