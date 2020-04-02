 FROM java:8
 COPY cykb-1.0.0-SNAPSHOT.jar /cykb-server/cykb-server.jar
 EXPOSE 8012
 CMD ["java", "-jar", "/cykb-server/cykb-server.jar"]
 RUN echo "Asia/Shanghai" > /etc/timezone
 ENV LANG C.UTF-8