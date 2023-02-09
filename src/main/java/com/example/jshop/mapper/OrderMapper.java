package com.example.jshop.mapper;

import com.example.jshop.domain.Order;
import com.example.jshop.domain.order.OrderDtoToCustomer;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {

    public OrderDtoToCustomer mapToOrderDtoToCustomer(Order order) {
        return new OrderDtoToCustomer(
                order.getOrderID(),
                order.getCreated(),
                order.getListOfProducts(),
                order.getCalculatedPrice().toString(),
                order.getOrder_status().toString(),
                order.getCreated().plusDays(14)
        );
    }
}
