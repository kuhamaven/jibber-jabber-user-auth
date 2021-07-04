FROM gradle:7.0.2-jdk16
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble
EXPOSE 8002
ENTRYPOINT ["java","-jar","/home/gradle/src/build/libs/jibberjabber.users-auth-0.0.1-SNAPSHOT.jar"]