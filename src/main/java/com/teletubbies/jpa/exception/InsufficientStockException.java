package com.teletubbies.jpa.exception;

/**
 * Regla de negocio: no hay stock suficiente de un producto.
 *
 * <p>Es RuntimeException a propósito: con {@code @Transactional}, una excepción unchecked
 * hace rollback de toda la transacción.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(final String productName, final int requested, final int available) {
        super("Stock insuficiente de '" + productName + "': se pidieron " + requested + ", hay " + available);
    }
}
