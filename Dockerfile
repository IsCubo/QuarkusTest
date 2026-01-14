# Etapa 1: Construcción nativa
FROM quay.io/quarkus/ubi-quarkus-maven:3.5 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Construir la aplicación en modo nativo
RUN mvn package -Pnative -DskipTests

# Etapa 2: Imagen de producción nativa
FROM registry.access.redhat.com/ubi8/ubi-minimal
WORKDIR /app

# Copiar binario nativo desde la etapa build
COPY --from=build /app/target/*-runner /app/app

# Dar permisos de ejecución
RUN chmod 755 /app/app

EXPOSE 8080

CMD ["/app/app"]
