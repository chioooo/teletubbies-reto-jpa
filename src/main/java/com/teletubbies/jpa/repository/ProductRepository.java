package com.teletubbies.jpa.repository;

import com.teletubbies.jpa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
}
