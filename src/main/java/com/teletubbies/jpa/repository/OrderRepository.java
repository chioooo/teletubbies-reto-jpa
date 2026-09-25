package com.teletubbies.jpa.repository;

import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

/**
 2. Repositorio
 Crea OrderRepository con dos consultas derivadas:

 pedidos por estado (status)
 pedidos por email del cliente
 */

@Repository
@Validated
public interface OrderRepository {


}
