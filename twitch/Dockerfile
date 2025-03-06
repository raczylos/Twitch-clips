FROM maven:3.9.9-eclipse-temurin-21-alpine
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -T 4 dependency:go-offline
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]