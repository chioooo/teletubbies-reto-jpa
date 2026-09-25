package com.teletubbies.jpa.service;

import com.teletubbies.jpa.exception.ResourceNotFoundException;
import com.teletubbies.jpa.model.CreateOrderRequest;
import com.teletubbies.jpa.model.OrderItemRequest;
import com.teletubbies.jpa.model.OrderResource;
import com.teletubbies.jpa.model.OrderSummaryResource;
import com.teletubbies.jpa.repository.CustomerRepository;
import com.teletubbies.jpa.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    // TODO reto 3: agrega aquí tu OrderRepository (private final, igual que los de arriba)

    /**
     * Crea un pedido en estado PENDING para el cliente con los productos indicados.
     *
     * <ul>
     *   <li>Valida que haya stock de cada producto.</li>
     *   <li>Descuenta el stock.</li>
     *   <li>Copia en cada renglón el precio actual del producto ({@code unit_price}).</li>
     *   <li>Si algún producto no tiene stock, lanza {@code InsufficientStockException} y no se guarda nada.</li>
     * </ul>
     *
     * <p>{@code @Transactional}: todo el método es UNA transacción. Si algo lanza una excepción
     * (RuntimeException), se revierte todo lo que se hizo dentro, incluido el stock ya descontado.
     * Además, las entidades que se leen aquí quedan <em>managed</em>: si les cambias un campo,
     * JPA genera el UPDATE solo al terminar el método, sin llamar {@code save}.
     */
    @Transactional
    public OrderResource createOrder(final CreateOrderRequest request) {
        // findById regresa un Optional: puede traer la entidad o venir vacío si el id no existe.
        // orElseThrow saca la entidad o, si viene vacío, lanza la excepción (el handler responde 404).
        final var customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", request.getCustomerId()));

        // TODO reto 3: crea el pedido (OrderEntity) para el cliente

        for (final OrderItemRequest item : request.getItems()) {
            final var product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", item.getProductId()));

            // TODO reto 3: descuenta el stock del producto y agrega el renglón al pedido
        }

        // TODO reto 3: guarda el pedido con orderRepository.save(...)

        // TODO reto 3: regresa el pedido creado con OrderResource.builder()
        throw new UnsupportedOperationException("Pendiente");
    }

    /**
     * Lista todos los pedidos con el nombre de su cliente.
     *
     * <p>{@code readOnly = true}: transacción solo de lectura. Hace falta porque {@code open-in-view}
     * está apagado: fuera de una transacción no se puede leer una relación LAZY como el cliente.
     */
    @Transactional(readOnly = true)
    public List<OrderSummaryResource> listOrders() {
        // TODO reto 5
        throw new UnsupportedOperationException("Pendiente");
    }
}
