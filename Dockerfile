# ===== 前端构建 =====
FROM node:20-alpine AS frontend
WORKDIR /app
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# ===== 后端构建 =====
FROM maven:3.9-eclipse-temurin-17 AS backend
WORKDIR /app
COPY pom.xml ./
COPY --from=frontend /app/dist/ ./src/main/resources/static/
COPY src/ ./src/
RUN mvn clean package -DskipTests

# ===== 运行环境 =====
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=backend /app/target/*.jar app.jar
RUN mkdir -p uploads logs
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
