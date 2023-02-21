package com.example.jshop.customer.service;

import com.example.jshop.customer.domain.CustomerDto;
import com.example.jshop.customer.domain.Customer_Logged;
import com.example.jshop.customer.domain.LoggedCustomerDto;
import com.example.jshop.carts_and_orders.domain.order.OrderDtoToCustomer;
import com.example.jshop.error_handlers.exceptions.AccessDeniedException;
import com.example.jshop.error_handlers.exceptions.UserNotFoundException;
import com.example.jshop.customer.mapper.CustomerMapper;
import com.example.jshop.carts_and_orders.mapper.OrderMapper;
import com.example.jshop.customer.repository.CustomerRepository;
import com.example.jshop.carts_and_orders.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    public Customer_Logged updateCustomer(Customer_Logged customer) {
        return customerRepository.save(customer);
    }

    public Customer_Logged createNewCustomer(CustomerDto customerDto) {
        Customer_Logged customer = customerMapper.mapToCustomer(customerDto);
        customer.setPassword(bCryptPasswordEncoder.encode(customerDto.getPassword()).toCharArray());
        return customerRepository.save(customer);
    }

    private Customer_Logged findCustomerByName(String userName) throws UserNotFoundException {
        return customerRepository.findByUserName(userName).orElseThrow(UserNotFoundException::new);
    }

    public Customer_Logged verifyLogin(String userName, char[] pwwd) throws UserNotFoundException, AccessDeniedException {
        Customer_Logged customer_logged = findCustomerByName(userName);
        StringBuffer request = new StringBuffer();
        for (char ch : pwwd) {
            request.append(ch);
        }
        StringBuffer response = new StringBuffer();
        for (char ch : customer_logged.getPassword()) {
            response.append(ch);
        }
        if (!bCryptPasswordEncoder.matches(request.toString(), response.toString())){
            LOGGER.error("Unauthorized access attempt for " + userName);
            throw new AccessDeniedException();
        }
        else return customer_logged;
    }

    public void removeCustomer(LoggedCustomerDto customerDto) throws UserNotFoundException, AccessDeniedException {
        verifyLogin(customerDto.getUsername(), customerDto.getPassword());
        customerRepository.deleteById(findCustomerByName(customerDto.getUsername()).getCustomerID());
    }

    public void deleteUnauthenticatedCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    public List<OrderDtoToCustomer> showMyOrders(LoggedCustomerDto customerDto) throws UserNotFoundException, AccessDeniedException {
        verifyLogin(customerDto.getUsername(), customerDto.getPassword());
        return orderMapper.mapToOrderDtoToCustomerList(orderService.findOrdersOfCustomer(customerDto.getUsername()));
    }


}
