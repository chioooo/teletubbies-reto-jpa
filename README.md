# Reto JPA: la tienda pasa a JPA

API de pedidos sobre el esquema de la tienda de la práctica SQL: Spring Boot + JPA + MySQL, contrato OpenAPI.

El reto está en [EXERCISES.md](EXERCISES.md).

## Requisitos

- Java 21
- Docker Desktop (abierto)

No necesitas Maven ni `make`: el proyecto trae el **Maven wrapper** (`mvnw` / `mvnw.cmd`), que descarga Maven solo la primera vez.

## Comandos

Todos se corren desde la raíz del proyecto.

| Qué hace | Windows (PowerShell / CMD) | Mac / Linux / Git Bash |
|---|---|---|
| Levantar la base (MySQL, puerto 3308) | `docker compose up -d` | `docker compose up -d` |
| Correr las pruebas | `.\mvnw.cmd test` | `./mvnw test` |
| Levantar la app (http://localhost:8080) | `.\mvnw.cmd spring-boot:run` | `./mvnw spring-boot:run` |
| Consola de MySQL | `docker exec -it store-jpa-db mysql -uiwa -pdemo store` | igual |
| Borrar la base y empezar de cero | `docker compose down -v` y luego `docker compose up -d` | igual |

También puedes correr las pruebas y la app desde IntelliJ con el botón ▶. Si IntelliJ marca en rojo las clases de `api` o `model`, corre una vez `.\mvnw.cmd compile` (las genera) y recarga el proyecto de Maven.

## Estructura

```
open-api/                 el contrato: de aquí se generan api/ y model/ (no los edites en target/)
docs/peticiones.http      peticiones listas para probar la API
src/main/java/com/teletubbies/jpa/
├── delegate/             capa web: implementa OrderApiDelegate
├── entity/               entidades JPA
├── enums/                OrderStatus
├── exception/            excepciones y @RestControllerAdvice
├── repository/           repositorios Spring Data
└── service/              reglas de negocio
src/main/resources/db/migration/   migraciones Flyway (V1 y V2)
```

**Flyway es dueño del esquema**: las entidades solo lo reflejan (`ddl-auto: validate`).

## Ver la API

Con la app corriendo:

- Swagger UI: <http://localhost:8080/swagger-ui/index.html>
- Peticiones de ejemplo: [docs/peticiones.http](docs/peticiones.http)

## Esquema

| Tabla         | Descripción                        |
|---------------|------------------------------------|
| `customers`   | Clientes de la tienda              |
| `products`    | Productos con categoría y stock    |
| `orders`      | Pedidos de cada cliente            |
| `order_items` | Productos incluidos en cada pedido |

Esta base usa el contenedor `store-jpa-db` en el puerto **3308**, separado del de la práctica SQL (3307), para que tus migraciones de la semana pasada no interfieran.
