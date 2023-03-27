package com.example.jshop.camunda;

import com.example.jshop.camunda.domain.CartCamundaDto;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartProcessShopping {

    private final ProcessEngine processEngine;

    public CartCamundaDto createProcessInstance(Long cartId) {

        String businessKey = String.valueOf(cartId);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("Shopping", businessKey);
        String processInstanceId = processInstance.getProcessInstanceId();
        return new CartCamundaDto(cartId,processInstanceId);
    }

/*      Map<String, Object> variables = newHashMap();
        variables.put("cartId", );
        variables.put("productId", );*/

}
