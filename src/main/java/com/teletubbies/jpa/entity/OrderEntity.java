package com.teletubbies.jpa.entity;

import com.teletubbies.jpa.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItemEntity> items = new ArrayList<>();

    public OrderEntity(
            CustomerEntity customer,
            LocalDate orderDate,
            OrderStatus status
    ) {
        this.customer = customer;
        this.orderDate = orderDate;
        this.status = status;
    }

    public void addItem(ProductEntity product, Integer quantity) {
        items.add(new OrderItemEntity(this, product, quantity));
    }
}