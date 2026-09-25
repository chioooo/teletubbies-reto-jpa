# Imagen de la API. Se construye con: docker compose --profile api up -d --build
#
# Etapa 1: compila el jar dentro de Docker (no necesitas Java en tu máquina para esto)
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY open-api/ open-api/
COPY src/ src/

RUN chmod +x mvnw
# La caché de ~/.m2 se reutiliza entre builds: Maven no descarga todo cada vez
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B package -DskipTests

# Etapa 2: solo el JRE y el jar
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/store-api.jar store-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Duser.timezone=America/Mexico_City", "-jar", "store-api.jar"]
