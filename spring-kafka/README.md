# `springboot-cloudkarafka /`
# `spring-kafka`

In this example, we use [`Spring Kafka`](https://docs.spring.io/spring-kafka/reference/html/) library to implement the
configuration between `Spring Boot` applications and `CloudKarafka`.

## Microservices

### producer

Spring Boot Web Java application that exposes one endpoint at which users can post `news` informing its
`source` and `title`. Once a request is made, the `producer` pushes a message about the `news` to Kafka.

### consumer

Spring Boot Web Java application that listens from `Kafka` to messages published by the `producer` and logs it.

## Start microservices

### producer

Open a terminal and export your `CloudKarafka` credentials to those environment variables
```
export CLOUDKARAFKA_USERNAME=...
export CLOUDKARAFKA_PASSWORD=...
```

Then, inside `springboot-cloudkarafka` root folder, run the following command
```
./mvnw spring-boot:run --projects spring-kafka/producer
```

### consumer

Open another terminal and, similar to what you did before, export your `CloudKarafka` credentials to the environment
variable
```
export CLOUDKARAFKA_USERNAME=...
export CLOUDKARAFKA_PASSWORD=...
```

Inside `springboot-cloudkarafka` root folder, run the command below
```
./mvnw spring-boot:run --projects spring-kafka/consumer
```

## Microservices URLs

| Microservice | URL                   |
| ------------ | --------------------- |
| producer     | http://localhost:9080 |
| consumer     | http://localhost:9081 |

## Execution example

**Posting a new news title**
> I am using [HTTPie](https://httpie.org/) 
```
http :9080/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
```

**Producer logs the following output**
```
INFO 1763 --- [nio-9080-exec-2] c.mycompany.producer.kafka.NewsProducer  : Sending News 'News(id=4e6857c1-cbfb-4c14-ba19-b9a0f1d535b2, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)' to topic '2gxxxxxx-news.json'

```

**Consumer logs the output below**
```
INFO 1917 --- [ntainer#0-0-C-1] c.mycompany.consumer.kafka.NewsConsumer  : Received message
---
TOPIC: 2gxxxxxx-news.json; PARTITION: 0; OFFSET: 0;
PAYLOAD: News(id=4e6857c1-cbfb-4c14-ba19-b9a0f1d535b2, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)
---
```