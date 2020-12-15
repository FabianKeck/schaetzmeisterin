FROM openjdk:15-oracle

MAINTAINER Fabian Keck <fabian.keck@posteo.de>

ENV ENVIRONMENT=prod

ADD backend/target/schaetzmeisterin.jar app.jar

CMD ["sh" , "-c", "java -jar -Dserver.port=$PORT -Djwt.secretkey=$JWT_SECRETKEY -Dspring.data.mongodb.uri=$MONGO_DB_URI app.jar"]