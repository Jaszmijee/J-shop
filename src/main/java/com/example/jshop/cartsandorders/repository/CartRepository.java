package com.example.jshop.cartsandorders.repository;

import java.util.List;
import com.example.jshop.cartsandorders.domain.cart.Cart;
import com.example.jshop.cartsandorders.domain.cart.CartStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CartRepository extends CrudRepository<Cart, Long> {

    @Override
    Cart save(Cart cart);

    void deleteByCartStatusAndAndCamundaProcessId(CartStatus cartStatus, String processId);

    @Query(value = "SELECT * from carts " +
        "where status = 'PROCESSING' " +
        "AND camunda_process_Id =:PROCESS_ID",
        //    "AND DATEDIFF(CURDATE(), created) >= 3",
        nativeQuery = true)
    List<Cart> selectByProcessingTime(@Param("PROCESS_ID") String processId);

    @Override
    List<Cart> findAll();
}
