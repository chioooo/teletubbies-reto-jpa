package com.teletubbies.jpa.repository;

import com.teletubbies.jpa.entity.OrderEntity;
import com.teletubbies.jpa.enums.OrderStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    List<OrderEntity> findByStatus(OrderStatus status);

    List<OrderEntity> findByCustomerEmail(String email);
}
