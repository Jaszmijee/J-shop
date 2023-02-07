package com.example.jshop.repository;

import com.example.jshop.domain.customer.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CustomerRepository extends CrudRepository<Customer, Long> {


}
