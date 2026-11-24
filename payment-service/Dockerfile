FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /src
COPY common/src/main/java ./common
COPY payment-service/src/main/java ./service
RUN mkdir -p /out && \
    javac --release 21 -d /out $(find ./common ./service -name "*.java") && \
    jar cfe /app.jar com.hypermesh.payment.PaymentServiceApplication -C /out .

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app.jar /app/app.jar
USER 1000:1000
EXPOSE 8080
HEALTHCHECK --interval=5s --timeout=3s --retries=12 CMD curl -fsS http://localhost:8080/health || exit 1
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=65.0", "-XX:TieredStopAtLevel=1", "-Djava.io.tmpdir=/tmp", "-jar", "/app/app.jar"]
