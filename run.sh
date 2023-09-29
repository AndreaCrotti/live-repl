#!/usr/bin/env bash

MODE=$1

if [ "$MODE" == "docker" ]
then
    clj -T:build uber
    docker compose build && docker compose up
elif [ "$MODE" == "jar" ]
then
    clj -T:build uber
    java -jar target/sample-standalone.jar
else
    clj -X main/-main
fi
