# Etap budowania (Maven + JDK)
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Najpierw sam pom.xml (cache zależności)
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Potem źródła
COPY src src

# Budowa
RUN mvn -q -DskipTests package

# Etap uruchomieniowy (lżejszy JRE)
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Kopiujemy JAR (jeśli masz więcej JARów – podaj dokładną nazwę zamiast *.jar)
COPY --from=builder /workspace/target/*.jar app.jar

# Katalog na uploady
RUN mkdir -p /app/uploads/attachments
ENV APP_ATTACHMENTS_BASE_PATH=/app/uploads/attachments
ENV JAVA_OPTS=""

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]