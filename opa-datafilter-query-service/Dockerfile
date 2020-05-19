FROM adoptopenjdk/openjdk11:alpine-slim

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY build/libs/*.jar app.jar

EXPOSE 8727

ENTRYPOINT ["java","-jar","/app.jar"]