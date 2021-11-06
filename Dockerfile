FROM openjdk:11
RUN mkdir /usr/src/myapp
COPY build/libs/epam_module4_authentication_and_spring_security.jar /usr/src/myapp
WORKDIR /usr/src/myapp
EXPOSE 8081
CMD ["java", "-jar","-Ddatasource.url=jdbc:postgresql://postgres:5432/rest_api_basics_db","epam_module4_authentication_and_spring_security.jar"]