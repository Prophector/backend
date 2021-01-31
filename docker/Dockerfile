FROM openjdk:11-jdk

# Copy the app
COPY prophector-*.jar /opt/app/lib/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/app/lib/app.jar"]
