ARG JAVA_VERSION=21
# -----------------------------------------------------------------------------
# Stage 1: Builder - Build & Extract Layers (Cache Optimized)
# -----------------------------------------------------------------------------
FROM eclipse-temurin:${JAVA_VERSION}-jdk-jammy AS builder
WORKDIR /workspace

# 1. Copy Gradle wrapper & settings (Layer 1: Thay đổi ít nhất)
COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .

# 2. Copy build scripts của các module (Layer 2: Thay đổi trung bình)
# Technical Note: Nếu có thêm module, cần thêm dòng COPY tương ứng ở đây
COPY common/build.gradle.kts common/
COPY core/build.gradle.kts core/
# Placeholder for additional modules:
# COPY moduleX/build.gradle.kts moduleX/

# 3. Download Dependencies (Cached nếu các file trên không đổi)
# Mẹo: Chạy task dependencies hoặc build dry-run để tải thư viện về
RUN ./gradlew build --no-daemon || true

# 4. Copy Source Code (Layer 3: Thay đổi thường xuyên nhất)
# Technical Note: Nếu có thêm module, cần thêm dòng COPY tương ứng ở đây
COPY common/src common/src
COPY core/src core/src
# Placeholder for additional modules:
# COPY moduleX/src moduleX/src

# 5. Nhận biến SERVICE để biết module nào cần build
ARG SERVICE
ENV SERVICE=${SERVICE}

# 6. Build JAR cho module đích
RUN ./gradlew :${SERVICE}:bootJar --no-daemon -x test

# 7. Extract JAR Layers (Spring Boot Optimization)

# -----------------------------------------------------------------------------
# Stage 2: Runtime - Secure & Lightweight
# -----------------------------------------------------------------------------
FROM eclipse-temurin:${JAVA_VERSION}-jre-jammy

ARG SERVICE # Nhận biến SERVICE để biết cần lấy JAR nào từ builder
ENV SERVICE=${SERVICE}

WORKDIR /app

# 1. Security: Tạo user non-root để chạy ứng dụng
RUN addgroup --system javauser && adduser --system --shell /bin/false --ingroup javauser javauser

# 2. Copy các lớp JAR đã được tách từ builder stage
COPY --from=builder /workspace/${SERVICE}/build/libs/${SERVICE}-*.jar ./app.jar

# 3. Thiết lập quyền và biến môi trường
USER javauser

ENTRYPOINT ["java","-jar","/app/app.jar"]
