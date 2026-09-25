package com.teletubbies.jpa.repository;

import com.teletubbies.jpa.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
}
