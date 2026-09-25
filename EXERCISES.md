# Reto JPA: la tienda pasa a JPA

**En parejas · 75 minutos**

La semana pasada trabajaste la tienda con SQL. Hoy la misma base se trabaja desde Java con JPA: entidades, relaciones, repositorios, una regla de negocio transaccional y pruebas. La API ya tiene su contrato OpenAPI; tu trabajo es la persistencia que hay detrás.

## Preparación

Con Docker Desktop abierto, desde la raíz del proyecto:

```bash
docker compose up -d
```

Y después corre las pruebas:

| Windows | Mac / Git Bash |
|---|---|
| `.\mvnw.cmd test` | `./mvnw test` |

Deben salir 2 pruebas en verde y 1 omitida (*skipped*): esa se activa en el reto 4. Si algo falla, avisa antes de empezar.

## Esquema

```
customers   (id, name, email, city, created_at)
products    (id, name, category, price, stock)
orders      (id, customer_id → customers, order_date, status)
order_items (id, order_id → orders, product_id → products, quantity, unit_price)
```

Las tablas ya existen (migraciones V1 y V2). **No crees migraciones nuevas**: el reto es que las entidades coincidan con el esquema tal como está.

## Qué ya está hecho

| Qué | Dónde |
|---|---|
| Contrato: `POST /api/v1/orders` y `GET /api/v1/orders` | `open-api/` |
| `CreateOrderRequest`, `OrderResource`, `OrderSummaryResource` (generados) | se generan al compilar |
| `OrderDelegate`: la capa web, ya llama al service | `delegate/` |
| `CustomerEntity` y `ProductEntity` completas (úsalas de ejemplo) | `entity/` |
| `OrderStatus` y su converter (`autoApply = true`) | `enums/`, `entity/` |
| `CustomerRepository` y `ProductRepository` | `repository/` |
| Excepciones y `ApiExceptionHandler` (404 y 422) | `exception/` |
| `OrderService` con el esqueleto: transacción, búsqueda de cliente y productos, y los pasos que te tocan como `TODO` | `service/` |
| Prueba de ejemplo, constantes con ids de los datos iniciales y la prueba de rollback | `src/test/.../OrderServiceTest.java` |
| Peticiones listas para probar la API | `docs/peticiones.http` |

La app corre con `ddl-auto: validate`: **si una entidad no coincide con su tabla, la app no arranca** y el error te dice qué columna falla. Con `show-sql: true` verás en la consola cada consulta que genera Hibernate.

---

## El reto

### 1. Entidades y relaciones

Crea `OrderEntity` (tabla `orders`) y `OrderItemEntity` (tabla `order_items`) en el paquete `entity`:

- `OrderEntity` → `CustomerEntity`: `@ManyToOne(fetch = FetchType.LAZY)`
- `OrderEntity` → `OrderItemEntity`: `@OneToMany(mappedBy = ...)` con la cascada que elijan
- `OrderItemEntity` → `ProductEntity`: `@ManyToOne(fetch = FetchType.LAZY)`

Pistas:
- Los ids son `INT AUTO_INCREMENT`: `GenerationType.IDENTITY`, igual que en las entidades de ejemplo.
- La llave foránea se indica con `@JoinColumn(name = "customer_id")`.
- `status` es de tipo `OrderStatus`; el converter ya se aplica solo.
- Sin `@Data` y sin setters. Para crear objetos, usa constructores.
- `order_id` no puede ser null: cuando agregues un renglón al pedido, el renglón también tiene que saber a qué pedido pertenece. Un método `addItem(product, quantity)` en `OrderEntity` que cree el `OrderItemEntity` pasándole `this` resuelve las dos cosas.

**Listo cuando:** las pruebas pasan (`contextLoads` valida el mapeo).

> Escribe aquí qué cascada usaron y por qué:
>
> _…_

### 2. Repositorio

Crea `OrderRepository` con dos consultas derivadas:

