PORT=9001
PID=$(lsof -t -i :$PORT)
if [ -n "$PID" ]; then
  echo "Killing process $PID on port $PORT"
  kill -9 $PID
else
  echo "No process found on port $PORT"
fi
unzip $1
mv squarerootapi-1.0-SNAPSHOT/*  /opt/square-root-api
cp /opt/square-root-api/conf/application.conf /opt/square-root-api/application_prod.conf
nohup /opt/square-root-api/bin/squarerootapi -J-server -J-Xms256M -J-Xmx512M -J-XX:+UseG1GC -Dconfig.file=application_prod.conf -DapplyEvolutions.default=false -Dhttp.port=9001 &

