#!/usr/bin/env bash

if [ "$1" = "cloudkarafka" ];
then

  if [ -z "$CLOUDKARAFKA_USERNAME" ] || [ -z "$CLOUDKARAFKA_PASSWORD" ];
  then
    echo "WARNING: export to CLOUDKARAFKA_USERNAME and CLOUDKARAFKA_PASSWORD environment variables yous CloudKarafka credentials!"
    exit 1
  fi

  docker run -d --rm --name producer-cloud-stream -p 9082:8080 \
    -e SPRING_PROFILES_ACTIVE=cloudkarafka \
    -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
    -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
    ivanfranchin/producer-cloud-stream:1.0.0

  docker run -d --rm --name consumer-cloud-stream -p 9083:8080 \
    -e SPRING_PROFILES_ACTIVE=cloudkarafka \
    -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
    -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
    ivanfranchin/consumer-cloud-stream:1.0.0

else

  docker run -d --rm --name producer-cloud-stream -p 9082:8080 \
    -e KAFKA_URL=kafka:9092 \
    --network spring-cloud-stream-kafka-multi-topics-cloudkarafka_default \
    ivanfranchin/producer-cloud-stream:1.0.0

  docker run -d --rm --name consumer-cloud-stream -p 9083:8080 \
    -e KAFKA_URL=kafka:9092 \
    --network spring-cloud-stream-kafka-multi-topics-cloudkarafka_default \
    ivanfranchin/consumer-cloud-stream:1.0.0

fi
