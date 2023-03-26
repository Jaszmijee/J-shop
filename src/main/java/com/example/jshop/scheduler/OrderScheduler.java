package com.example.jshop.scheduler;

import com.example.jshop.cartsandorders.domain.order.Order;
import com.example.jshop.email.service.EmailContentCreator;
import com.example.jshop.errorhandlers.exceptions.OrderNotFoundException;
import com.example.jshop.cartsandorders.service.CartService;
import com.example.jshop.cartsandorders.service.OrderService;
import com.example.jshop.email.service.SimpleEmailService;
import com.example.jshop.errorhandlers.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class OrderScheduler {

    @Autowired
    OrderService orderService;
    @Autowired
    SimpleEmailService emailService;
    @Autowired
    CartService cartService;
    @Autowired
    EmailContentCreator contentCreator;

    @Scheduled(cron = "0 0 0 * * ?")
    public void remindAboutPayment() {
        List<Order> unpaidOrders = orderService.findCloseUnpaidOrders();
        for (Order order : unpaidOrders) {
            emailService.send(contentCreator.createContentReminder(order));
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void removeUnpaidOrders() throws OrderNotFoundException, ProductNotFoundException {
        List<Order> unpaidOrders = orderService.findUnpaidOrders();
        for (Order orderToCancel : unpaidOrders) {
              cartService.cancelOrder(orderToCancel.getOrderID());
        }
    }
}

