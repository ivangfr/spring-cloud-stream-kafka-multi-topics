#!/usr/bin/env bash

echo
echo "Create topic spring.kafka.news"
echo "------------------------------"
docker exec -t zookeeper kafka-topics --create --bootstrap-server kafka:9092 --replication-factor 1 --partitions 5 --topic spring.kafka.news

echo
echo "Create topic spring.kafka.alert"
echo "-------------------------------"
docker exec -t zookeeper kafka-topics --create --bootstrap-server kafka:9092 --replication-factor 1 --partitions 5 --topic spring.kafka.alert

echo
echo "Create topic spring.cloud.stream.news"
echo "-------------------------------------"
docker exec -t zookeeper kafka-topics --create --bootstrap-server kafka:9092 --replication-factor 1 --partitions 5 --topic spring.cloud.stream.news

echo
echo "Create topic spring.cloud.stream.alert"
echo "--------------------------------------"
docker exec -t zookeeper kafka-topics --create --bootstrap-server kafka:9092 --replication-factor 1 --partitions 5 --topic spring.cloud.stream.alert

echo
echo "List topics"
echo "-----------"
docker exec -t zookeeper kafka-topics --list --bootstrap-server kafka:9092