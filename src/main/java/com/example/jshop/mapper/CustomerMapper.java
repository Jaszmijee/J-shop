package com.example.jshop.mapper;

//import com.example.jshop.domain.customer.Customer;
//import com.example.jshop.domain.customer.CustomerDto;
//import com.example.jshop.domain.customer.Customer_Logged;
//import com.example.jshop.domain.customer.Customer_UnLogged;

import com.example.jshop.domain.customer.Address;
import com.example.jshop.domain.customer.CustomerDto;
import com.example.jshop.domain.customer.Customer_Logged;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {


  /*  public CustomerDto mapToCustomerDto(Customer customer){

        return new CustomerDto(
                customer.

        )

    }

   */
    public Customer_Logged mapToCustomer(CustomerDto customerDto){
       return new Customer_Logged(
               customerDto.getUserName(),
               customerDto.getPassword().toCharArray(),
               customerDto.getFirstName(),
               customerDto.getLastName(),
               customerDto.getEmail(),
               new Address(
                       customerDto.getStreet(),
                       customerDto.getHouseNo(),
                       customerDto.getFlatNo(),
                       customerDto.getZipCode(),
                       customerDto.getCity(),
                       customerDto.getCountry()
               )
       );
    }
}
