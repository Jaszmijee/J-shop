package com.example.jshop.camunda.serviceTasks;

import com.example.jshop.cartsandorders.domain.cart.CartItemsDto;
import com.example.jshop.cartsandorders.service.CartService;
import com.example.jshop.customer.domain.AuthenticationDataDto;
import com.example.jshop.customer.domain.UnauthenticatedCustomerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingDelegate {

    private final CartService cartService;

    public void executeAddToCart(DelegateExecution execution) throws Exception {
        log.info("AddToCart execution started");

        Long cartId = Long.valueOf(execution.getProcessBusinessKey());
        Long productId = (Long) execution.getVariable("productId");
        int quantity = (Integer) execution.getVariable("quantity");

        CartItemsDto cartItemsDto = new CartItemsDto(productId, quantity);
        cartService.addToCartCamunda(cartId, cartItemsDto);
    }

    public void executeRemoveFromCart(DelegateExecution execution) throws Exception {
        log.info("RemoveFromCart execution started" + execution.getVariable("activity"));

        Long cartId = Long.valueOf(execution.getProcessBusinessKey());
        Long productId = (Long) execution.getVariable("productId");
        int quantity = (Integer) execution.getVariable("quantity");

        CartItemsDto cartItemsDto = new CartItemsDto(productId, quantity);
        cartService.removeFromCartCamunda(cartId, cartItemsDto);
    }

    public void executeFinalizeAndPayUnauthenticated(DelegateExecution execution) throws Exception {
        log.info("FinalizeAndPayUnauthenticated execution started");

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

        var data = new UnauthenticatedCustomerDto(firstName, lastName, email, street, houseNo, flatNo, zipCode, city, country);
        cartService.payForCartUnauthenticatedCustomerCamunda(cartId, data);
    }

    public void executeFinalizeCartAuthenticated(DelegateExecution execution) throws Exception {
        log.info("FinalizeCartAuthenticated execution started");

        Long cartId = Long.valueOf(execution.getProcessBusinessKey());
        String user = (String) execution.getVariable("userName");
        char[] pwwd = (char[]) execution.getVariable("password");

        AuthenticationDataDto data = new AuthenticationDataDto(user, pwwd);
        cartService.finalizeCartAuthenticatedCamunda(cartId, data);
    }

    public void executePayForOrderAuthenticated(DelegateExecution execution) throws Exception {
        log.info("PayForOrderAuthenticated execution started");

        Long orderId = (Long) execution.getVariable("orderId");

        cartService.payForOrderCamunda(orderId);
    }

    public void executeCancelOrder(DelegateExecution execution) throws Exception {
        log.info("CancelOrder execution started");

        Long orderId = (Long) execution.getVariable("orderId");

        cartService.cancelOrder(orderId);
    }

    public void executeRemindAboutPayment(DelegateExecution execution) {
        log.info("RemindAboutPayment execution started");

        String processId = execution.getProcessInstanceId();

        cartService.remindAboutPaymentCamunda(processId);
    }

    public void executeRemoveNotFinalizedCarts(DelegateExecution execution) {
        log.info("RemoveNotFinalizedCartsDelegate execution started");

        String processId = execution.getProcessInstanceId();

        cartService.removeNotFinalizedCartsCamunda(processId);
    }

}
