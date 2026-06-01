# Multi-stage build for Render deployment
# Stage 1: Build Spring Boot backend
FROM maven:3.8.6-openjdk-11 AS builder
WORKDIR /build
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B 2>/dev/null || true
COPY backend/src ./src
RUN mvn package -DskipTests -q

# Stage 2: Runtime
FROM openjdk:11-jre-slim

RUN apt-get update && \
    apt-get install -y nginx && \
    rm -rf /var/lib/apt/lists/* && \
    mkdir -p /app/data

COPY --from=builder /build/target/book-management-1.0.0.jar /app/app.jar
COPY frontend/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
COPY start.sh /start.sh
RUN chmod +x /start.sh

EXPOSE 80

CMD ["/start.sh"]
