FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/translatetask-0.0.1-SNAPSHOT.jar translate-task.jar
ENTRYPOINT ["java","-jar","/translate-task.jar"]