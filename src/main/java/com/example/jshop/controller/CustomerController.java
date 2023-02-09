package com.example.jshop.controller;

import com.example.jshop.domain.customer.CustomerDto;
import com.example.jshop.mapper.CustomerMapper;
import com.example.jshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;
   @PostMapping
    ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        customerService.createNewCustomer(customerDto);
        return ResponseEntity.ok().build();
    }

 /*   @DeleteMapping
    ResponseEntity<Void> removeCustomer(@RequestParam String username, @RequestParam String password){
        customerervice.removeCustomer(username,password);
        return ResponseEntity.ok().build();
    }*/
}
