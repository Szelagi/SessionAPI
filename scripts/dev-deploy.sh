#!/bin/bash
# Automated post-build script. Reads .env and applies the action (none/reload/restart) to the running server.

cd "$(dirname "$0")/.." || exit 1

CONTAINER_NAME="craftcontainers-dev"
DEFAULT_ACTION="reload"

# Load variables from .env file if it exists
if [ -f .env ]; then
  source .env
fi

# Set to default if POST_BUILD_ACTION is unset or empty
POST_BUILD_ACTION="${POST_BUILD_ACTION:-$DEFAULT_ACTION}"

# Check if the container is running
IF_RUNNING=$(docker ps -q -f name=$CONTAINER_NAME)

if [ -z "$IF_RUNNING" ]; then
    echo "Container $CONTAINER_NAME is not running. Skipping post-build action."
    exit 0
fi

# Execute action based on configuration
echo "Executing post-build action: $POST_BUILD_ACTION"

case "$POST_BUILD_ACTION" in
    "none")
        echo "Jar replaced. No further action taken."
        ;;
    "reload")
        echo "Triggering server reload..."
        # Note: Paper requires just 'reload confirm', Spigot uses 'bukkit:reload confirm'
        # Adjust the command below if your server complains about unknown command
        docker exec -i $CONTAINER_NAME rcon-cli "bukkit:reload confirm"
        ;;
    "restart")
        echo "Restarting the container..."
        docker restart $CONTAINER_NAME
        ;;
    *)
        echo "Unknown POST_BUILD_ACTION: '$POST_BUILD_ACTION'. Valid options are: none, reload, restart."
        exit 1
        ;;
esac