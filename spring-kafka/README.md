# springboot-cloudkarafka
## `> spring-kafka`

In this example, we use [`Spring Kafka`](https://docs.spring.io/spring-kafka/reference/html/) library to implement the configuration between `Spring Boot` applications and `Kafka`.

## Applications

- **producer-kafka**

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news`. Once a request is made, `producer-kafka` pushes a message about the `news` to Kafka.

  Endpoint
  ```
  POST /api/news {"source": "...", "title": "..."}
  ```

- **consumer-kafka**

  `Spring Boot` Web Java application that listens to messages (published by the `producer-kafka`) and logs it.

## Running applications using Maven

#### Using CloudKarafka

- **producer-kafka**

  - Open a terminal and navigate to `springboot-cloudkarafka` root folder

  - Export your `CloudKarafka` credentials to those environment variables
    ```
    export CLOUDKARAFKA_USERNAME=...
    export CLOUDKARAFKA_PASSWORD=...
    ```
    
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-kafka/producer-kafka \
    -Dspring-boot.run.jvmArguments="-Dserver.port=9080" \
    -Dspring-boot.run.profiles=cloudkarafka
    ```

- **consumer-kafka**

  - Open another terminal and make sure you are in `springboot-cloudkarafka` root folder
  
  - Export your `CloudKarafka` credentials to those environment variables
    ```
    export CLOUDKARAFKA_USERNAME=...
    export CLOUDKARAFKA_PASSWORD=...
    ```
  
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-kafka/consumer-kafka \
    -Dspring-boot.run.jvmArguments="-Dserver.port=9081" \
    -Dspring-boot.run.profiles=cloudkarafka
    ```

#### Using Kafka running locally

> **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/springboot-cloudkarafka#running-kafka-locally)  

- **producer-kafka**

  - Open a terminal and navigate to `springboot-cloudkarafka` root folder
  
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-kafka/producer-kafka \
    -Dspring-boot.run.jvmArguments="-Dserver.port=9080"
    ```

- **consumer-kafka**

  - Open another terminal and make sure you are in `springboot-cloudkarafka` root folder
  
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-kafka/consumer-kafka \
    -Dspring-boot.run.jvmArguments="-Dserver.port=9081"
    ```

## Running applications as Docker containers

### Build applications Docker images

- In a terminal, make sure you are in `springboot-cloudkarafka` root folder

- Build **producer-kafka** Docker image
  ```
  ./mvnw clean package dockerfile:build -DskipTests --projects spring-kafka/producer-kafka
  ```
  
  | Environment Variable     | Description |
  | -----------------------  | ----------- |
  | `SPRING_PROFILES_ACTIVE` | Specify the type of profile to run the application. To use `CloudKarafka` set `cloudkarafka`. The `default` profile will use local `Kafka` |
  | `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value for `cloudkarafka` profile is `ark-01.srvs.cloudkafka.com:9094, ark-02.srvs.cloudkafka.com:9094, ark-03.srvs.cloudkafka.com:9094`. Using the `default` profile, the default value is `localhost:29092` |
  | `CLOUDKARAFKA_USERNAME`  | Specify your `CloudKarafka` username. Required when using `cloudkarafka` profile |
  | `CLOUDKARAFKA_PASSWORD`  | Specify your `CloudKarafka` password. Required when using `cloudkarafka` profile |

- Build **consumer-kafka** Docker image
  ```
  ./mvnw clean package dockerfile:build -DskipTests --projects spring-kafka/consumer-kafka
  ```

  | Environment Variable     | Description |
  | ------------------------ | ----------- |
  | `SPRING_PROFILES_ACTIVE` | Specify the type of profile to run the application. To use `CloudKarafka` set `cloudkarafka`. The `default` will use local `Kafka` |
  | `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value for `karafka` profile is `ark-01.srvs.cloudkafka.com:9094, ark-02.srvs.cloudkafka.com:9094, ark-03.srvs.cloudkafka.com:9094`. Using the `default` profile, the default value is `localhost:29092` |
  | `CLOUDKARAFKA_USERNAME`  | Specify your `CloudKarafka` username. Required when using `cloudkarafka` profile |
  | `CLOUDKARAFKA_PASSWORD`  | Specify your `CloudKarafka` password. Required when using `cloudkarafka` profile |

### Starting applications Docker containers

#### Using CloudKarafka

- In a terminal, export your `CloudKarafka` credentials to those environment variables
  ```
  export CLOUDKARAFKA_USERNAME=...
  export CLOUDKARAFKA_PASSWORD=...
  ```

- Run **producer-kafka**
  ```
  docker run -d --rm --name producer-kafka -p 9080:8080 \
  -e SPRING_PROFILES_ACTIVE=cloudkarafka \
  -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
  docker.mycompany.com/producer-kafka:1.0.0
  ```

- Run **consumer-kafka**
  ```
  docker run -d --rm --name consumer-kafka -p 9081:8080 \
  -e SPRING_PROFILES_ACTIVE=cloudkarafka \
  -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
  docker.mycompany.com/consumer-kafka:1.0.0
  ```

#### Using Kafka running locally

> **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/springboot-cloudkarafka#running-kafka-locally)

- Run **producer-kafka**
  ```
  docker run -d --rm --name producer-kafka -p 9080:8080 \
  --network springboot-cloudkarafka_default -e KAFKA_URL=kafka:9092 \
  docker.mycompany.com/producer-kafka:1.0.0
  ```

- Run **consumer-kafka**
  ```
  docker run -d --rm --name consumer-kafka -p 9081:8080 \
  --network springboot-cloudkarafka_default -e KAFKA_URL=kafka:9092 \
  docker.mycompany.com/consumer-kafka:1.0.0
  ```

## Applications URLs

| Application    | URL                   |
| -------------- | --------------------- |
| producer-kafka | http://localhost:9080 |
| consumer-kafka | http://localhost:9081 |

## Example of execution using CloudKarafka

> **Note:** In the call below, I am using [HTTPie](https://httpie.org/)

- In a terminal, post a news
  ```
  http :9080/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
  ```

  **producer-kafka** logs
  ```
  INFO 6818 --- [nio-9080-exec-1] c.m.producerkafka.kafka.NewsProducer : Sending News 'News(id=17d3ddb1-ec35-449f-89b2-4e93ddb5e88b, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)' to topic '2gxxxxxx-news.json'
  ```

  **consumer-kafka** logs
  ```
  INFO 6834 --- [ntainer#0-0-C-1] c.m.consumerkafka.kafka.NewsConsumer : Received message
  ---
  TOPIC: 2gxxxxxx-news.json; PARTITION: 0; OFFSET: 2;
  PAYLOAD: News(id=17d3ddb1-ec35-449f-89b2-4e93ddb5e88b, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)
  ---
  ```

## Stop applications

- If they were started with `Maven`, go to the terminals where they are running and press `Ctrl+C`

- If they were started as Docker containers, run the command below
  ```
  docker stop producer-kafka consumer-kafka
  ```
