package com.example.jshop.service;

import com.example.jshop.domain.customer.CustomerDto;
import com.example.jshop.domain.customer.Customer_Logged;
import com.example.jshop.domain.customer.LoggedCustomerDto;
import com.example.jshop.domain.order.OrderDtoToCustomer;
import com.example.jshop.exception.AccessDeniedException;
import com.example.jshop.exception.UserNotFoundException;
import com.example.jshop.mapper.CustomerMapper;
import com.example.jshop.mapper.OrderMapper;
import com.example.jshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class CustomerService {

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

    public Customer_Logged saveCustomer(Customer_Logged customer) {
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

    private boolean verifyPassword(String userName, char[] pwwd) throws UserNotFoundException {
        StringBuffer request = new StringBuffer();
        for (char ch : pwwd){
            request.append(ch);
        }
        StringBuffer response = new StringBuffer();
        for (char ch : findCustomerByName(userName).getPassword()){
            response.append(ch);
        }

        return bCryptPasswordEncoder.matches(request.toString(),response.toString());

    }

    public Customer_Logged verifyLogin(String name, char[] pwwd) throws UserNotFoundException, AccessDeniedException {
        if (!(verifyPassword(name, pwwd))) {
            throw new AccessDeniedException();
        }
        return findCustomerByName(name);
    }

    public void removeCustomer(LoggedCustomerDto customerDto) throws UserNotFoundException, AccessDeniedException {
        verifyLogin(customerDto.getUsername(), customerDto.getPassword());
        customerRepository.deleteById(findCustomerByName(customerDto.getUsername()).getCustomerID());
    }

    public void deleteUnloggedCustomer(Long customerId) {
    customerRepository.deleteById(customerId);
    }


    public List<OrderDtoToCustomer> showMyOrders(LoggedCustomerDto customerDto) throws UserNotFoundException, AccessDeniedException {
        verifyLogin(customerDto.getUsername(), customerDto.getPassword());
        return orderMapper.mapToOrderDtoToCustomerList(orderService.findOrdersOfCustomer(customerDto.getUsername()));
    }


}
