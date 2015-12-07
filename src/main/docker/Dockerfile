FROM java:8
MAINTAINER Stephen Harrison <stephen@harrison.org>
VOLUME /tmp
ADD modern-0.0.1-SNAPSHOT.war app.war
RUN bash -c 'touch /app.war'
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.war"]
