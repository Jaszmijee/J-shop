package com.example.jshop.service;

import com.example.jshop.domain.cart.Item;
import com.example.jshop.domain.customer.Customer_Logged;
import com.example.jshop.domain.customer.LoggedCustomerDto;
import com.example.jshop.domain.order.ORDER_STATUS;
import com.example.jshop.domain.order.Order;
import com.example.jshop.domain.warehouse.Warehouse;
import com.example.jshop.exception.AccessDeniedException;
import com.example.jshop.exception.OrderNotFoundException;
import com.example.jshop.exception.UserNotFoundException;
import com.example.jshop.mapper.OrderMapper;
import com.example.jshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;


    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Order findOrderById(Long orderId) throws OrderNotFoundException {
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

    public Order findByIdAndUserName(Long orderId, String username) throws OrderNotFoundException {
        return orderRepository.findByOrderIDAndAndCustomer_UserName(orderId, username).orElseThrow(OrderNotFoundException::new);
    }


    public List<Order> findOrders(String order_status) {
        return orderRepository.findOrders(order_status);
    }

    public List<Order> findOrdersOfCustomer(String userName) {
        return orderRepository.findByCustomer_UserName(userName);
    }

    public void deleteOrder(Order order) {
        orderRepository.deleteById(order.getOrderID());
    }

    public List<Order> findCloseUnpaidOrders() {
        return orderRepository.findOrdersCloseTOPayment();
    }

    public List<Order> findUnpaidOrders() throws UserNotFoundException, OrderNotFoundException, AccessDeniedException {
       return orderRepository.findUnpaidOrders();

    }
}


