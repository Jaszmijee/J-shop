package com.example.jshop.scheduler;

import com.example.jshop.email.domain.Mail;
import com.example.jshop.carts_and_orders.domain.order.Order;
import com.example.jshop.error_handlers.exceptions.AccessDeniedException;
import com.example.jshop.error_handlers.exceptions.OrderNotFoundException;
import com.example.jshop.error_handlers.exceptions.UserNotFoundException;
import com.example.jshop.carts_and_orders.service.CartService;
import com.example.jshop.carts_and_orders.service.OrderService;
import com.example.jshop.email.service.SimpleEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderScheduler {

    @Autowired
    OrderService orderService;

    @Autowired
    SimpleEmailService emailService;

    @Autowired
    CartService cartService;


    private Mail createContent(Order order) {

        String subject = "Payment reminder for order: " + order.getOrderID();
        String message = "Your order: " + order.getOrderID() + ", created on: " + order.getCreated()
                + "\n total sum: " + order.getCalculatedPrice() +
                "\n Your payment is due tomorrow. " +
                "\n Please proceed with payment, otherwise, your order will be cancelled " +
                "\n Thank you for your purchase" +
                "\n Your J-Shop";

        return new Mail(
                order.getCustomer().getEmail(),
                subject,
                message,
                "admin@j-shop.com"
        );
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void remindAboutPayment() {
        List<Order> unpaidOrders = orderService.findCloseUnpaidOrders();
        for (Order order : unpaidOrders) {
            emailService.send(createContent(order));
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void removeUnpaidOrders() throws UserNotFoundException, OrderNotFoundException, AccessDeniedException {
        List<Order> unpaidOrders = orderService.findUnpaidOrders();
        for (Order orderToCancel : unpaidOrders) {
              cartService.cancelOrder(orderToCancel.getOrderID());
        }
    }
}

