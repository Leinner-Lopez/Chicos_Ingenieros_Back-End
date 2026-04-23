# Usamos una imagen de Maven con Java 21 para compilar el código
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiamos el pom.xml y descargamos las dependencias para aprovechar el caché de capas de Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente y generamos el archivo JAR (saltando las pruebas para agilizar)
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Runtime)
# Usamos una imagen de JRE (Java Runtime Environment) mucho más ligera
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el archivo JAR generado en la etapa anterior
# El nombre suele ser el artifactId (Zenkai) más la versión
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto estándar de Spring Boot
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]