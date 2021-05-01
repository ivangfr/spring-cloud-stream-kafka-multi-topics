#!/usr/bin/env bash

if [ "$1" = "native" ];
then
  ./mvnw clean spring-boot:build-image --projects spring-kafka/producer-kafka
  ./mvnw clean spring-boot:build-image --projects spring-kafka/consumer-kafka
else
  ./mvnw clean package jib:dockerBuild --projects spring-kafka/producer-kafka
  ./mvnw clean package jib:dockerBuild --projects spring-kafka/consumer-kafka
fi
