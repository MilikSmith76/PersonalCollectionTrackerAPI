FROM gradle:jdk24-alpine AS builder

WORKDIR /builder

COPY . /builder

RUN ./gradlew build

FROM amazoncorretto:24-alpine3.18 AS runner

WORKDIR /app

COPY --from=builder /builder/build/libs/PersonalCollectionTrackerAPI-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

RUN adduser --disabled-password --gecos "" -h /bin/bash appuser

USER appuser

ENTRYPOINT ["java","-jar","app.jar"]
