FROM fpxxkj/java8:321
MAINTAINER fpxxkj.com
ADD rpm-server-web-3.0.1.jar rpm-server-web.jar
ENV JAVA_OPTS = ""
ENTRYPOINT java ${JAVA_OPTS} -jar -Duser.timezone=GMT+8 /rpm-server-web.jar