# spring-cloud-stream-kafka-multi-topics-cloudkarafka

The goal of this project is to implement a [`Spring Boot`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) application that _produces_ messages to a [`Kafka`](https://kafka.apache.org/) topic and another `Spring Boot` application that _consumes_ those messages.

Similar projects are: [`spring-kafka-de-serialization-types`](https://github.com/ivangfr/spring-kafka-de-serialization-types) and [`spring-cloud-stream-kafka-elasticsearch`](https://github.com/ivangfr/spring-cloud-stream-kafka-elasticsearch).

However, in this one, when the Spring Profile `cloudkarafka` is used, `producer` and `consumer` will connect to a `Kafka` that is located in a cloud-based messaging service called [`CloudKarafka`](https://www.cloudkarafka.com/). When running the applications with `default` profile, `producer` and `consumer` will connect to `Kafka` that is running locally in a `Docker` container.

## Examples

- ### [spring-kafka](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka/tree/master/spring-kafka#spring-cloud-stream-kafka-multi-topics-cloudkarafka)
- ### [spring-cloud-stream](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka/tree/master/spring-cloud-stream#spring-cloud-stream-kafka-multi-topics-cloudkarafka)

## Prerequisites

- [`Java 11+`](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [`Docker`](https://www.docker.com/)
- [`Docker-Compose`](https://docs.docker.com/compose/install/)

## Using CloudKarafka

### Configuration

- Access [`CloudKarafka`](https://www.cloudkarafka.com/) website

- Create an account. There is the `Developer Duck` plan that is totally free

- Once you log into `CloudKarafka`, click on `Details` menu. You should see something like the picture below. In this page you have all the credentials and the URLs of the `Kafka` brokers

  ![cloudkarafka-details](images/cloudkarafka-details.png)

- In order to get information about the topic you will use, click on the `Topics` menu

  ![cloudkarafka-topics](images/cloudkarafka-topics.png)

- You can use the topic with suffix `default` or create new ones. In my case, I created two: one with suffix `news.json` and another with suffix `alert.json`.

## Using Kafka running locally

### Start Environment

- Open a terminal and inside `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder run
  ```
  docker-compose up -d
  ```

- Wait until all containers are `running (healthy)`. You can check their status by running
  ```
  docker-compose ps
  ```

### Useful Links

- **Kafka Topics UI**
   
  `Kafka Topics UI` can be accessed at http://localhost:8085

- **Kafka Manager**
   
  `Kafka Manager` can be accessed at http://localhost:9000

  _Configuration_

  - First, you must create a new cluster. Click on `Cluster` (dropdown on the header) and then on `Add Cluster`
  - Type the name of your cluster in `Cluster Name` field, for example: `MyCluster`
  - Type `zookeeper:2181` in `Cluster Zookeeper Hosts` field
  - Enable checkbox `Poll consumer information (Not recommended for large # of consumers if ZK is used for offsets tracking on older Kafka versions)`
  - Click on `Save` button at the bottom of the page.

### Shutdown

To stop and remove docker-compose containers, network and volumes, go to a terminal and, inside `spring-cloud-stream-kafka-multi-topics-cloudkarafka`, run the command below
```
docker-compose down -v
```

## Cleanup

To remove the Docker images created by this project, go to a terminal and, inside `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder, run the following script
```
./remove-docker-images.sh
```

## References

- https://docs.spring.io/spring-kafka/reference/html/
- https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html