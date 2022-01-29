# spring-cloud-stream-kafka-multi-topics-cloudkarafka
## `> spring-kafka`

In this example, we use [`Spring Kafka`](https://docs.spring.io/spring-kafka/reference/html/) library to implement the configuration between `Spring Boot` applications and `Kafka`.

## Applications

- ### producer-kafka

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news` or `alert`. Once a request is made, `producer-kafka` pushes a message related to the `news` or `alert` to Kafka.

  Endpoints
  ```
  POST /api/news {"source": "...", "title": "..."}
  POST /api/alerts {"level": "...", "message": "..."}
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

  > **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka#using-kafka-running-locally)  

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

    | Environment Variable     | Description |
    |--------------------------|-------------|
    | `SPRING_PROFILES_ACTIVE` | Specify the type of profile to run the application. To use `CloudKarafka` set `cloudkarafka`. The `default` profile will use local `Kafka` |
    | `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value is `localhost:29092` |
    | `CLOUDKARAFKA_USERNAME`  | Specify your `CloudKarafka` username. Required when using `cloudkarafka` profile |
    | `CLOUDKARAFKA_PASSWORD`  | Specify your `CloudKarafka` password. Required when using `cloudkarafka` profile |

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

    > **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka#using-kafka-running-locally)

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

> **Note:** [HTTPie](https://httpie.org/) is being used in the calls bellow 

- **news**
  ```
  http :9080/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
  ```

- **alerts**
  ```
  http :9080/api/alerts level=4 message="Tsunami is coming"
  ```

## Stop applications

- If they were started with `Maven`, go to the terminals where they are running and press `Ctrl+C`
- If they were started as Docker containers, go to a terminal and run the command below
  ```
  docker stop producer-kafka consumer-kafka
  ```

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

- Unable to run `producer-kafka` tests as **Mockito** is still not supported in AOT. See `spring-native` issues [#1343](https://github.com/spring-projects-experimental/spring-native/issues/1343) and [#1063](https://github.com/spring-projects-experimental/spring-native/issues/1063)
- Unable to run `consumer-kafka` due to the following issue [#1427](https://github.com/spring-projects-experimental/spring-native/issues/1427)
- After building successfully the `producer-kafka` Docker native image, the following exception is thrown during startup using `cloudkarafka` profile
  ```
  ERROR 1 --- [           main] o.springframework.kafka.core.KafkaAdmin  : Could not configure topics
  
  org.springframework.kafka.KafkaException: Timed out waiting to get existing topics; nested exception is java.util.concurrent.TimeoutException
  at org.springframework.kafka.core.KafkaAdmin.lambda$checkPartitions$5(KafkaAdmin.java:275) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.8.2]
  at java.util.HashMap.forEach(HashMap.java:1337) ~[na:na]
  at org.springframework.kafka.core.KafkaAdmin.checkPartitions(KafkaAdmin.java:254) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.8.2]
  at org.springframework.kafka.core.KafkaAdmin.addOrModifyTopicsIfNeeded(KafkaAdmin.java:240) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.8.2]
  at org.springframework.kafka.core.KafkaAdmin.initialize(KafkaAdmin.java:178) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.8.2]
  at org.springframework.kafka.core.KafkaAdmin.afterSingletonsInstantiated(KafkaAdmin.java:145) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.8.2]
  at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:972) ~[na:na]
  at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:918) ~[na:na]
  at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:583) ~[na:na]
  at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:64) ~[na:na]
  at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:732) ~[com.mycompany.producerkafka.ProducerKafkaApplication:na]
  at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:414) ~[com.mycompany.producerkafka.ProducerKafkaApplication:na]
  at org.springframework.boot.SpringApplication.run(SpringApplication.java:302) ~[com.mycompany.producerkafka.ProducerKafkaApplication:na]
  at org.springframework.boot.SpringApplication.run(SpringApplication.java:1303) ~[com.mycompany.producerkafka.ProducerKafkaApplication:na]
  at org.springframework.boot.SpringApplication.run(SpringApplication.java:1292) ~[com.mycompany.producerkafka.ProducerKafkaApplication:na]
  at com.mycompany.producerkafka.ProducerKafkaApplication.main(ProducerKafkaApplication.java:35) ~[com.mycompany.producerkafka.ProducerKafkaApplication:na]
  Caused by: java.util.concurrent.TimeoutException: null
  at java.util.concurrent.CompletableFuture.timedGet(CompletableFuture.java:1886) ~[na:na]
  at java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2021) ~[na:na]
  at org.apache.kafka.common.internals.KafkaFutureImpl.get(KafkaFutureImpl.java:180) ~[na:na]
  at org.springframework.kafka.core.KafkaAdmin.lambda$checkPartitions$5(KafkaAdmin.java:257) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.8.2]
  ... 15 common frames omitted
  ```
- After building successfully the `consumer-kafka` Docker native image, the following exception is thrown during startup using `cloudkarafka` profile. Probably due to this issue [#1367](https://github.com/spring-projects-experimental/spring-native/issues/1367) as `sasl.jaas.config` is not created correctly is it's using environment variable to set the username and password.
  ```
  WARN 1 --- [ntainer#1-1-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-consumerKafkaGroup-4, groupId=consumerKafkaGroup] Connection to node -1 (...) terminated during authentication. This may happen due to any of the following reasons: (1) Authentication failed due to invalid credentials with brokers older than 1.0.0, (2) Firewall blocking Kafka TLS traffic (eg it may only allow HTTPS traffic), (3) Transient network issue.
  WARN 1 --- [ntainer#1-1-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-consumerKafkaGroup-4, groupId=consumerKafkaGroup] Bootstrap broker ... (id: -1 rack: null) disconnected
  WARN 1 --- [ntainer#1-0-C-1] o.apache.kafka.common.network.Selector   : [Consumer clientId=consumer-consumerKafkaGroup-3, groupId=consumerKafkaGroup] Unexpected error from ...; closing connection
  
  java.lang.NullPointerException: null
  at java.util.regex.Matcher.getTextLength(Matcher.java:1770) ~[na:na]
  at java.util.regex.Matcher.reset(Matcher.java:416) ~[na:na]
  at java.util.regex.Matcher.<init>(Matcher.java:253) ~[na:na]
  at java.util.regex.Pattern.matcher(Pattern.java:1133) ~[na:na]
  at org.apache.kafka.common.security.scram.internals.ScramFormatter.saslName(ScramFormatter.java:106) ~[na:na]
  at org.apache.kafka.common.security.scram.internals.ScramSaslClient.evaluateChallenge(ScramSaslClient.java:116) ~[na:na]
  at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.lambda$createSaslToken$1(SaslClientAuthenticator.java:534) ~[na:na]
  at java.security.AccessController.doPrivileged(AccessController.java:150) ~[na:na]
  at javax.security.auth.Subject.doAs(Subject.java:423) ~[na:na]
  at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.createSaslToken(SaslClientAuthenticator.java:534) ~[na:na]
  at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.sendSaslClientToken(SaslClientAuthenticator.java:433) ~[na:na]
  at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.sendInitialToken(SaslClientAuthenticator.java:332) ~[na:na]
  at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.authenticate(SaslClientAuthenticator.java:273) ~[na:na]
  at org.apache.kafka.common.network.KafkaChannel.prepare(KafkaChannel.java:181) ~[na:na]
  at org.apache.kafka.common.network.Selector.pollSelectionKeys(Selector.java:543) ~[na:na]
  at org.apache.kafka.common.network.Selector.poll(Selector.java:481) ~[na:na]
  at org.apache.kafka.clients.NetworkClient.poll(NetworkClient.java:551) ~[na:na]
  at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:265) ~[na:na]
  at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:236) ~[na:na]
  at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:215) ~[na:na]
  at org.apache.kafka.clients.consumer.internals.AbstractCoordinator.ensureCoordinatorReady(AbstractCoordinator.java:246) ~[na:na]
  at org.apache.kafka.clients.consumer.internals.ConsumerCoordinator.poll(ConsumerCoordinator.java:480) ~[na:na]
  at org.apache.kafka.clients.consumer.KafkaConsumer.updateAssignmentMetadataIfNeeded(KafkaConsumer.java:1262) ~[na:na]
  at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1231) ~[na:na]
  at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1211) ~[na:na]
  at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollConsumer(KafkaMessageListenerContainer.java:1509) ~[na:na]
  at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doPoll(KafkaMessageListenerContainer.java:1499) ~[na:na]
  at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1327) ~[na:na]
  at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:1236) ~[na:na]
  at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515) ~[na:na]
  at java.util.concurrent.FutureTask.run(FutureTask.java:264) ~[na:na]
  at java.lang.Thread.run(Thread.java:829) ~[na:na]
  at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:596) ~[na:na]
  at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:192) ~[na:na]
  ```