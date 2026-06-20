FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl mysql-client bash
WORKDIR /app
COPY app.jar app.jar
COPY start.sh start.sh
RUN chmod +x start.sh
EXPOSE 8080
CMD ["./start.sh"]
