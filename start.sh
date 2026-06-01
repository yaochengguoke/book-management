#!/bin/bash
set -e

echo "=== Book Management System Starting ==="
echo "PORT: ${PORT:-80}"

# Create dynamic nginx config with environment variables
envsubst '${PORT}' < /etc/nginx/nginx.conf > /tmp/nginx_runtime.conf
mv /tmp/nginx_runtime.conf /etc/nginx/nginx.conf
echo "Nginx config prepared"

# Create data directory
mkdir -p /app/data

# Start Spring Boot backend in background
echo "Starting backend on port 8080..."
cd /app
java -jar /app/app.jar &
BACKEND_PID=$!

# Wait for backend to be ready
echo "Waiting for backend to start..."
for i in $(seq 1 30); do
    if curl -s -f http://localhost:8080/api/books > /dev/null 2>&1; then
        echo "Backend is ready! (PID: $BACKEND_PID)"
        break
    fi
    echo "  Waiting... ($i/30)"
    sleep 2
done

# Start nginx in foreground
echo "Starting nginx on port ${PORT:-80}..."
exec nginx -g "daemon off;"
