package com.teletubbies.jpa.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Estado de un pedido.
 *
 * <p>ESTA CLASE NO SE GENERA: el schema {@code OrderStatus} de open-api/enum.yaml está mapeado
 * aquí en el pom.xml. En la API viaja el nombre en inglés ({@code PENDING}); en la columna
 * {@code orders.status} se guarda {@code dbValue} ({@code PENDIENTE}), que es lo que ya tiene
 * la base. La traducción la hace {@code OrderStatusConverter}.
 */
@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    PENDING("PENDIENTE"),
    SHIPPED("ENVIADO"),
    DELIVERED("ENTREGADO"),
    CANCELLED("CANCELADO");

    private final String dbValue;

    public static OrderStatus fromDbValue(final String dbValue) {
        for (final OrderStatus status : values()) {
            if (status.dbValue.equals(dbValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado desconocido: " + dbValue);
    }
}
