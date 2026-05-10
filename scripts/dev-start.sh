#!/bin/bash
# Starts the development server container, pulls updates, and waits for it to load.

cd "$(dirname "$0")/.." || exit 1

CONTAINER_NAME="craftcontainers-dev"
SERVER_ADDRESS="127.0.0.1:25565"
MAX_RETRIES=240
COUNTER=0

echo "Checking for image updates and pulling layers..."
docker compose -f docker-compose-dev.yml pull

docker compose -f docker-compose-dev.yml up -d

MC_VERSION=$(docker inspect "$CONTAINER_NAME" | grep -m 1 '"VERSION=' | awk -F'=' '{print $2}' | tr -d '",\r')
MC_VERSION="${MC_VERSION:-Unknown}"

echo "Container starting. Minecraft version: $MC_VERSION. Waiting for the Minecraft server to load..."

while [ $COUNTER -lt $MAX_RETRIES ]; do
    if docker exec -i "$CONTAINER_NAME" rcon-cli list > /dev/null 2>&1; then
        echo ""
        echo "========================================"
        echo "  Server is successfully started!"
        echo "  Minecraft version: $MC_VERSION"
        echo "  Join address: $SERVER_ADDRESS"
        echo "========================================"
        exit 0
    fi
    sleep 1
    ((COUNTER++))
done

echo ""
echo "========================================"
echo "  WARNING: Server failed to start within ${MAX_RETRIES}s or crashed."
echo "  Please check the logs manually:"
echo "  docker logs $CONTAINER_NAME"
echo "========================================"
exit 1