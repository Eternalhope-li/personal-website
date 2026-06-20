FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY app.jar .
COPY start.sh .
RUN chmod +x start.sh
EXPOSE 8080

# Health check
HEALTHCHECK --start-period=30s --interval=30s --timeout=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

CMD ["./start.sh"]
