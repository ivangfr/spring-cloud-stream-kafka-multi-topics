# `springboot-cloudkarafka`
# `> spring-cloud-stream`

In this example, we use [`Spring Cloud Stream`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/)
library to implement the configuration between `Spring Boot` applications and `Kafka`.

## Microservices

### producer-cloud-stream

Spring Boot Web Java application that exposes one endpoint at which users can post `news`. Once a request is made, 
`producer-cloud-stream` pushes a message about the `news` to Kafka.

Endpoint
```
POST /api/news {"source": "...", "title": "..."}
```

### consumer-cloud-stream

Spring Boot Web Java application that listens to messages (published by the `producer-cloud-stream`) and logs it.

## Start microservices

### Using CloudKarafka

#### producer-cloud-stream

Open a terminal and export your `CloudKarafka` credentials to those environment variables
```
export CLOUDKARAFKA_USERNAME=...
export CLOUDKARAFKA_PASSWORD=...
```

Then, inside `springboot-cloudkarafka` root folder, run the following command
```
./mvnw spring-boot:run --projects spring-cloud-stream/producer-cloud-stream -Dspring-boot.run.profiles=cloudkarafka
```

#### consumer-cloud-stream

Open another terminal and, similar to what you did before, export your `CloudKarafka` credentials to the environment
variable
```
export CLOUDKARAFKA_USERNAME=...
export CLOUDKARAFKA_PASSWORD=...
```

Inside `springboot-cloudkarafka` root folder, run the command below
```
./mvnw spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream -Dspring-boot.run.profiles=cloudkarafka
```

### Using Kafka running locally

>Note. you must have the `docker-compose.yml` services up and running, as explained in the main README.  

#### producer-cloud-stream

Open a terminal and inside `springboot-cloudkarafka` root folder, run the following command
```
./mvnw spring-boot:run --projects spring-cloud-stream/producer-cloud-stream
```

#### consumer-cloud-stream

Open another terminal and inside `springboot-cloudkarafka` root folder, run the command below
```
./mvnw spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream
```

## Microservice URLs

| Microservice          | URL                   |
| --------------------- | --------------------- |
| producer-cloud-stream | http://localhost:9082 |
| consumer-cloud-stream | http://localhost:9083 |

## Execution example using CloudKarafka

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