FROM openjdk:17
EXPOSE 8080
ADD target/betterreads.jar betterreads.jar
ENTRYPOINT ["java","-jar","/springboot-images-new.jar"]