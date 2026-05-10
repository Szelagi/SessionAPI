#!/bin/bash
# Restarts the existing development server container.

cd "$(dirname "$0")/.." || exit 1

# Builds the project, copies the jar (via maven-antrun-plugin), and restarts the container
mvn clean package
docker restart craftcontainers-dev