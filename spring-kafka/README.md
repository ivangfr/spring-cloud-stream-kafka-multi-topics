# spring-cloud-stream-kafka-multi-topics
## `> spring-kafka`

In this example, we use [`Spring for Apache Kafka`](https://docs.spring.io/spring-kafka/reference/index.html) dependency to implement the configuration between `Spring Boot` applications and `Kafka`.

## Applications

- ### producer-kafka

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news` or `alert`. Once a request is made, `producer-kafka` pushes a message related to the `news` or `alert` to Kafka.

  Endpoints:
  ```
  POST /api/news {"source":"...", "title":"..."}
  POST /api/alerts {"level":"...", "message":"..."}
  ```

- ### consumer-kafka

  `Spring Boot` Web Java application that listens to the messages (published by `producer-kafka`) and logs it.

## Running applications using Maven

> **Note**: you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics#start-environment)  

- **producer-kafka**

  - In a terminal, make sure you are in the `spring-cloud-stream-kafka-multi-topics` root folder:
  
  - Run application:
    ```
    ./mvnw clean spring-boot:run --projects spring-kafka/producer-kafka \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9080"
    ```

- **consumer-kafka**

  - Open a new terminal and navigate to the `spring-cloud-stream-kafka-multi-topics` root folder:
  
  - Run application:
    ```
    ./mvnw clean spring-boot:run --projects spring-kafka/consumer-kafka \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9081"
    ```

## Running applications as Docker containers

- ### Build application's Docker image

  - In a terminal, make sure you are in the `spring-cloud-stream-kafka-multi-topics` root folder:

  - Run the following script to build the Docker images:
    ```
    ./build-docker-images-spring-kafka.sh
    ```

- ### Application's Environment Variables

  - **producer-kafka** and **consumer-kafka**

    | Environment Variable     | Description                                                                                 |
    |--------------------------|---------------------------------------------------------------------------------------------|
    | `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value is `localhost:29092` |

- ### Starting application's Docker container

  > **Note**: you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics#start-environment)

  - **producer-kafka**

    In a terminal, run the command below to start the Docker container:
    ```
    docker run --rm --name producer-kafka -p 9080:8080 \
      -e KAFKA_URL=kafka:9092 \
      --network spring-cloud-stream-kafka-multi-topics_default \
      ivanfranchin/producer-kafka:1.0.0
    ```

  - **consumer-kafka**

    Open a new terminal and run the command below to start the Docker container:
    ```
    docker run --rm --name consumer-kafka -p 9081:8080 \
      -e KAFKA_URL=kafka:9092 \
      --network spring-cloud-stream-kafka-multi-topics_default \
      ivanfranchin/consumer-kafka:1.0.0
    ```

## Playing around

In a terminal, submit the following POST requests to `producer-kafka` and check its logs and `consumer-kafka` logs:

> **Note**: [HTTPie](https://httpie.org/) is being used in the calls bellow 

- **news**
  ```
  http :9080/api/news source="Spring Boot Blog" title="Spring Boot and Apache Kafka"
  ```

- **alerts**
  ```
  http :9080/api/alerts level=4 message="Tsunami is coming"
  ```

## Stop applications

Go to the terminals where they are running and press `Ctrl+C`.

## Running Test Cases

In a terminal, make sure you are inside the `spring-cloud-stream-kafka-multi-topics` root folder:

- **producer-kafka**
  ```
  ./mvnw clean test --projects spring-kafka/producer-kafka
  ```

- **consumer-kafka**
  ```
  ./mvnw clean test --projects spring-kafka/consumer-kafka
  ```

## Cleanup

To remove the Docker images created by this example, go to a terminal and run the following commands:
```
docker rmi ivanfranchin/producer-kafka:1.0.0
docker rmi ivanfranchin/consumer-kafka:1.0.0
```
