package com.teletubbies.jpa.service;

import com.teletubbies.jpa.entity.OrderEntity;
import com.teletubbies.jpa.enums.OrderStatus;
import com.teletubbies.jpa.exception.InsufficientStockException;
import com.teletubbies.jpa.exception.ResourceNotFoundException;
import com.teletubbies.jpa.model.CreateOrderRequest;
import com.teletubbies.jpa.model.OrderItemRequest;
import com.teletubbies.jpa.model.OrderResource;
import com.teletubbies.jpa.model.OrderSummaryResource;
import com.teletubbies.jpa.repository.CustomerRepository;
import com.teletubbies.jpa.repository.OrderRepository;
import com.teletubbies.jpa.repository.ProductRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResource createOrder(final CreateOrderRequest request) {

        final var customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cliente",
                                request.getCustomerId()
                        ));

        final var order = new OrderEntity(
                customer,
                LocalDate.now(),
                OrderStatus.PENDING
        );

        for (final OrderItemRequest item : request.getItems()) {

            final var product = productRepository.findById(item.getProductId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Producto",
                                    item.getProductId()
                            ));

            if (product.getStock() < item.getQuantity()) {
                throw new InsufficientStockException(
                        product.getName(),
                        item.getQuantity(),
                        product.getStock()
                );
            }

            product.decreaseStock(item.getQuantity());

            order.addItem(product, item.getQuantity());
        }

        final var orderEntity = orderRepository.save(order);

        return OrderResource.builder()
                .id(orderEntity.getId())
                .customerId(orderEntity.getCustomer().getId())
                .orderDate(orderEntity.getOrderDate())
                .status(orderEntity.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryResource> listOrders() {
        return orderRepository.findAllWithCustomer()
                .stream()
                .map(order -> new OrderSummaryResource()
                        .id(order.getId())
                        .customerName(order.getCustomer().getName())
                        .orderDate(order.getOrderDate())
                        .status(order.getStatus()))
                .toList();
    }
}