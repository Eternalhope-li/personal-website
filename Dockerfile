FROM maven:3.9-eclipse-temurin-17
WORKDIR /app

RUN apt-get update -qq && apt-get install -y -qq nodejs npm && rm -rf /var/lib/apt/lists/*

COPY frontend/package.json frontend/package-lock.json ./frontend/
RUN cd frontend && npm install --no-audit --no-fund
COPY frontend/ ./frontend/
RUN cd frontend && npm run build

RUN mkdir -p src/main/resources/static && cp -a frontend/dist/. src/main/resources/static/

COPY pom.xml ./
COPY src/ ./src/
RUN mvn clean package -DskipTests -q

EXPOSE 8080
CMD ["sh", "-c", "java -jar target/*.jar"]
