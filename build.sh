#!/usr//bin/bash
#打包项目
sbt stage
# 删除之前构建的同名容器(如果存在)
if [ "$(docker ps -aq -f name=square-root-api)" ]; then
    docker rm -f square-root-api
fi
# 删除之前构建的同名镜像(如果存在)
if [ "$(docker images -q square-root-api:1.0)" ]; then
    docker rmi square-root-api:1.0
fi
#构建镜像
docker buildx build -t square-root-api:1.0 .
#启动容器
docker run  -d -p 9001:9001 --name square-root-api square-root-api:1.0