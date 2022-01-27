FROM openjdk:17
ARG APP_NAME="MailWorker"
ARG APP_PORT=8086

WORKDIR    /usr/local/app
ENV ARTIFACT="${APP_NAME}.jar"
ENV JAVA_OPTS=""
ENV MAIL_WORKER_DB_URL jdbc:sqlserver://ee-db:1433;databaseName=MailWorker
ENV DB_USERNAME gttrading
ENV DB_PASSWORD 1895arcH
ENV REDIS_URL redis
ENV REDIS_PORT 6379
ENV REDIS_PASSWORD P@ssword247!
ENV KAFKA_SERVER_URL=broker:29092
#ENV KAFKA_USERNAME=RBJODQ6FBWHIHPYK
#ENV KAFKA_PASSWORD_KEY=k19vrveDLEKSwypAuXtPxZxkf6bZijvDD/tSRmxPs7zRcdYc1nTI9zGQZ2PAmsP0
ENV KAFKA_SCHEMA_REGISTRY_URL=http://schema-registry:8081
ENV MAIN_DB_ACCESS_LAYER_API_URL=http://dataaccess:7130
ENV MAIL_WORKER_PROJECT_NAME MailWorker

COPY target/${APP_NAME}.jar /usr/local/app
EXPOSE ${APP_PORT}
RUN set -x \
    && mkdir -p /usr/local/app/log \
    && useradd -u 1000 -G 0 -M ee \
    && ln -sf /dev/stdout /usr/local/app/log/application.log \
    && ln -sf /dev/stderr /usr/local/app/log/error.log \
    && chown -R 1000:1000 /usr/local/app
USER 1000

CMD java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8087  \
    ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom \
    -jar -Dspring.config.location=classpath:/application-dev.properties ${ARTIFACT}
