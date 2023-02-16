package com.example.jshop.repository;

import com.example.jshop.domain.customer.Customer_Logged;
import com.example.jshop.domain.order.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Override
    Order save(Order order);


    Optional<Order> findByOrderIDAndAndCustomer_UserName(Long orderId, String userName);


    @Query(value = "SELECT * from orders " +
            "where (:STATUS IS NULL OR order_status LIKE :STATUS) " +
            "ORDER BY order_status ASC", nativeQuery = true)
    List<Order> findOrders(@Param("STATUS") String order_status);


    List<Order> findByCustomer_UserName(String userName);

    @Query(value = "SELECT * from orders " +
            "where order_status = 'UNPAID' " +
            "AND Paid IS NULL " +
            "AND DATEDIFF(CURDATE(), Created) = 13",
            nativeQuery = true)
    List<Order> findOrdersCloseTOPayment();

    @Query(value = "SELECT * from orders " +
            "where order_status = 'UNPAID' " +
            "AND Paid IS NULL " +
            "AND DATEDIFF(CURDATE(), Created) > 14",
            nativeQuery = true)
    List<Order> findUnpaidOrders();
}

