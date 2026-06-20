#!/bin/sh
echo "=== Waiting for MySQL ===
HOST="localhost"
PORT="3306"
if [ -n "$MYSQL_URL" ]; then
  HOST=$(echo "$MYSQL_URL" | sed "s|jdbc:mysql://||" | cut -d: -f1)
  PORT=$(echo "$MYSQL_URL" | sed "s|jdbc:mysql://||" | cut -d/ -f1 | cut -d: -f2)
fi
echo "Host: $HOST, Port: $PORT
MAX=30
i=0
while [ $i -lt $MAX ]; do
  if nc -z "$HOST" "$PORT" 2>/dev/null; then
    echo "MySQL ready after $((i*2))s
    break
  fi
  i=$((i+1))
  sleep 2
done
echo "=== Starting app ===
exec java -jar app.jar
