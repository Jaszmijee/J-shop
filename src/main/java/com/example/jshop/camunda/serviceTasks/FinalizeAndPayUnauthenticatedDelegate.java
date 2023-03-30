package com.example.jshop.camunda.serviceTasks;

import com.example.jshop.cartsandorders.service.CartService;
import com.example.jshop.customer.domain.UnauthenticatedCustomerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinalizeAndPayUnauthenticatedDelegate implements JavaDelegate {

    private final CartService cartService;

    public void execute(DelegateExecution execution) throws Exception {
        log.info("FinalizeAndPayUnauthenticatedDelegate started");

        Long cartId = Long.valueOf(execution.getProcessBusinessKey());
        String firstName = (String) execution.getVariable("firstName");
        String lastName = (String) execution.getVariable("lastName");
        String email = (String) execution.getVariable("email");
        String street = (String) execution.getVariable("street");
        String houseNo = (String) execution.getVariable("houseNo");
        String flatNo = (String) execution.getVariable("flatNo");
        String zipCode = (String) execution.getVariable("zipCode");
        String city = (String) execution.getVariable("city");
        String country = (String) execution.getVariable("country");

        UnauthenticatedCustomerDto data = new UnauthenticatedCustomerDto(firstName,lastName,email,street,houseNo,flatNo,zipCode,city,country);
        cartService.payForCartUnauthenticatedCustomerCamunda(cartId, data);
    }
}
