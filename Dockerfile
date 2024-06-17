
FROM openjdk:8-jdk-alpine
WORKDIR /opt/square-root-api/
RUN apk add --no-cache bash

COPY ./target/universal/squarerootapi-*.zip squarerootapi-1.0-SNAPSHOT.zip
COPY ./start_docker.sh start.sh
RUN chmod 777 start.sh

EXPOSE 9001
# 运行Play应用
CMD ["bash", "-c", "./start.sh squarerootapi-1.0-SNAPSHOT.zip"]