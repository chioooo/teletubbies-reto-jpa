package com.teletubbies.jpa.delegate;

import com.teletubbies.jpa.api.OrderApiDelegate;
import com.teletubbies.jpa.model.CreateOrderRequest;
import com.teletubbies.jpa.model.OrderResource;
import com.teletubbies.jpa.model.OrderSummaryResource;
import com.teletubbies.jpa.service.OrderService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Capa web: implementa el OrderApiDelegate que genera openapi-generator desde open-api/.
 * Solo decide el código de estado y delega al service.
 */
@Service
public record OrderDelegate(OrderService orderService) implements OrderApiDelegate {

    /**
     * 201 porque sí se crea un recurso. Lo correcto sería acompañarlo de un header
     * Location hacia GET /api/v1/orders/{id}, pero ese endpoint no es parte de este reto.
     */
    @Override
    public ResponseEntity<OrderResource> createOrder(final CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @Override
    public ResponseEntity<List<OrderSummaryResource>> listOrders() {
        return ResponseEntity.ok(orderService.listOrders());
    }
}
