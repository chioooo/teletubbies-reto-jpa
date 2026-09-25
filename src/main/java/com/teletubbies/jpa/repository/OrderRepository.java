package com.teletubbies.jpa.repository;

import com.teletubbies.jpa.entity.OrderEntity;
import com.teletubbies.jpa.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Query("""
            SELECT o
            FROM OrderEntity o
            JOIN FETCH o.customer
            """)
    List<OrderEntity> findAllWithCustomer();

    List<OrderEntity> findByStatus(OrderStatus status);

    List<OrderEntity> findByCustomerEmail(String email);

}