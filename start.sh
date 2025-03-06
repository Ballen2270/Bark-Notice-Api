#!/bin/sh

# 设置系统限制
ulimit -c unlimited

# 设置JVM参数
JAVA_OPTS="-Xms512m -Xmx1024m \
    -XX:MetaspaceSize=128m \
    -XX:MaxMetaspaceSize=256m \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=200 \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/app/heapdump.hprof \
    -XX:+DisableExplicitGC \
    -XX:+UseStringDeduplication \
    -Djava.security.egd=file:/dev/./urandom \
    -Duser.timezone=GMT+08"

# 启动Java应用
echo "Starting backend service..."
java $JAVA_OPTS \
     -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
     -jar /app/app.jar &

# 等待后端服务启动
echo "Waiting for backend service to start..."
while ! nc -z 127.0.0.1 8080; do
  sleep 1
done
echo "Backend service started successfully"

# 启动nginx（前台运行）
echo "Starting nginx..."
nginx -g 'daemon off;'

