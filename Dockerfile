FROM eclipse-temurin:17
RUN mkdir /opt/app
# ssh setup
RUN mkdir /root/.ssh
COPY live_repl_key.pub /root/.ssh/authorized_keys
RUN chmod 600 /root/.ssh/authorized_keys

COPY entrypoint.sh /opt/app/entrypoint.sh
RUN apt update -y && apt install -y openssh-server

COPY target/sample-standalone.jar /opt/app
COPY nrepl_tls.keys /opt/app
EXPOSE 5553 5554 5555 5556 22

WORKDIR /opt/app
CMD ["./entrypoint.sh"]