- pedidos por estado (`status`)
- pedidos por email del cliente

### 3. Regla de negocio: crear un pedido

Completa `OrderService.createOrder(CreateOrderRequest)`. El esqueleto ya trae:

- `@Transactional`: todo el método es **una sola transacción**. Si algo lanza una excepción, se revierte todo, incluido el stock ya descontado.
- La búsqueda del cliente y de cada producto con `findById(...).orElseThrow(...)`: si el id no existe, responde 404.

Lee los comentarios del método y completa los `TODO`:

1. Agrega tu `OrderRepository` al service.
2. Crea el pedido para el cliente, con estado `PENDING` y la fecha de hoy.
3. Por cada producto: valida que haya stock suficiente, descuéntalo y agrega el renglón al pedido con el **precio actual** del producto en `unit_price` (como en el ejercicio 1.3 de la práctica SQL).
4. Si algún producto no tiene stock suficiente, lanza `InsufficientStockException`.
5. Guarda el pedido.
6. Regresa un `OrderResource` (tiene `builder()`, como el `ErrorResource` de `endpoints`).

Pistas:
- `ProductEntity` no tiene setters: agrégale un método que valide y descuente el stock.
- El producto que regresa `findById` está *managed* (ciclo de vida de la entidad): si le cambias el stock, JPA hace el UPDATE solo al terminar la transacción. No necesitas `save` para el producto.
- Con la cascada correcta, basta con guardar el pedido para que se guarden sus renglones.

Pruébalo con la app corriendo y `docs/peticiones.http`: el pedido válido responde 201 y el de 999 escritorios, 422.

### 4. Pruebas

En `OrderServiceTest`:

1. **Escribe la prueba "crear un pedido descuenta el stock".** Lee el stock del mouse, crea un pedido de 2 mouse y verifica que el stock bajó 2 y que la respuesta trae un `id`. Para armar el request:

   ```java
   final var request = new CreateOrderRequest(ANA_LOPEZ_ID, List.of(new OrderItemRequest(MOUSE_ID, 2)));
   ```

2. **Activa la prueba de rollback.** `createOrderWithoutStockSavesNothing` ya está escrita: pide 1 mouse y 999 escritorios y verifica que el stock no cambió. Quítale la línea `@Disabled` y comprueba que pasa. Su comentario explica por qué lleva `NOT_SUPPORTED`.

### 5. Problema N+1

1. Implementa `OrderService.listOrders()`: todos los pedidos con el nombre de su cliente (`OrderSummaryResource`).
   El método ya trae `@Transactional(readOnly = true)`; su comentario explica por qué hace falta.
2. Con la app corriendo, llama `GET /api/v1/orders` (está en `docs/peticiones.http`) y **cuenta las consultas** que aparecen en la consola (`Hibernate: select ...`).
3. Corrígelo con `JOIN FETCH` o `@EntityGraph` en `OrderRepository`.
4. Vuelve a contar.

> Anota aquí el resultado:
>
> Antes: _…_ consultas · Después: _…_ consultas

---

## Qué se entrega

```
src/main/java/com/teletubbies/jpa/
├── entity/OrderEntity.java           (nuevo)
├── entity/OrderItemEntity.java       (nuevo)
├── entity/ProductEntity.java         (método para descontar stock)
├── repository/OrderRepository.java   (nuevo)
└── service/OrderService.java         (TODO de createOrder y listOrders)
src/test/java/com/teletubbies/jpa/
└── service/OrderServiceTest.java     (1 prueba nueva y la de rollback activada)
EXERCISES.md                          (cascada y conteo de consultas)
```

## Qué se evalúa

- Las pruebas pasan: la app arranca con `validate` y las pruebas están en verde.
- El pedido se crea en una sola transacción: la prueba de rollback pasa sin `@Disabled`.
- Las relaciones son LAZY y el N+1 está corregido, con el antes y el después anotados.
