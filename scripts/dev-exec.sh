#!/bin/bash
# Executes a command directly in the server console via RCON. Usage: ./dev-exec.sh <command>

cd "$(dirname "$0")/.." || exit 1

if [ -z "$1" ]; then
  echo "Usage: ./scripts/dev-exec.sh <command>"
  exit 1
fi

docker exec -i craftcontainers-dev rcon-cli "$@"