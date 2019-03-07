FROM         johnking123/jdk8:1.0.1
MAINTAINER    zheng.cq

ARG JAR_FILE=./target/tg-config-1.0-SNAPSHOT.jar
ARG DEFAULT_PORT=9181

ENV HTTP_PORT=${DEFAULT_PORT} \
    DATAS=/app/data

WORKDIR /app

run echo ${JAR_FILE}

COPY ${JAR_FILE} /app/app.jar

VOLUME $DATAS
EXPOSE $HTTP_PORT

CMD ["java", "-jar", "/app/app.jar"]