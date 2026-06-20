#!/bin/sh
echo "=== Personal Website ==="
echo "Waiting 45s for MySQL..."
sleep 45
echo "Starting application..."
exec java -jar app.jar
