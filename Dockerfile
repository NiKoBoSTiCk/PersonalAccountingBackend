FROM openjdk:17
WORKDIR /PersonalAccountingServer
EXPOSE 8080
ADD ./build/libs/PersonalAccountingBackend.jar .
CMD ["java", "-jar", "PersonalAccountingBackend.jar"]