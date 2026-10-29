# syntax=docker/dockerfile:1

# ---------- build ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

# Сначала только файлы сборки — слой с зависимостями кешируется, пока не меняется pom.xml
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && sed -i 's/\r$//' mvnw
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B dependency:go-offline

COPY src src
# Тесты требуют PostgreSQL, они гоняются в CI
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B package -DskipTests

# Раскладываем jar по слоям для лучшего кеширования образа
RUN java -Djarmode=tools -jar target/*.jar extract --layers --launcher --destination extracted

# ---------- runtime ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app app
USER app

COPY --from=build /workspace/extracted/dependencies/ ./
COPY --from=build /workspace/extracted/spring-boot-loader/ ./
COPY --from=build /workspace/extracted/snapshot-dependencies/ ./
COPY --from=build /workspace/extracted/application/ ./

ENV SPRING_PROFILE=prod \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
