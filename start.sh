#!/bin/bash
echo "=== Waiting for MySQL... ===
MAX=30
TRY=0
HOST=${MYSQLHOST:-127.0.0.1}
PORT=${MYSQLPORT:-3306}
USER=${MYSQLUSER:-root}
PASS=${MYSQLPASSWORD:-root123}
until mysqladmin ping -h"$HOST" -P"$PORT" -u"$USER" -p"$PASS" --silent 2>/dev/null || [ $TRY -eq $MAX ]
do
  TRY=$((TRY+1))
  sleep 2
done
echo "=== Starting application... ===
exec java -jar app.jar
