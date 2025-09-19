FROM amazoncorretto:24-alpine3.18

WORKDIR /app

COPY /build/libs/PersonalCollectionTrackerAPI-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

RUN adduser --disabled-password --gecos "" -h /bin/bash appuser

USER appuser

ENTRYPOINT ["java","-jar","app.jar"]
