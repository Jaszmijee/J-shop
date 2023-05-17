package com.example.jshop.cartsandorders.repository;

import java.util.List;
import java.util.Optional;
import com.example.jshop.cartsandorders.domain.order.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Override
    Order save(Order order);

    Optional<Order> findByOrderIDAndLoggedCustomer_UserName(Long orderId, String userName);

    @Query(value = "SELECT * from orders " +
        "where (:STATUS IS NULL OR order_status LIKE :STATUS) " +
        "ORDER BY order_status", nativeQuery = true)
    List<Order> findOrders(@Param("STATUS") String order_status);

    List<Order> findByLoggedCustomer_UserName(String userName);

    @Query(value = "SELECT * from orders " +
        "where order_status = 'UNPAID' " +
        "AND paid IS NULL " +
        "AND camunda_process_Id =:PROCESS_ID",
        // "AND DATEDIFF(CURDATE(), created) < 13",
        nativeQuery = true)
    List<Order> findOrdersCloseToPayment(@Param("PROCESS_ID") String processId);

    @Query(value = "SELECT * from orders " +
        "where order_status = 'UNPAID' " +
        "AND paid IS NULL " +
        "AND camunda_process_Id =:PROCESS_ID",
        //    "AND DATEDIFF(CURDATE(), created) > 14",
        nativeQuery = true)
    List<Order> findUnpaidOrders(@Param("PROCESS_ID") String processId);

    Order findByCart_CartID(Long cartId);
}

