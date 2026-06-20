FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY app.jar .
COPY start.sh .
RUN chmod +x start.sh
EXPOSE 8080
CMD ["./start.sh"]
