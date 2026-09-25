# ---------------------------------------------------------------------------
# Atajos para las tareas comunes del proyecto. `make` sin argumentos muestra la ayuda.
#
# Si no tienes `make`, cada target es una sola línea: mira la receta y córrela
# a mano, o usa la tabla "Sin make" del README.
# ---------------------------------------------------------------------------

# En Windows se usa cmd.exe como shell para que mvnw.cmd funcione igual si corres
# `make` desde PowerShell, CMD o Git Bash. Se usa el Maven wrapper: no hace falta
# tener Maven instalado.
ifeq ($(OS),Windows_NT)
    SHELL := cmd.exe
    .SHELLFLAGS := /c
    MVNW := .\mvnw.cmd
else
    MVNW := ./mvnw
endif

.PHONY: help db-up db-shell test run build openapi-generate api-up api-logs down reset clean

help:
	@echo Targets disponibles:
	@echo   make db-up             levanta la base MySQL en Docker, puerto 3308
	@echo   make test              corre las pruebas, necesita la base arriba
	@echo   make run               levanta la API en local en http://localhost:8080
	@echo   make api-up            levanta la base y la API en Docker, puerto 8080
	@echo   make api-logs          muestra el log de la API en Docker, con las consultas SQL
	@echo   make down              detiene los contenedores, conserva los datos
	@echo   make reset             borra la base y la vuelve a crear con los datos iniciales
	@echo   make db-shell          abre la consola de MySQL
	@echo   make build             compila y arma el jar sin correr pruebas
	@echo   make openapi-generate  regenera el codigo del contrato OpenAPI
	@echo   make clean             borra target/

# --- Base de datos ----------------------------------------------------------

db-up:
	docker compose up -d

db-shell:
	docker exec -it store-jpa-db mysql -uiwa -pdemo store

# --- API en local (necesita Java 21) ---------------------------------------

test:
	$(MVNW) test

run:
	$(MVNW) spring-boot:run

build:
	$(MVNW) clean package -DskipTests

# Lo generado vive en target/generated-sources/openapi. test, run y build ya lo
# regeneran solos; esto es para ver el código generado sin compilar todo.
openapi-generate:
	$(MVNW) generate-sources

clean:
	$(MVNW) clean

# --- API en Docker (no necesita Java en tu máquina) ------------------------

# --build reconstruye la imagen: córrelo de nuevo cada vez que cambies código
api-up:
	docker compose --profile api up -d --build

api-logs:
	docker compose logs -f api

# --- Detener y limpiar -----------------------------------------------------

down:
	docker compose --profile api down

# -v borra el volumen de la base: se pierden los datos
reset:
	docker compose --profile api down -v
	docker compose up -d
