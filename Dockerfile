# =========================
# Etapa 1: Build (JVM)
# =========================
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias primero (mejor cache)
RUN ./mvnw -B dependency:go-offline

COPY src ./src
RUN ./mvnw package -DskipTests


# =========================
# Etapa 2: Runtime
# =========================
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/quarkus-app /app/quarkus-app

EXPOSE 8080

CMD ["java", "-jar", "/app/quarkus-app/quarkus-run.jar"]
