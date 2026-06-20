FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache tzdata curl
WORKDIR /app
COPY app.jar app.jar
RUN mkdir -p uploads logs
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
