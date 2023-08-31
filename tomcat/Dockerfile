FROM tomcat:10.1.13-jdk17-temurin-jammy

RUN mkdir -p /check

COPY clever-bank.war /usr/local/tomcat/webapps

EXPOSE 8080

CMD ["catalina.sh", "run"]