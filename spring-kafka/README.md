# `springboot-cloudkarafka /`
# `spring-kafka`

In this example, we use [`Spring Kafka`](https://docs.spring.io/spring-kafka/reference/html/) library to implement the
configuration between `Spring Boot` applications and `CloudKarafka`.

## Microservices

### producer-kafka

Spring Boot Web Java application that exposes one endpoint at which users can post `news` informing its
`source` and `title`. Once a request is made, `producer-kafka` pushes a message about the `news` to Kafka.

### consumer-kafka

Spring Boot Web Java application that listens from `Kafka` to messages published by the `producer-kafka` and logs it.

## Start microservices

### producer-kafka

Open a terminal and export your `CloudKarafka` credentials to those environment variables
```
export CLOUDKARAFKA_USERNAME=...
export CLOUDKARAFKA_PASSWORD=...
```

Then, inside `springboot-cloudkarafka` root folder, run the following command
```
./mvnw spring-boot:run --projects spring-kafka/producer-kafka
```

### consumer-kafka

Open another terminal and, similar to what you did before, export your `CloudKarafka` credentials to the environment
variable
```
export CLOUDKARAFKA_USERNAME=...
export CLOUDKARAFKA_PASSWORD=...
```

Inside `springboot-cloudkarafka` root folder, run the command below
```
./mvnw spring-boot:run --projects spring-kafka/consumer-kafka
```

## Microservices URLs

| Microservice   | URL                   |
| -------------- | --------------------- |
| producer-kafka | http://localhost:9080 |
| consumer-kafka | http://localhost:9081 |

## Execution example

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