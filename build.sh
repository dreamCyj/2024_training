#!/usr//bin/bash
#打包项目
sbt stage

if [ "$(docker ps -aq -f name=square-root-api)" ]; then
    docker rm -f square-root-api
fi

if [ "$(docker images -q square-root-api:1.0)" ]; then
    docker rmi square-root-api:1.0
fi
#构建镜像
docker buildx build -t square-root-api:1.0 .
#启动容器
docker run  -d -p 9001:9001 --name square-root-api square-root-api:1.0