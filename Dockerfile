# 构建前端项目
FROM node:18-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
# 清理缓存并重新安装依赖
RUN rm -rf node_modules && \
    npm cache clean --force && \
    npm install -g npm@10.2.4 && \
    npm install

# 强制安装指定版本的 esbuild
RUN npm install esbuild@0.17.19

COPY frontend/ .
RUN npm run build

# 构建后端项目
FROM maven:3.8.6-openjdk-8-slim AS backend-build
WORKDIR /app/backend

# 设置Maven和Java选项
ENV MAVEN_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn"
ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx512m -XX:+UseG1GC"

# 复制源代码
COPY pom.xml .
COPY src ./src

# 一次性构建，避免多次Maven调用
RUN ulimit -c unlimited && mvn clean package -DskipTests -Dmaven.test.skip=true --batch-mode --no-transfer-progress

# 最终运行阶段
FROM nginx:alpine
WORKDIR /app

# 复制前端构建产物
COPY --from=frontend-build /app/frontend/dist /usr/share/nginx/html

# 复制后端JAR包
COPY --from=backend-build /app/backend/target/*.jar /app/app.jar

# 复制nginx配置
COPY nginx.conf /etc/nginx/nginx.conf

# 安装OpenJDK 8和netcat
RUN apk add --no-cache openjdk8-jre netcat-openbsd

# 创建目录用于存放堆转储文件
RUN mkdir -p /app/logs && \
    chmod 777 /app/logs

# 暴露端口
EXPOSE 3000 8080

# 启动脚本
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

CMD ["/app/start.sh"]