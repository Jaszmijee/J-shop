package com.example.jshop.service;

import com.example.jshop.domain.customer.Customer;
import com.example.jshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public Customer createNewCustomer() {
        return null;
    }

}
