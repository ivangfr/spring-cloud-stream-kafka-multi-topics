# spring-cloud-stream-kafka-multi-topics
## `> spring-cloud-stream`

In this example, we use the [`Spring Cloud Stream`](https://docs.spring.io/spring-cloud-stream/reference/) dependency to configure communication between `Spring Boot` applications and `Kafka`.

## Applications

- ### producer-cloud-stream

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news` or `alert`. Once a request is made, `producer-cloud-stream` pushes a message containing the `news` or `alert` information to Kafka.

  Endpoints:
  ```text
  POST /api/news {"source":"...", "title":"..."}
  POST /api/alerts {"level":"...", "message":"..."}
  ```

- ### consumer-cloud-stream

  `Spring Boot` Web Java application that listens to the messages (published by `producer-cloud-stream`) and logs it.

## Running applications using Maven

> **Note**: you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics#start-environment)  

- **producer-cloud-stream**

  - In a terminal, make sure you are in the `spring-cloud-stream-kafka-multi-topics` root folder:
  
  - Run the Maven command below to start the application:
    ```bash
    ./mvnw clean spring-boot:run --projects spring-cloud-stream/producer-cloud-stream \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9082"
    ```

- **consumer-cloud-stream**

  - Open a new terminal and navigate to the `spring-cloud-stream-kafka-multi-topics` root folder:
  
  - Run the Maven command below to start the application:
    ```bash
    ./mvnw clean spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9083"
    ```

## Running applications as Docker containers

- ### Build application's Docker image

  - In a terminal, make sure you are in the `spring-cloud-stream-kafka-multi-topics` root folder:

  - Run the following script to build the Docker images:
    ```bash
    ./build-docker-images-spring-cloud-stream.sh
    ```

- ### Application's Environment Variables

  - **producer-cloud-stream** and **consumer-cloud-stream**

    | Environment Variable     | Description                                                                                  |
    |--------------------------|----------------------------------------------------------------------------------------------|
    | `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value is `localhost:29092`  |

- ### Starting application's Docker container

  > **Note**: you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics#start-environment)

  - **producer-kafka**

    In a terminal, run the command below to start the Docker container:
    ```bash
    docker run --rm --name producer-cloud-stream -p 9082:8080 \
      -e KAFKA_URL=kafka:9092 \
      --network spring-cloud-stream-kafka-multi-topics_default \
      ivanfranchin/producer-cloud-stream:1.0.0
    ```

  - **consumer-kafka**

    Open a new terminal and run the command below to start the Docker container:
    ```bash
    docker run --rm --name consumer-cloud-stream -p 9083:8080 \
      -e KAFKA_URL=kafka:9092 \
      --network spring-cloud-stream-kafka-multi-topics_default \
      ivanfranchin/consumer-cloud-stream:1.0.0
    ```

## Playing around

In a terminal, submit the following POST requests to `producer-cloud-stream` and check its logs as well as the `consumer-cloud-stream` logs:

> **Note**: [HTTPie](https://httpie.org/) is being used in the calls bellow

- **news**
  ```bash
  http :9082/api/news source="Spring Boot Blog" title="Spring Boot and Apache Kafka"
  ```
  
- **alerts**
  ```bash
  http :9082/api/alerts level=4 message="Tsunami is coming"
  ```

## Stop applications

Go to the terminals where they are running and press `Ctrl+C`.

## Running Test Cases

In a terminal, make sure you are inside the `spring-cloud-stream-kafka-multi-topics` root folder:

- **producer-cloud-stream**
  ```bash
  ./mvnw clean test --projects spring-cloud-stream/producer-cloud-stream
  ```

- **consumer-cloud-stream**
  ```bash
  ./mvnw clean test --projects spring-cloud-stream/consumer-cloud-stream
  ```

## Cleanup

To remove the Docker images created by this example, go to a terminal and run the following commands:
```bash
docker rmi ivanfranchin/producer-cloud-stream:1.0.0
docker rmi ivanfranchin/consumer-cloud-stream:1.0.0
```
