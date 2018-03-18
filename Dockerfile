FROM openjdk:8
RUN mkdir -p /opt/app/sp
WORKDIR /opt/app/sp
ARG JAR_FILE
ADD ${JAR_FILE} /opt/app/sp/sn_fm.jar
ENTRYPOINT ["java","-jar","/opt/app/sp/sn_fm.jar"]
