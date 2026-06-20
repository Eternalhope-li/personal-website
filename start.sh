#!/bin/sh
echo "=== Personal Website ==="
echo "MySQL URL: ${MYSQL_URL:-local}"
echo "Waiting 15s for MySQL..."
sleep 15
echo "Starting application..."
exec java -jar app.jar
