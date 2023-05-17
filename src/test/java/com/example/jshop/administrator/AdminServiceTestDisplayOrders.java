package com.example.jshop.administrator;

import com.example.jshop.cartsandorders.mapper.OrderMapper;
import com.example.jshop.cartsandorders.service.OrderService;
import com.example.jshop.errorhandlers.exceptions.InvalidOrderStatusException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTestDisplayOrders {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderMapper orderMapper;

    @Test
    void testDisplayOrdersInvalidOrderStatusException() throws InvalidOrderStatusException {
        //Given
        when(orderService.findOrders(anyString())).thenThrow(InvalidOrderStatusException.class);

        //When & Then
        assertThrows(InvalidOrderStatusException.class, () -> adminService.displayOrders("dummy"));

        verify(orderService, times(1)).findOrders(anyString());
        verify(orderMapper, times(0)).mapToOrderDtoToCustomerList(anyList());
    }

    /*@Test
    void testDisplayOrdersPositiveWithOptionalStatus() throws InvalidOrderStatusException, OrderNotFoundException {
        //Given
        char[] pwwd = "password".toCharArray();
        LoggedCustomer loggedCustomer =  new LoggedCustomer("user", pwwd, "Adam", "DDD", "ptr@ptr",
                new Address("Fairy", "5", "5", "55-555", "Maputo", "Mosambique"));
        Cart cart =  new Cart(1L, CartStatus.FINALIZED, List.of(), new BigDecimal("1000.00"), LocalDate.of(2023, 2, 20), "dummy Camunda process");
        Order order = new Order(loggedCustomer, LocalDate.of(2023, 2, 22), OrderStatus.UNPAID, "list of products unpaid", new BigDecimal("1000.00"), cart, "dummy Camunda process");
        List<Order> unpaid = List.of(order);

        LoggedCustomer loggedCustomer1 =  new LoggedCustomer("user1", pwwd, "Adam1", "DDD1", "ptr@ptr1",
                new Address("Fairy1", "6", "5", "66-666", "Maputo", "Mosambique"));
        Cart cart1 =  new Cart(2L, CartStatus.FINALIZED, List.of(), new BigDecimal("1500.00"), LocalDate.of(2023, 2, 20), "dummy Camunda process");
        Order order1 = new Order(loggedCustomer1, LocalDate.of(2023, 2, 21), OrderStatus.PAID,  "list of products paid", new BigDecimal("1500.00"), cart1, "dummy Camunda process1");
        List<Order> paid = List.of(order1);
        List<Order> combined = new ArrayList<>();
        combined.addAll(paid);
        combined.addAll(unpaid);

        when(orderService.findOrders("unpaid")).thenReturn(unpaid);
        when(orderService.findOrders(null)).thenReturn(combined);

        List<OrderDtoToCustomer> unpaidOrders = List.of(new OrderDtoToCustomer(1L, LocalDate.of(2023, 2, 22), "list of products unpaid", "1000", "UNPAID", LocalDate.of(2023, 3, 20)));
        List<OrderDtoToCustomer> paidOrders = List.of(new OrderDtoToCustomer(2L, LocalDate.of(2023, 2, 21), "list of products paid", "1500", "PAID", LocalDate.of(2023, 3, 19)));
        List<OrderDtoToCustomer> combinedOrders = new ArrayList<>();
        combinedOrders.addAll(paidOrders);
        combinedOrders.addAll(unpaidOrders);

        when(orderMapper.mapToOrderDtoToCustomerList(unpaid)).thenReturn(unpaidOrders);
        when(orderMapper.mapToOrderDtoToCustomerList(combined)).thenReturn(combinedOrders);

        //When & Then
        assertEquals(1, adminService.displayOrders("unpaid").size());
        assertEquals(2, adminService.displayOrders(null).size());

        verify(orderService, times(1)).findOrders(anyString());
        verify(orderService, times(1)).findOrders(null);
        verify(orderMapper, times(2)).mapToOrderDtoToCustomerList(anyList());
    }*/
}
