# `springboot-cloudkarafka`
# `> spring-kafka`

In this example, we use [`Spring Kafka`](https://docs.spring.io/spring-kafka/reference/html/) library to implement the
configuration between `Spring Boot` applications and `Kafka`.

## Microservices

### producer-kafka

Spring Boot Web Java application that exposes one endpoint at which users can post `news`. Once a request is made,
`producer-kafka` pushes a message about the `news` to Kafka.

Endpoint
```
POST /api/news {"source": "...", "title": "..."}
```

### consumer-kafka

Spring Boot Web Java application that listens to messages (published by the `producer-kafka`) and logs it.

## Build Docker images

### producer-kafka

In a terminal and inside `springboot-cloudkarafka` root folder, run the following command
```
./mvnw clean package dockerfile:build -DskipTests --projects spring-kafka/producer-kafka
```

| Environment Variable     | Description |
| -----------------------  | ----------- |
| `SPRING_PROFILES_ACTIVE` | Specify the type of profile to run the application. To use `CloudKarafka` set `cloudkarafka`. The `default` profile will use local `Kafka` |
| `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value for `cloudkarafka` profile is `ark-01.srvs.cloudkafka.com:9094, ark-02.srvs.cloudkafka.com:9094, ark-03.srvs.cloudkafka.com:9094`. Using the `default` profile, the default value is `localhost:29092` |
| `CLOUDKARAFKA_USERNAME`  | Specify your `CloudKarafka` username. Required when using `cloudkarafka` profile |
| `CLOUDKARAFKA_PASSWORD`  | Specify your `CloudKarafka` password. Required when using `cloudkarafka` profile |

### consumer-kafka

In a terminal and inside `springboot-cloudkarafka` root folder, run the following command
```
./mvnw clean package dockerfile:build -DskipTests --projects spring-kafka/consumer-kafka
```

| Environment Variable     | Description |
| ------------------------ | ----------- |
| `SPRING_PROFILES_ACTIVE` | Specify the type of profile to run the application. To use `CloudKarafka` set `karafka`. The `default` will use local `Kafka` |
| `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value for `karafka` profile is `ark-01.srvs.cloudkafka.com:9094, ark-02.srvs.cloudkafka.com:9094, ark-03.srvs.cloudkafka.com:9094`. Using the `default` profile, the default value is `localhost:29092` |
| `CLOUDKARAFKA_USERNAME`  | Specify your `CloudKarafka` username. Required when using `cloudkarafka` profile |
| `CLOUDKARAFKA_PASSWORD`  | Specify your `CloudKarafka` password. Required when using `cloudkarafka` profile |

## Running microservices as Docker containers

### Using CloudKarafka

Open a terminal and export your `CloudKarafka` credentials to those environment variables
```
export CLOUDKARAFKA_USERNAME=...
export CLOUDKARAFKA_PASSWORD=...
```

| Microservice     | Command |
| ---------------- | ------- |
| `producer-kafka` | `docker run -d --rm --name producer-kafka --network springboot-cloudkarafka_default -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD -e SPRING_PROFILES_ACTIVE=cloudkarafka -p 9080:8080 docker.mycompany.com/producer-kafka:1.0.0` |
| `consumer-kafka` | `docker run -d --rm --name consumer-kafka --network springboot-cloudkarafka_default -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD -e SPRING_PROFILES_ACTIVE=cloudkarafka -p 9081:8080 docker.mycompany.com/consumer-kafka:1.0.0` |

### Using Kafka running locally

>Note. you must have the `docker-compose.yml` services up and running, as explained in the main README.

| Microservice     | Command |
| ---------------- | ------- |
| `producer-kafka` | `docker run -d --rm --name producer-kafka --network springboot-cloudkarafka_default -e KAFKA_URL=kafka:9092 -p 9080:8080 docker.mycompany.com/producer-kafka:1.0.0` |
| `consumer-kafka` | `docker run -d --rm --name consumer-kafka --network springboot-cloudkarafka_default -e KAFKA_URL=kafka:9092 -p 9081:8080 docker.mycompany.com/consumer-kafka:1.0.0` |

## Running microservices using Maven

### Using CloudKarafka

In a terminal and export your `CloudKarafka` credentials to those environment variables
```
export CLOUDKARAFKA_USERNAME=...
export CLOUDKARAFKA_PASSWORD=...
```

Then, inside `springboot-cloudkarafka` root folder, run the following command

| Microservice     | Command |
| ---------------- | ------- |
| `producer-kafka` | `./mvnw spring-boot:run --projects spring-kafka/producer-kafka -Dspring-boot.run.profiles=cloudkarafka -Dspring-boot.run.jvmArguments="-Dserver.port=9080"` |
| `consumer-kafka` | `./mvnw spring-boot:run --projects spring-kafka/consumer-kafka -Dspring-boot.run.profiles=cloudkarafka -Dspring-boot.run.jvmArguments="-Dserver.port=9081"` |

### Using Kafka running locally

>Note. you must have the `docker-compose.yml` services up and running, as explained in the main README.  

Inside `springboot-cloudkarafka` root folder, run the following command

| Microservice     | Command |
| ---------------- | ------- |
| `producer-kafka` | `./mvnw spring-boot:run --projects spring-kafka/producer-kafka -Dspring-boot.run.jvmArguments="-Dserver.port=9080"` |
| `consumer-kafka` | `./mvnw spring-boot:run --projects spring-kafka/consumer-kafka -Dspring-boot.run.jvmArguments="-Dserver.port=9081"` |

## Microservice URLs

| Microservice     | URL                   |
| ---------------- | --------------------- |
| `producer-kafka` | http://localhost:9080 |
| `consumer-kafka` | http://localhost:9081 |

## Execution example using CloudKarafka

**Posting a news**
> I am using [HTTPie](https://httpie.org/) 
```
http :9080/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
```

**producer-kafka logs**
```
INFO 6818 --- [nio-9080-exec-1] c.m.producerkafka.kafka.NewsProducer : Sending News 'News(id=17d3ddb1-ec35-449f-89b2-4e93ddb5e88b, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)' to topic '2gxxxxxx-news.json'
```

**consumer-kafka logs**
```
INFO 6834 --- [ntainer#0-0-C-1] c.m.consumerkafka.kafka.NewsConsumer : Received message
---
TOPIC: 2gxxxxxx-news.json; PARTITION: 0; OFFSET: 2;
PAYLOAD: News(id=17d3ddb1-ec35-449f-89b2-4e93ddb5e88b, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)
---
```