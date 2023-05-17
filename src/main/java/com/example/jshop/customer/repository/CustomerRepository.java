package com.example.jshop.customer.repository;

import java.util.Optional;
import com.example.jshop.customer.domain.LoggedCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CustomerRepository extends CrudRepository<LoggedCustomer, Long> {

    @Override
    LoggedCustomer save(LoggedCustomer loggedCustomer);

    Optional<LoggedCustomer> findCustomer_LoggedByUserNameEquals(String userName);
}
