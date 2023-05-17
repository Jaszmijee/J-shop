package com.example.jshop.cartsandorders.mapper;

import java.util.List;
import com.example.jshop.cartsandorders.domain.order.Order;
import com.example.jshop.cartsandorders.domain.order.OrderDtoToCustomer;
import com.example.jshop.cartsandorders.domain.order.OrderIdDTO;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {

    public OrderDtoToCustomer mapToOrderDtoToCustomer(Order order) {
        return new OrderDtoToCustomer(
            order.getOrderID(),
            order.getCreated(),
            order.getListOfProducts(),
            order.getCalculatedPrice().toString(),
            order.getOrderStatus().toString(),
            order.getPaymentDue()
        );
    }

    public List<OrderDtoToCustomer> mapToOrderDtoToCustomerList(List<Order> listOfOrders) {
        return listOfOrders.stream().map(order -> mapToOrderDtoToCustomer(order)).toList();
    }

    public OrderIdDTO mapToOrderId(Order order) {
        return new OrderIdDTO(order.getOrderID());
    }
}
