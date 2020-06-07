# springboot-cloudkarafka
## `> spring-cloud-stream`

In this example, we use [`Spring Cloud Stream`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/) library to implement the configuration between `Spring Boot` applications and `Kafka`.

## Applications

- **producer-cloud-stream**

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news`. Once a request is made, `producer-cloud-stream` pushes a message about the `news` to Kafka.

  Endpoint
  ```
  POST /api/news {"source": "...", "title": "..."}
  ```

- **consumer-cloud-stream**

  `Spring Boot` Web Java application that listens to messages (published by the `producer-cloud-stream`) and logs it.

## Running applications using Maven

#### Using CloudKarafka

- **producer-cloud-stream**

  - Open a terminal and navigate to `springboot-cloudkarafka` root folder

  - Export your `CloudKarafka` credentials to those environment variables
    ```
    export CLOUDKARAFKA_USERNAME=...
    export CLOUDKARAFKA_PASSWORD=...
    ```
    
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-cloud-stream/producer-cloud-stream \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9082" \
      -Dspring-boot.run.profiles=cloudkarafka
    ```

- **consumer-cloud-stream**

  - Open another terminal and make sure you are in `springboot-cloudkarafka` root folder
  
  - Export your `CloudKarafka` credentials to those environment variables
    ```
    export CLOUDKARAFKA_USERNAME=...
    export CLOUDKARAFKA_PASSWORD=...
    ```
  
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9083" \
      -Dspring-boot.run.profiles=cloudkarafka
    ```

#### Using Kafka running locally

> **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/springboot-cloudkarafka#running-kafka-locally)  

- **producer-cloud-stream**

  - Open a terminal and navigate to `springboot-cloudkarafka` root folder
  
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-cloud-stream/producer-cloud-stream \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9082"
    ```

- **consumer-cloud-stream**

  - Open another terminal and make sure you are in `springboot-cloudkarafka` root folder
  
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9083"
    ```

## Running applications as Docker containers

### Build applications Docker images

- In a terminal, make sure you are in `springboot-cloudkarafka` root folder

- Build **producer-cloud-stream** Docker image
  ```
  ./mvnw clean compile jib:dockerBuild -DskipTests --projects spring-cloud-stream/producer-cloud-stream
  ```
  
  | Environment Variable     | Description |
  | -----------------------  | ----------- |
  | `SPRING_PROFILES_ACTIVE` | Specify the type of profile to run the application. To use `CloudKarafka` set `cloudkarafka`. The `default` profile will use local `Kafka` |
  | `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value for `cloudkarafka` profile is `ark-01.srvs.cloudkafka.com:9094, ark-02.srvs.cloudkafka.com:9094, ark-03.srvs.cloudkafka.com:9094`. Using the `default` profile, the default value is `localhost:29092` |
  | `CLOUDKARAFKA_USERNAME`  | Specify your `CloudKarafka` username. Required when using `cloudkarafka` profile |
  | `CLOUDKARAFKA_PASSWORD`  | Specify your `CloudKarafka` password. Required when using `cloudkarafka` profile |

- Build **consumer-cloud-stream** Docker image
  ```
  ./mvnw clean compile jib:dockerBuild -DskipTests --projects spring-cloud-stream/consumer-cloud-stream
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

- Run **producer-cloud-stream**
  ```
  docker run -d --rm --name producer-cloud-stream -p 9082:8080 \
    -e SPRING_PROFILES_ACTIVE=cloudkarafka \
    -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
    -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
    docker.mycompany.com/producer-cloud-stream:1.0.0
  ```

- Run **consumer-cloud-stream**
  ```
  docker run -d --rm --name consumer-cloud-stream -p 9083:8080 \
    -e SPRING_PROFILES_ACTIVE=cloudkarafka \
    -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
    -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
    docker.mycompany.com/consumer-cloud-stream:1.0.0
  ```

#### Using Kafka running locally

> **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/springboot-cloudkarafka#running-kafka-locally)

- Run **producer-cloud-stream**
  ```
  docker run -d --rm --name producer-cloud-stream \
    -p 9082:8080 -e KAFKA_URL=kafka:9092 \
    --network springboot-cloudkarafka_default \
    docker.mycompany.com/producer-cloud-stream:1.0.0
  ```

- Run **consumer-cloud-stream**
  ```
  docker run -d --rm --name consumer-cloud-stream \
    -p 9083:8080 -e KAFKA_URL=kafka:9092 \
    --network springboot-cloudkarafka_default \
    docker.mycompany.com/consumer-cloud-stream:1.0.0
  ```

## Applications URLs

| Application           | URL                   |
| --------------------- | --------------------- |
| producer-cloud-stream | http://localhost:9082 |
| consumer-cloud-stream | http://localhost:9083 |

## Example of execution using CloudKarafka

> **Note:** In the call below, I am using [HTTPie](https://httpie.org/)

- In a terminal, post a news
  ```
  http :9082/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
  ```

  **producer-cloud-stream** logs
  ```
  INFO 5090 --- [nio-9082-exec-1] c.m.p.kafka.NewsProducer : Sending News 'News(id=04253f40-ff8e-4293-91ba-a570febda5e1, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)' to topic '2gxxxxxx-news.json'
  ```

  **consumer-cloud-stream** logs
  ```
  INFO 5066 --- [container-0-C-1] c.m.c.kafka.NewsConsumer : Received message
  ---
  TOPIC: 2gxxxxxx-news.json; PARTITION: 0; OFFSET: 1;
  PAYLOAD: News(id=04253f40-ff8e-4293-91ba-a570febda5e1, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)
  ---
  ```

## Stop applications

- If they were started with `Maven`, go to the terminals where they are running and press `Ctrl+C`

- If they were started as Docker containers, run the command below
  ```
  docker stop producer-cloud-stream consumer-cloud-stream
  ```
