FROM openjdk:21

WORKDIR /app

COPY . .

EXPOSE 8080

CMD ["java", "-cp", "out:lib/json-20230618.jar", "Main"]
