package com.teletubbies.jpa.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.teletubbies.jpa.exception.InsufficientStockException;
import com.teletubbies.jpa.model.CreateOrderRequest;
import com.teletubbies.jpa.model.OrderItemRequest;
import com.teletubbies.jpa.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Corre contra el MySQL de docker ({@code docker compose up -d}).
 *
 * <p>{@code @Transactional} en la clase: cada prueba corre dentro de una transacción
 * que se revierte al terminar, así las pruebas no ensucian la base.
 */
@SpringBootTest
@Transactional
class OrderServiceTest {

    // Datos de V2__insert_initial_data.sql
    private static final int ANA_LOPEZ_ID = 1;
    private static final int MOUSE_ID = 2;   // Mouse inalámbrico, stock 50
    private static final int DESK_ID = 6;    // Escritorio, stock 5

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void exampleReadsSeedData() {
        final var mouse = productRepository.findById(MOUSE_ID).orElseThrow();

        assertThat(mouse.getName()).isEqualTo("Mouse inalámbrico");
    }

    // TODO reto 4: escribe aquí la prueba "crear un pedido descuenta el stock"
    @Test
    void createOrderDecreasesStock() {

        // GIVEN
        final int stockBefore = productRepository
                .findById(MOUSE_ID)
                .orElseThrow()
                .getStock();

        final var request = new CreateOrderRequest(
                ANA_LOPEZ_ID,
                List.of(
                        new OrderItemRequest(MOUSE_ID, 2)
                )
        );

        // WHEN
        orderService.createOrder(request);

        // THEN
        final int stockAfter = productRepository
                .findById(MOUSE_ID)
                .orElseThrow()
                .getStock();

        assertThat(stockAfter).isEqualTo(stockBefore - 2);
    }

    /**
     * Un pedido con un producto sin stock no deja nada guardado.
     *
     * <p>{@code NOT_SUPPORTED}: esta prueba NO corre dentro de la transacción de la clase.
     * Si corriera dentro, el service se uniría a esa transacción y leeríamos el mouse en
     * memoria, con el stock ya descontado. Así, el service usa su propia transacción y las
     * lecturas de abajo van directo a la base: si el rollback funciona, el stock no cambió.
     */
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void createOrderWithoutStockSavesNothing() {
        final int mouseStockBefore = productRepository.findById(MOUSE_ID).orElseThrow().getStock();
        final int deskStockBefore = productRepository.findById(DESK_ID).orElseThrow().getStock();
        final var request = new CreateOrderRequest(ANA_LOPEZ_ID, List.of(
                new OrderItemRequest(MOUSE_ID, 1),
                new OrderItemRequest(DESK_ID, 999)));

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(InsufficientStockException.class);

        assertThat(productRepository.findById(MOUSE_ID).orElseThrow().getStock()).isEqualTo(mouseStockBefore);
        assertThat(productRepository.findById(DESK_ID).orElseThrow().getStock()).isEqualTo(deskStockBefore);
    }

    @Test
    void listOrdersReturnsCustomerName() {

        // GIVEN
        orderService.createOrder(
                new CreateOrderRequest(
                        ANA_LOPEZ_ID,
                        List.of(
                                new OrderItemRequest(MOUSE_ID, 1)
                        )
                )
        );

        // WHEN
        final var orders = orderService.listOrders();

        // THEN
        assertThat(orders).isNotEmpty();
        assertThat(orders)
                .anyMatch(order -> order.getCustomerName().equals("Ana López"));
    }
}
