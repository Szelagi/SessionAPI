#!/bin/bash
# Gracefully stops and removes the development server container.

cd "$(dirname "$0")/.." || exit 1

docker compose -f docker-compose-dev.yml down
echo "Container stopped and removed."