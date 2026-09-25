package com.teletubbies.jpa.exception;

/**
 * El cliente o un producto del pedido no existe.
 *
 * <p>Uso: {@code repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto", id))}
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(final String resource, final Integer id) {
        super(resource + " con id " + id + " no existe");
    }
}
