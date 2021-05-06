FROM node:9.11.1
COPY . /tech-test
WORKDIR /tech-test
RUN yarn install
ENTRYPOINT yarn start

# select parent image
FROM maven:3.6.3-jdk-8

# copy the source tree and the pom.xml to our new container
COPY ./ ./tech-test

# package our application code
RUN mvn clean package

# set the startup command to execute the jar
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]

FROM openjdk:8-jre-slim

# Add the jar with all the dependencies
COPY  target/selenium-docker-test.jar /usr/share/tag/selenium-docker-test.jar

# Add the suite xmls
COPY  ete-test.xml /usr/share/tag/e2e-test.xml

# Command line to execute the test
# Expects below ennvironment variables
# BROWSER = chrome / firefox
# MODULE  = order-module / search-module
# TYPE = local/remote

ENTRYPOINT /usr/bin/java -cp /usr/share/tag/selenium-docker-test.jar  -Dbrowser=$BROWSER -Dtype=$TYPE org.testng.TestNG /usr/share/tag/$MODULE