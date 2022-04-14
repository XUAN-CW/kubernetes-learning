# FROM java:8
# FROM openjdk:8-jdk-alpine
# 测试时用这个，工具比较多
FROM openjdk:8u312-oraclelinux8
COPY springboot-connect-mysql-0.0.1-SNAPSHOT.jar app.jar
#docker run -e PARAMS="--spring.profiles.active=prod"
ENV PARAMS="--spring.profiles.active=test"
ENTRYPOINT ["/bin/sh","-c","java -jar app.jar ${PARAMS}"]