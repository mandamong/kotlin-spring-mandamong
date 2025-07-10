FROM amazoncorretto:17
LABEL authors="kyuwon jeong"

COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
