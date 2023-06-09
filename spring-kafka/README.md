# spring-cloud-stream-kafka-multi-topics-cloudkarafka
## `> spring-kafka`

In this example, we use [`Spring Kafka`](https://docs.spring.io/spring-kafka/reference/html/) library to implement the configuration between `Spring Boot` applications and `Kafka`.

## Applications

- ### producer-kafka

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news` or `alert`. Once a request is made, `producer-kafka` pushes a message related to the `news` or `alert` to Kafka.

  Endpoints
  ```
  POST /api/news {"source":"...", "title":"..."}
  POST /api/alerts {"level":"...", "message":"..."}
  ```

- ### consumer-kafka

  `Spring Boot` Web Java application that listens to the messages (published by `producer-kafka`) and logs it.

## Running applications using Maven

- #### Using CloudKarafka

  - **producer-kafka**

    - In a terminal, make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder

    - Export your `CloudKarafka` credentials to those environment variables
      ```
      export KAFKA_URL=...
      export CLOUDKARAFKA_USERNAME=...
      export CLOUDKARAFKA_PASSWORD=...
      ```
    
    - Run the Maven command below to start the application
      ```
      ./mvnw clean spring-boot:run --projects spring-kafka/producer-kafka \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9080" \
        -Dspring-boot.run.profiles=cloudkarafka
      ```

  - **consumer-kafka**

    - Open a new terminal and navigate to `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder
  
    - Export your `CloudKarafka` credentials to those environment variables
      ```
      export KAFKA_URL=...
      export CLOUDKARAFKA_USERNAME=...
      export CLOUDKARAFKA_PASSWORD=...
      ```
  
    - Run the Maven command below to start the application
      ```
      ./mvnw clean spring-boot:run --projects spring-kafka/consumer-kafka \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9081" \
        -Dspring-boot.run.profiles=cloudkarafka
      ```

- #### Using Kafka running locally

  > **Note**: you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka#using-kafka-running-locally)  

  - **producer-kafka**

    - In a terminal, make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder
  
    - Run application
      ```
      ./mvnw clean spring-boot:run --projects spring-kafka/producer-kafka \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9080"
      ```

  - **consumer-kafka**

    - Open a new terminal and navigate to `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder
  
    - Run application
      ```
      ./mvnw clean spring-boot:run --projects spring-kafka/consumer-kafka \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9081"
      ```

## Running applications as Docker containers

- ### Build application's Docker image

  - In a terminal, make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder

  - Run the following script to build the Docker images
    - JVM
      ```
      ./docker-build-spring-kafka.sh
      ```
    - Native
      ```
      ./docker-build-spring-kafka.sh native
      ```

- ### Application's Environment Variables

  - **producer-kafka** and **consumer-kafka**

    | Environment Variable     | Description                                                                                                                                |
    |--------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
    | `SPRING_PROFILES_ACTIVE` | Specify the type of profile to run the application. To use `CloudKarafka` set `cloudkarafka`. The `default` profile will use local `Kafka` |
    | `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value is `localhost:29092`                                                |
    | `CLOUDKARAFKA_USERNAME`  | Specify your `CloudKarafka` username. Required when using `cloudkarafka` profile                                                           |
    | `CLOUDKARAFKA_PASSWORD`  | Specify your `CloudKarafka` password. Required when using `cloudkarafka` profile                                                           |

- ### Starting application's Docker container

  - #### Using CloudKarafka

    - **producer-kafka**
      
      - In a terminal, export your `CloudKarafka` credentials to these environment variables
        ```
        export KAFKA_URL=...
        export CLOUDKARAFKA_USERNAME=...
        export CLOUDKARAFKA_PASSWORD=...
        ```

      - Run the command below to start the Docker container
        ```
        docker run --rm --name producer-kafka -p 9080:8080 \
          -e SPRING_PROFILES_ACTIVE=cloudkarafka \
          -e KAFKA_URL=$KAFKA_URL \
          -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
          -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
          ivanfranchin/producer-kafka:1.0.0
        ```

    - **consumer-kafka**

      - Open a new terminal and export your `CloudKarafka` credentials to these environment variables
        ```
        export KAFKA_URL=...
        export CLOUDKARAFKA_USERNAME=...
        export CLOUDKARAFKA_PASSWORD=...
        ```

      - Run the command below to start the Docker container
        ```
        docker run --rm --name consumer-kafka -p 9081:8080 \
          -e SPRING_PROFILES_ACTIVE=cloudkarafka \
          -e KAFKA_URL=$KAFKA_URL \
          -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
          -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
          ivanfranchin/consumer-kafka:1.0.0
        ```

  - #### Using Kafka running locally

    > **Note**: you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka#using-kafka-running-locally)

    - **producer-kafka**

      In a terminal, run the command below to start the Docker container
      ```
      docker run --rm --name producer-kafka -p 9080:8080 \
        -e KAFKA_URL=kafka:9092 \
        --network spring-cloud-stream-kafka-multi-topics-cloudkarafka_default \
        ivanfranchin/producer-kafka:1.0.0
      ```

    - **consumer-kafka**

      Open a new terminal and run the command below to start the Docker container
      ```
      docker run --rm --name consumer-kafka -p 9081:8080 \
        -e KAFKA_URL=kafka:9092 \
        --network spring-cloud-stream-kafka-multi-topics-cloudkarafka_default \
        ivanfranchin/consumer-kafka:1.0.0
      ```

## Applications URLs

| Application    | URL                   |
|----------------|-----------------------|
| producer-kafka | http://localhost:9080 |
| consumer-kafka | http://localhost:9081 |

## Playing around

In a terminal, submit the following POST requests to `producer-kafka` and check its logs and `consumer-kafka` logs

> **Note**: [HTTPie](https://httpie.org/) is being used in the calls bellow 

- **news**
  ```
  http :9080/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
  ```

- **alerts**
  ```
  http :9080/api/alerts level=4 message="Tsunami is coming"
  ```

## Stop applications

Go to the terminals where they are running and press `Ctrl+C`

## Running Test Cases

In a terminal, make sure you are inside `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder

- **producer-kafka**
  ```
  ./mvnw clean test --projects spring-kafka/producer-kafka
  ```

- **consumer-kafka**
  ```
  ./mvnw clean test --projects spring-kafka/consumer-kafka
  ```

## Cleanup

To remove the Docker images created by this example, go to a terminal and run the following commands
```
docker rmi ivanfranchin/producer-kafka:1.0.0
docker rmi ivanfranchin/consumer-kafka:1.0.0
```

## Issues

- When trying to run the Docker native images of `producer-kafka` and `consumer-kafka` using `cloudkarafka` profile, the following exception is thrown
  ```
  ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed
  
  org.springframework.context.ApplicationContextException: Failed to start bean 'org.springframework.kafka.config.internalKafkaListenerEndpointRegistry'
  	at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:181) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:6.0.4]
  	at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:356) ~[na:na]
  	at java.base@17.0.6/java.lang.Iterable.forEach(Iterable.java:75) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:na]
  	at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:155) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:6.0.4]
  	at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:123) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:6.0.4]
  	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:932) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:6.0.4]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:587) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:6.0.4]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:66) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:730) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:432) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:308) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1302) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1291) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at com.ivanfranchin.consumerkafka.ConsumerKafkaApplication.main(ConsumerKafkaApplication.java:10) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:na]
  Caused by: org.apache.kafka.common.KafkaException: Failed to construct kafka consumer
  	at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:830) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:666) ~[na:na]
  	at org.springframework.kafka.core.DefaultKafkaConsumerFactory.createRawConsumer(DefaultKafkaConsumerFactory.java:483) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.kafka.core.DefaultKafkaConsumerFactory.createKafkaConsumer(DefaultKafkaConsumerFactory.java:451) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.kafka.core.DefaultKafkaConsumerFactory.createConsumerWithAdjustedProperties(DefaultKafkaConsumerFactory.java:427) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.kafka.core.DefaultKafkaConsumerFactory.createKafkaConsumer(DefaultKafkaConsumerFactory.java:394) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.kafka.core.DefaultKafkaConsumerFactory.createConsumer(DefaultKafkaConsumerFactory.java:371) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.<init>(KafkaMessageListenerContainer.java:849) ~[na:na]
  	at org.springframework.kafka.listener.KafkaMessageListenerContainer.doStart(KafkaMessageListenerContainer.java:380) ~[na:na]
  	at org.springframework.kafka.listener.AbstractMessageListenerContainer.start(AbstractMessageListenerContainer.java:531) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.kafka.listener.ConcurrentMessageListenerContainer.doStart(ConcurrentMessageListenerContainer.java:226) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.kafka.listener.AbstractMessageListenerContainer.start(AbstractMessageListenerContainer.java:531) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:3.0.2]
  	at org.springframework.kafka.config.KafkaListenerEndpointRegistry.startIfNecessary(KafkaListenerEndpointRegistry.java:383) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:na]
  	at org.springframework.kafka.config.KafkaListenerEndpointRegistry.start(KafkaListenerEndpointRegistry.java:328) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:na]
  	at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:178) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:6.0.4]
  	... 13 common frames omitted
  Caused by: org.apache.kafka.common.KafkaException: org.apache.kafka.common.KafkaException: Could not find a public no-argument constructor for org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler
  	at org.apache.kafka.common.network.SaslChannelBuilder.configure(SaslChannelBuilder.java:184) ~[na:na]
  	at org.apache.kafka.common.network.ChannelBuilders.create(ChannelBuilders.java:192) ~[na:na]
  	at org.apache.kafka.common.network.ChannelBuilders.clientChannelBuilder(ChannelBuilders.java:81) ~[na:na]
  	at org.apache.kafka.clients.ClientUtils.createChannelBuilder(ClientUtils.java:105) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:738) ~[na:na]
  	... 27 common frames omitted
  Caused by: org.apache.kafka.common.KafkaException: Could not find a public no-argument constructor for org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler
  	at org.apache.kafka.common.utils.Utils.newInstance(Utils.java:394) ~[na:na]
  	at org.apache.kafka.common.network.SaslChannelBuilder.createClientCallbackHandler(SaslChannelBuilder.java:305) ~[na:na]
  	at org.apache.kafka.common.network.SaslChannelBuilder.configure(SaslChannelBuilder.java:148) ~[na:na]
  	... 31 common frames omitted
  Caused by: java.lang.NoSuchMethodException: org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler.<init>()
  	at java.base@17.0.6/java.lang.Class.getConstructor0(DynamicHub.java:3585) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:na]
  	at java.base@17.0.6/java.lang.Class.getDeclaredConstructor(DynamicHub.java:2754) ~[com.ivanfranchin.consumerkafka.ConsumerKafkaApplication:na]
  	at org.apache.kafka.common.utils.Utils.newInstance(Utils.java:392) ~[na:na]
  	... 33 common frames omitted
  ```
