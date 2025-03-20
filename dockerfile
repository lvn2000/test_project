FROM sbtscala/scala-sbt:eclipse-temurin-alpine-21.0.2_13_1.10.2_2.13.14
ARG project_path
ENV project_path=${project_path}
WORKDIR $project_path
CMD ["sbt", "run"]
