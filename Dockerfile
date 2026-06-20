FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY app.jar app.jar
EXPOSE 8080
CMD ["sh", "-c", "echo \"Waiting 30s for MySQL...\" && sleep 30 && echo \"Starting app...\" && java -jar app.jar"]
