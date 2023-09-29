#!/usr/bin/env bash

# start ssh in daemon mode
/usr/sbin/sshd -D

java -jar sample-standalone.jar
