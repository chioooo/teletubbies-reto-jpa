package com.teletubbies.jpa.exception;

import com.teletubbies.jpa.model.ErrorDetailResource;
import com.teletubbies.jpa.model.ErrorResource;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce excepciones a respuestas HTTP con el mismo {@link ErrorResource} en todos los casos.
 *
 * <pre>
 *   400  request mal formado (lo rechaza el contrato)
 *   404  el cliente o un producto no existe
 *   422  regla de negocio: no hay stock suficiente
 * </pre>
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResource> handleInsufficientStock(final InsufficientStockException ex) {
        log.warn("Pedido rechazado: {}", ex.getMessage());
        return error(HttpStatus.UNPROCESSABLE_CONTENT, "INSUFFICIENT_STOCK", ex.getMessage(), null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResource> handleNotFound(final ResourceNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResource> handleValidation(final MethodArgumentNotValidException ex) {
        final List<ErrorDetailResource> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ErrorDetailResource.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();
        return error(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "La petición tiene campos inválidos", details);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResource> handleUnreadable(final HttpMessageNotReadableException ex) {
        return error(HttpStatus.BAD_REQUEST, "MALFORMED_REQUEST", "El cuerpo de la petición no es un JSON válido", null);
    }

    private ResponseEntity<ErrorResource> error(
            final HttpStatus status, final String code, final String message, final List<ErrorDetailResource> details) {

        final ErrorResource body = ErrorResource.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .code(code)
                .message(message)
                .details(details)
                .build();

        return ResponseEntity.status(status).body(body);
    }
}
