# `springboot-cloudkarafka /`
# `spring-cloud-stream`

In this example, we use [`Spring Cloud Stream`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/)
library to implement the configuration between `Spring Boot` applications and `CloudKarafka`.

## Microservices

### producer-cloud-stream

Spring Boot Web Java application that exposes one endpoint at which users can post `news` informing its
`source` and `title`. Once a request is made, `producer-cloud-stream` pushes a message about the `news` to Kafka.

### consumer-cloud-stream

Spring Boot Web Java application that listens from `Kafka` to messages published by the `producer-cloud-stream` and
logs it.

## Start microservices

### producer-cloud-stream

Open a terminal and export your `CloudKarafka` credentials to those environment variables
```
export CLOUDKARAFKA_USERNAME=...
export CLOUDKARAFKA_PASSWORD=...
```

Then, inside `springboot-cloudkarafka` root folder, run the following command
```
./mvnw spring-boot:run --projects spring-cloud-stream/producer-cloud-stream
```

### consumer-cloud-stream

Open another terminal and, similar to what you did before, export your `CloudKarafka` credentials to the environment
variable
```
export CLOUDKARAFKA_USERNAME=...
export CLOUDKARAFKA_PASSWORD=...
```

Inside `springboot-cloudkarafka` root folder, run the command below
```
./mvnw spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream
```

## Microservices URLs

| Microservice          | URL                   |
| --------------------- | --------------------- |
| producer-cloud-stream | http://localhost:9082 |
| consumer-cloud-stream | http://localhost:9083 |

## Execution example

**Posting a news**
> I am using [HTTPie](https://httpie.org/) 
```
http :9082/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
```

**producer-cloud-stream logs**
```
INFO 5090 --- [nio-9082-exec-1] c.m.p.kafka.NewsProducer : Sending News 'News(id=04253f40-ff8e-4293-91ba-a570febda5e1, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)' to topic '2gxxxxxx-news.json'
```

**consumer-cloud-stream logs**
```
INFO 5066 --- [container-0-C-1] c.m.c.kafka.NewsConsumer : Received message
---
TOPIC: 2gxxxxxx-news.json; PARTITION: 0; OFFSET: 1;
PAYLOAD: News(id=04253f40-ff8e-4293-91ba-a570febda5e1, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)
---
```