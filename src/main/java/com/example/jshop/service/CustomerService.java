package com.example.jshop.service;

import com.example.jshop.domain.customer.CustomerDto;
import com.example.jshop.domain.customer.Customer_Logged;
import com.example.jshop.domain.customer.LoggedCustomerDto;
import com.example.jshop.exception.AccessDeniedException;
import com.example.jshop.exception.UserNotFoundException;
import com.example.jshop.mapper.CustomerMapper;
import com.example.jshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    public Customer_Logged saveCustomer(Customer_Logged customer) {
        return customerRepository.save(customer);
    }

 public Customer_Logged createNewCustomer(CustomerDto customerDto) {
     Customer_Logged customer = customerMapper.mapToCustomer(customerDto);
     return customerRepository.save(customer);
            }

    private Customer_Logged findCustomerByName(String userName) throws UserNotFoundException {
        return customerRepository.findByUserName(userName).orElseThrow(UserNotFoundException::new);
    }

    private boolean verifyPassword(String name, char[] pwwd) throws UserNotFoundException {

        return (Arrays.equals(findCustomerByName(name).getPassword(),pwwd));
    }

    public Customer_Logged verifyLogin(String name, char[] pwwd) throws UserNotFoundException, AccessDeniedException {
        if (!(verifyPassword(name, pwwd))) {
            throw new AccessDeniedException();
        }
        return findCustomerByName(name);

    }
}
