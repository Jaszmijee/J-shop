package com.example.jshop.repository;

import com.example.jshop.domain.customer.Customer_Logged;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface CustomerRepository extends CrudRepository<Customer_Logged, Long> {


    @Override
    Customer_Logged save(Customer_Logged customer);

    Optional<Customer_Logged> findByUserName(String name);
}
