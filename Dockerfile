# syntax=docker/dockerfile:1
#
# Build:
#   docker build -t family-dinner-time .
#
# Or from this directory:
#   docker compose up -d
#
# Run (Postgres must be reachable; default application.yml uses localhost:5432).
# When the API runs in Docker and Postgres is on the host (published on 5432), use:
#   docker run --rm -p 8080:8080 \
#     -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/dinner \
#     -e SPRING_DATASOURCE_USERNAME=postgres \
#     -e SPRING_DATASOURCE_PASSWORD=postgres \
#     family-dinner-time
#
# On Linux, if host.docker.internal is missing, add:
#   --add-host=host.docker.internal:host-gateway

FROM gradle:8.14.2-jdk17 AS build
WORKDIR /workspace

COPY --chown=gradle:gradle . .

RUN gradle generateJavaJooq --no-daemon

RUN gradle compileKotlin --no-daemon && \
    gradle bootJar --no-daemon -x test && \
    JAR=$(ls build/libs/*.jar | grep -v 'plain.jar' | head -n 1) && \
    cp "$JAR" /tmp/application.jar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app app

COPY --from=build --chown=app:app /tmp/application.jar app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

