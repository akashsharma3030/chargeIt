#!/bin/bash

# Script to build and run the Docker container

# Step 1: Build the Docker image
echo "Building the Docker image..."
docker build -t chargeit:1.0.0 .

# Step 2: Run the Docker container
echo "Running the Docker container..."
docker run -d -p 8080:8080 --name chargeit-container chargeit:1.0.0

# Step 3: Display running containers
echo "Docker container is running. Listing active containers:"
docker ps