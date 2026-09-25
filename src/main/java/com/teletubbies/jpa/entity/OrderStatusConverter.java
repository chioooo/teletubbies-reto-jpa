package com.teletubbies.jpa.entity;

import com.teletubbies.jpa.enums.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Guarda {@link OrderStatus} en la columna VARCHAR {@code orders.status} usando su {@code dbValue}.
 * Con {@code autoApply = true} se aplica a todo campo de tipo {@link OrderStatus}, sin anotarlo.
 */
@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(final OrderStatus status) {
        return status == null ? null : status.getDbValue();
    }

    @Override
    public OrderStatus convertToEntityAttribute(final String value) {
        return value == null ? null : OrderStatus.fromDbValue(value);
    }
}
