# spring-cloud-stream-kafka-multi-topics

The goal of this project is to create two applications: one as a [`Spring Boot`](https://spring.io/projects/spring-boot) producer and the other as a `Spring Boot` consumer. We'll be using [`Spring for Apache Kafka`](https://spring.io/projects/spring-kafka) and [`Spring Cloud Stream`](https://spring.io/projects/spring-cloud-stream).

Similar projects are: [`spring-kafka-de-serialization-types`](https://github.com/ivangfr/spring-kafka-de-serialization-types) and [`spring-cloud-stream-kafka-elasticsearch`](https://github.com/ivangfr/spring-cloud-stream-kafka-elasticsearch).

## Proof-of-Concepts & Articles

On [ivangfr.github.io](https://ivangfr.github.io), I have compiled my Proof-of-Concepts (PoCs) and articles. You can easily search for the technology you are interested in by using the filter. Who knows, perhaps I have already implemented a PoC or written an article about what you are looking for.

## Additional Readings

- \[**Medium**\] [**Implementing a Kafka Producer and Consumer using Spring Cloud Stream**](https://medium.com/@ivangfr/implementing-a-kafka-producer-and-consumer-using-spring-cloud-stream-d4b9a6a9eab1)
- \[**Medium**\] [**Implementing Unit Tests for a Kafka Producer and Consumer that uses Spring Cloud Stream**](https://medium.com/@ivangfr/implementing-unit-tests-for-a-kafka-producer-and-consumer-that-uses-spring-cloud-stream-f7a98a89fcf2)
- \[**Medium**\] [**Implementing End-to-End testing for a Kafka Producer and Consumer that uses Spring Cloud Stream**](https://medium.com/@ivangfr/implementing-end-to-end-testing-for-a-kafka-producer-and-consumer-that-uses-spring-cloud-stream-fbf5e666899e)
- \[**Medium**\] [**Configuring Distributed Tracing with Zipkin in a Kafka Producer and Consumer that uses Spring Cloud Stream**](https://medium.com/@ivangfr/configuring-distributed-tracing-with-zipkin-in-a-kafka-producer-and-consumer-that-uses-spring-cloud-9f1e55468b9e)
- \[**Medium**\] [**Using Cloudevents in a Kafka Producer and Consumer that uses Spring Cloud Stream**](https://medium.com/@ivangfr/using-cloudevents-in-a-kafka-producer-and-consumer-that-uses-spring-cloud-stream-9c51670b5566)
- \[**Medium**\] [**Running in Minikube (Kubernetes) a Kafka Producer and Consumer that uses Spring Cloud Stream**](https://medium.com/@ivangfr/running-in-minikube-kubernetes-a-kafka-producer-and-consumer-that-uses-spring-cloud-stream-d50b2dbfc5ea)

## Examples

- ### [spring-kafka](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics/tree/master/spring-kafka#spring-cloud-stream-kafka-multi-topics)
- ### [spring-cloud-stream](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics/tree/master/spring-cloud-stream#spring-cloud-stream-kafka-multi-topics)

## Prerequisites

- [`Java 17+`](https://www.oracle.com/java/technologies/downloads/#java17)
- [`Docker`](https://www.docker.com/)

## Start Environment

- Open a terminal and inside `spring-cloud-stream-kafka-multi-topics` root folder run:
  ```
  docker compose up -d
  ```

- Wait for Docker containers to be up and running. To check it, run:
  ```
  docker compose ps
  ```

- Create the Kafka topics used by the applications:
  ```
  ./create-kafka-topics.sh
  ```

## Useful Links

- **Kafdrop**

  `Kafdrop` can be accessed at http://localhost:9000

## Shutdown

To stop and remove docker compose containers, network and volumes, go to a terminal and, inside `spring-cloud-stream-kafka-multi-topics`, run the command below:
```
docker compose down -v
```

## Cleanup

To remove the Docker images created by this project, go to a terminal and, inside `spring-cloud-stream-kafka-multi-topics` root folder, run the following script:
```
./remove-docker-images.sh
```