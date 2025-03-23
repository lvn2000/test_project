FROM openjdk:24-jre-slim
WORKDIR /app
COPY target/docker/stage/opt/docker /app
ENTRYPOINT ["bin/test_project"]
