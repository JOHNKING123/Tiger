FROM         johnking123/jdk8:1.0.1
MAINTAINER    zheng.cq

USER root

ARG JAR_FILE=undefined.jar
ARG DEFAULT_PORT=9183

ENV HTTP_PORT=${DEFAULT_PORT} \
    DATAS=/app/data

WORKDIR /app

run echo ${JAR_FILE}

COPY ${JAR_FILE} /app/app.jar

VOLUME $DATAS
EXPOSE $HTTP_PORT

ENTRYPOINT java -jar /app/app.jar