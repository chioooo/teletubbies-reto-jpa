# Reto JPA: la tienda pasa a JPA

API de pedidos sobre el esquema de la tienda de la práctica SQL: Spring Boot + JPA + MySQL, contrato OpenAPI.

El reto está en [EXERCISES.md](EXERCISES.md).

## Requisitos

- **Docker Desktop**, abierto. La base de datos siempre corre en Docker.
- **Java 21**, para correr las pruebas y la API en local.
- **`make`, opcional.** Todo se puede hacer sin él; abajo vienen los comandos de las dos formas.

No necesitas instalar Maven: el proyecto trae el **Maven wrapper** (`mvnw` / `mvnw.cmd`), que lo descarga solo la primera vez.

## Dos formas de correr la API

La base siempre va en Docker. La API la puedes correr como prefieras:

| | API en local | API en Docker |
|---|---|---|
| Qué necesitas | Java 21 | Solo Docker |
| Después de cambiar código | Detén la API y vuelve a correrla | Vuelve a correr el comando de levantar (reconstruye la imagen) |
| Dónde ves las consultas SQL | En la consola donde la corriste | Con el comando de logs |
| Depurar con breakpoints | Sí, desde IntelliJ | No |

**Para el reto recomendamos la API en local**: los cambios se prueban más rápido y ves las consultas SQL directo en la consola.

Las dos usan el puerto **8080**: no las corras al mismo tiempo.

## Comandos

Todos se corren desde la raíz del proyecto.

### Base de datos

| Qué hace | Con make | Sin make |
|---|---|---|
| Levantar la base (MySQL, puerto 3308) | `make db-up` | `docker compose up -d` |
| Consola de MySQL | `make db-shell` | `docker exec -it store-jpa-db mysql -uiwa -pdemo store` |

### API en local

| Qué hace | Con make | Sin make · Windows | Sin make · Mac / Linux / Git Bash |
|---|---|---|---|
| Correr las pruebas | `make test` | `.\mvnw.cmd test` | `./mvnw test` |
| Levantar la API (http://localhost:8080) | `make run` | `.\mvnw.cmd spring-boot:run` | `./mvnw spring-boot:run` |
| Compilar sin correr pruebas | `make build` | `.\mvnw.cmd clean package -DskipTests` | `./mvnw clean package -DskipTests` |
| Regenerar el código del contrato OpenAPI | `make openapi-generate` | `.\mvnw.cmd generate-sources` | `./mvnw generate-sources` |

Para detener la API local: `Ctrl+C` en su consola.

También puedes correr las pruebas y la API desde IntelliJ con el botón ▶. Si IntelliJ marca en rojo las clases de `api` o `model`, corre una vez la compilación (`make build` o `.\mvnw.cmd compile`), que las genera, y recarga el proyecto de Maven.

### API en Docker

| Qué hace | Con make | Sin make |
|---|---|---|
| Levantar la base y la API (http://localhost:8080) | `make api-up` | `docker compose --profile api up -d --build` |
| Ver el log de la API, con las consultas SQL | `make api-logs` | `docker compose logs -f api` |

La primera vez tarda unos minutos: compila el proyecto dentro de Docker. Para salir del log: `Ctrl+C` (la API sigue corriendo).

### Detener y empezar de cero

| Qué hace | Con make | Sin make |
|---|---|---|
| Detener todo (conserva los datos) | `make down` | `docker compose --profile api down` |
| Borrar la base y volver a los datos iniciales | `make reset` | `docker compose --profile api down -v` y luego `docker compose up -d` |

### ¿`make` en Windows?

Con `make` instalado (por ejemplo, `choco install make`), los comandos funcionan igual desde PowerShell, CMD o Git Bash. Si `make` no está o algo falla, usa la columna "Sin make": cada target del `Makefile` es exactamente ese comando.

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
compose.yaml              la base y, con --profile api, la API
Dockerfile                imagen de la API
Makefile                  atajos de los comandos de arriba
```

**Flyway es dueño del esquema**: las entidades solo lo reflejan (`ddl-auto: validate`).

## Ver la API

Con la API corriendo, en local o en Docker:

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
