FROM openjdk:17
WORKDIR /PersonalAccountingServer
EXPOSE 8080 4000
ADD ./build/libs/PersonalAccounting.jar .
CMD ["java", "-jar", "PersonalAccounting.jar"]