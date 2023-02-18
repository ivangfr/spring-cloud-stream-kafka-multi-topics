#!/usr/bin/env bash

if [ "$1" = "native" ];
then
  ./mvnw clean -Pnative spring-boot:build-image --projects spring-cloud-stream/producer-cloud-stream -DskipTests
  ./mvnw clean -Pnative spring-boot:build-image --projects spring-cloud-stream/consumer-cloud-stream -DskipTests
else
  ./mvnw clean compile jib:dockerBuild --projects spring-cloud-stream/producer-cloud-stream
  ./mvnw clean compile jib:dockerBuild --projects spring-cloud-stream/consumer-cloud-stream
fi
