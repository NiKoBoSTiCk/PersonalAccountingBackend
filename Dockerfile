FROM openjdk:17
WORKDIR /PersonalAccountingServer
EXPOSE 8080
ADD ./build/libs/PersonalAccountingServer.jar .
CMD ["java", "-jar", "PersonalAccountingServer.jar"]