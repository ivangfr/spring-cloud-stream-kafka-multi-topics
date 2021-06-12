#!/usr/bin/env bash

if [ "$1" = "cloudkarafka" ];
then

  if [ -z "$CLOUDKARAFKA_USERNAME" ] || [ -z "$CLOUDKARAFKA_PASSWORD" ];
  then
    echo "WARNING: export to CLOUDKARAFKA_USERNAME and CLOUDKARAFKA_PASSWORD environment variables yous CloudKarafka credentials!"
    exit 1
  fi

  docker run -d --rm --name producer-kafka -p 9080:8080 \
    -e SPRING_PROFILES_ACTIVE=cloudkarafka \
    -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
    -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
    ivanfranchin/producer-kafka:1.0.0

  docker run -d --rm --name consumer-kafka -p 9081:8080 \
    -e SPRING_PROFILES_ACTIVE=cloudkarafka \
    -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
    -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
    ivanfranchin/consumer-kafka:1.0.0

else

  docker run -d --rm --name producer-kafka -p 9080:8080 \
    -e KAFKA_URL=kafka:9092 \
    --network springboot-cloudkarafka_default \
    ivanfranchin/producer-kafka:1.0.0

  docker run -d --rm --name consumer-kafka -p 9081:8080 \
    -e KAFKA_URL=kafka:9092 \
    --network springboot-cloudkarafka_default \
    ivanfranchin/consumer-kafka:1.0.0

fi
