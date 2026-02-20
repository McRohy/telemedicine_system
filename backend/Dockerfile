#https://spring.io/guides/gs/spring-boot-docker
#https://medium.com/@bhargavkanjarla01/how-to-combine-a-java-spring-boot-back-end-with-a-reactjs-front-end-app-ed8d8ca65285
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
RUN cp target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]