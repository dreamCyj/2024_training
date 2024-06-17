unzip $1
mv /opt/square-root-api/squarerootapi-1.0-SNAPSHOT/*  /opt/square-root-api
cp /opt/square-root-api/conf/application.conf /opt/square-root-api/application_prod.conf
nohup /opt/square-root-api/bin/squarerootapi -J-server -J-Xms256M -J-Xmx512M -J-XX:+UseG1GC -Dconfig.file=application_prod.conf -DapplyEvolutions.default=false -Dhttp.port=9001 &

