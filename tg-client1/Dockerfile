FROM         docker.io/fabric8/java-centos-openjdk8-jdk:latest
MAINTAINER    zheng.cq

USER root
RUN /sbin/ip route|awk '/default/ { print  $3,"\tconfig" }' >> /etc/hosts
RUN /sbin/ip route|awk '/default/ { print  $3,"\tdiscovery" }' >> /etc/hosts

ARG JAR_FILE=undefined.jar
ARG DEFAULT_PORT=9183

ENV HTTP_PORT=${DEFAULT_PORT} \
    DATAS=/app/data

WORKDIR /app

run echo ${JAR_FILE}

COPY ${JAR_FILE} /app/app.jar

VOLUME $DATAS
EXPOSE $HTTP_PORT

ENTRYPOINT java -jar /app/app.jar >> /app/run.log