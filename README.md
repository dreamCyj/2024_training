# docker容器部署
1. 执行build.sh
2. windows浏览器访问localhost:9001/lowLevel/5


# 直接部署在linux
1. 将start.sh和target/universal/squarerootapi-1.0-SNAPSHOT.zip 上传到linux环境中(虚拟机/opt/square-root-api/目录下)
2. 执行命令
   ```
   chmod 777 start.sh 
   ./start.sh squarerootapi-1.0-SNAPSHOT.zip
   ```
3. 虚拟机内打开浏览器 输入localhost:9001 即可访问 或者主机访问虚拟机IP:9001 例192.168.200.130:9001
4. 访问192.168.200.130:9001/lowLevel/5  或者192.168.200.130:9001/highLevel/5