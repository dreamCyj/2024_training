FROM openjdk:8-jdk-alpine

WORKDIR /opt/square-root-api/
COPY ./target/universal/stage/  /opt/square-root-api/

EXPOSE 9001

ENTRYPOINT ["/opt/square-root-api/bin/squarerootapi", "-J-server", "-J-Xms256M", "-J-Xmx512M", "-J-XX:+UseG1GC", "-Dconfig.file=conf/application.conf", "-DapplyEvolutions.default=false", "-Dhttp.port=9001"]

