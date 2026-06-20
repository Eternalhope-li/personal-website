# ===== Stage 1: 构建前端 =====
FROM node:20-alpine AS frontend-build
WORKDIR /app
# 一次性复制所有前端源码（不含node_modules，因为被.gitignore排除）
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install --no-audit --no-fund
COPY frontend/ ./
RUN npm run build

# ===== Stage 2: 构建后端 =====
FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /app
# 先复制 pom.xml 下载依赖（利用 Docker 缓存）
COPY pom.xml ./
RUN mvn dependency:go-offline -q 2>/dev/null || true
# 复制前端构建产物到静态资源目录
COPY --from=frontend-build /app/dist/ ./src/main/resources/static/
# 复制后端源码
COPY src/ ./src/
RUN mvn clean package -DskipTests -q

# ===== Stage 3: 运行环境 =====
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache tzdata curl && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
RUN mkdir -p /app/uploads /app/logs
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s \
  CMD curl -f http://localhost:8080/api/health || exit 1
CMD ["java", "-jar", "app.jar"]
