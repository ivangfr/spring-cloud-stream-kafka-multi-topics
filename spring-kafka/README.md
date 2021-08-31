# spring-cloud-stream-kafka-multi-topics-cloudkarafka
## `> spring-kafka`

In this example, we use [`Spring Kafka`](https://docs.spring.io/spring-kafka/reference/html/) library to implement the configuration between `Spring Boot` applications and `Kafka`.

## Applications

- ### producer-kafka

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news` or `alert`. Once a request is made, `producer-kafka` pushes a message related to the `news` or `alert` to Kafka.

  Endpoints
  ```
  POST /api/news {"source": "...", "title": "..."}
  POST /api/alert {"level": "...", "message": "..."}
  ```

- ### consumer-kafka

  `Spring Boot` Web Java application that listens to the messages (published by `producer-kafka`) and logs it.

## Running applications using Maven

- #### Using CloudKarafka

  - **producer-kafka**

    - In a terminal, make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder

    - Export your `CloudKarafka` credentials to those environment variables
      ```
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
    | ------------------------ | ----------- |
    | `SPRING_PROFILES_ACTIVE` | Specify the type of profile to run the application. To use `CloudKarafka` set `cloudkarafka`. The `default` profile will use local `Kafka` |
    | `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value for `cloudkarafka` profile is `ark-01.srvs.cloudkafka.com:9094, ark-02.srvs.cloudkafka.com:9094, ark-03.srvs.cloudkafka.com:9094`. Using the `default` profile, the default value is `localhost:29092` |
    | `CLOUDKARAFKA_USERNAME`  | Specify your `CloudKarafka` username. Required when using `cloudkarafka` profile |
    | `CLOUDKARAFKA_PASSWORD`  | Specify your `CloudKarafka` password. Required when using `cloudkarafka` profile |

- ### Starting application's Docker container

  - #### Using CloudKarafka

    - **producer-kafka**
      
      - In a terminal, export your `CloudKarafka` credentials to these environment variables
        ```
        export CLOUDKARAFKA_USERNAME=...
        export CLOUDKARAFKA_PASSWORD=...
        ```

      - Run the command below to start the Docker container
        ```
        docker run --rm --name producer-kafka -p 9080:8080 \
          -e SPRING_PROFILES_ACTIVE=cloudkarafka \
          -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
          -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
          ivanfranchin/producer-kafka:1.0.0
        ```

    - **consumer-kafka**

      - Open a new terminal and export your `CloudKarafka` credentials to these environment variables
        ```
        export CLOUDKARAFKA_USERNAME=...
        export CLOUDKARAFKA_PASSWORD=...
        ```

      - Run the command below to start the Docker container
        ```
        docker run --rm --name consumer-kafka -p 9081:8080 \
          -e SPRING_PROFILES_ACTIVE=cloudkarafka \
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
| -------------- | --------------------- |
| producer-kafka | http://localhost:9080 |
| consumer-kafka | http://localhost:9081 |

## Example of execution using CloudKarafka

> **Note:** In the call below, I am using [HTTPie](https://httpie.org/)

- In a terminal, the following command will post a `news`
  ```
  http :9080/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
  ```

  **producer-kafka** logs
  ```
  INFO c.m.producerkafka.kafka.NewsProducer     : Sending News 'News(id=f631e9ca-497a-4d17-9e17-07e5a1ce28a9, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)' to topic '2gxxxxxx-news.json'
  ```

  **consumer-kafka** logs
  ```
  INFO c.m.consumerkafka.kafka.NewsConsumer     : Received message
  ---
  TOPIC: 2gxxxxxx-news.json; PARTITION: 0; OFFSET: 1;
  PAYLOAD: News(id=f631e9ca-497a-4d17-9e17-07e5a1ce28a9, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)
  ---
  ```

- In a terminal, the following command will post an `alert`
  ```
  http :9080/api/alert level=4 message="Tsunami is coming"
  ```

  **producer-kafka** logs
  ```
  INFO c.m.producerkafka.kafka.AlertProducer    : Sending Alert 'Alert(id=756a8dc8-21ca-4856-9a4d-a0b34c158b43, level=4, message=Tsunami is coming)' to topic '2gxxxxxx-alert.json'
  ```

  **consumer-kafka** logs
  ```
  INFO c.m.consumerkafka.kafka.NewsConsumer     : Received message
  ---
  TOPIC: 2gxxxxxx-alert.json; PARTITION: 0; OFFSET: 1;
  PAYLOAD: Alert(id=756a8dc8-21ca-4856-9a4d-a0b34c158b43, level=4, message=Tsunami is coming)
  ---
  ```

## Stop applications

- If they were started with `Maven`, go to the terminals where they are running and press `Ctrl+C`
- If they were started as Docker containers, go to a terminal and run the command below
  ```
  docker stop producer-kafka consumer-kafka
  ```

## Cleanup

To remove the Docker images created by this example, go to a terminal and run the following commands
```
docker rmi ivanfranchin/producer-kafka:1.0.0
docker rmi ivanfranchin/consumer-kafka:1.0.0
```

## Issues

### Profile `cloudkarafka` 

- `producer-kafka`

  After building and starting the application in Native mode, the following exception is thrown the first `news` or `alert` is submitted
  ```
  WARN 1 --- [ad | producer-1] o.apache.kafka.common.network.Selector   : [Producer clientId=producer-1] Unexpected error from ark-03.srvs.cloudkafka.com/xx.xxx.xxx.xx; closing connection
  
  java.lang.NullPointerException: null
  	at java.util.regex.Matcher.getTextLength(Matcher.java:1770) ~[na:na]
  	at java.util.regex.Matcher.reset(Matcher.java:416) ~[na:na]
  	at java.util.regex.Matcher.<init>(Matcher.java:253) ~[na:na]
  	at java.util.regex.Pattern.matcher(Pattern.java:1133) ~[na:na]
  	at org.apache.kafka.common.security.scram.internals.ScramFormatter.saslName(ScramFormatter.java:106) ~[na:na]
  	at org.apache.kafka.common.security.scram.internals.ScramSaslClient.evaluateChallenge(ScramSaslClient.java:115) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.lambda$createSaslToken$1(SaslClientAuthenticator.java:524) ~[na:na]
  	at java.security.AccessController.doPrivileged(AccessController.java:150) ~[na:na]
  	at javax.security.auth.Subject.doAs(Subject.java:423) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.createSaslToken(SaslClientAuthenticator.java:524) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.sendSaslClientToken(SaslClientAuthenticator.java:431) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.sendInitialToken(SaslClientAuthenticator.java:330) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.authenticate(SaslClientAuthenticator.java:271) ~[na:na]
  	at org.apache.kafka.common.network.KafkaChannel.prepare(KafkaChannel.java:176) ~[na:na]
  	at org.apache.kafka.common.network.Selector.pollSelectionKeys(Selector.java:543) ~[na:na]
  	at org.apache.kafka.common.network.Selector.poll(Selector.java:481) ~[na:na]
  	at org.apache.kafka.clients.NetworkClient.poll(NetworkClient.java:563) ~[na:na]
  	at org.apache.kafka.clients.producer.internals.Sender.runOnce(Sender.java:325) ~[na:na]
  	at org.apache.kafka.clients.producer.internals.Sender.run(Sender.java:240) ~[na:na]
  	at java.lang.Thread.run(Thread.java:829) ~[na:na]
  	at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:567) ~[na:na]
  	at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:192) ~[na:na]
  
  WARN 1 --- [ad | producer-1] org.apache.kafka.clients.NetworkClient   : [Producer clientId=producer-1] Connection to node -3 (ark-03.srvs.cloudkafka.com/xx.xxx.xxx.xx:9094) terminated during authentication. This may happen due to any of the following reasons: (1) Authentication failed due to invalid credentials with brokers older than 1.0.0, (2) Firewall blocking Kafka TLS traffic (eg it may only allow HTTPS traffic), (3) Transient network issue.
  WARN 1 --- [ad | producer-1] org.apache.kafka.clients.NetworkClient   : [Producer clientId=producer-1] Bootstrap broker ark-03.srvs.cloudkafka.com:9094 (id: -3 rack: null) disconnected  
  ```

- `consumer-kafka`

  After building the application in Native mode, the following exception is thrown at startup time
  ```
  WARN 1 --- [ntainer#0-1-C-1] o.apache.kafka.common.network.Selector   : [Consumer clientId=consumer-consumerKafkaGroup-2, groupId=consumerKafkaGroup] Unexpected error from ark-03.srvs.cloudkafka.com/xx.xxx.xxx.xx; closing connection
  
  java.lang.NullPointerException: null
  	at java.util.regex.Matcher.getTextLength(Matcher.java:1770) ~[na:na]
  	at java.util.regex.Matcher.reset(Matcher.java:416) ~[na:na]
  	at java.util.regex.Matcher.<init>(Matcher.java:253) ~[na:na]
  	at java.util.regex.Pattern.matcher(Pattern.java:1133) ~[na:na]
  	at org.apache.kafka.common.security.scram.internals.ScramFormatter.saslName(ScramFormatter.java:106) ~[na:na]
  	at org.apache.kafka.common.security.scram.internals.ScramSaslClient.evaluateChallenge(ScramSaslClient.java:115) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.lambda$createSaslToken$1(SaslClientAuthenticator.java:524) ~[na:na]
  	at java.security.AccessController.doPrivileged(AccessController.java:150) ~[na:na]
  	at javax.security.auth.Subject.doAs(Subject.java:423) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.createSaslToken(SaslClientAuthenticator.java:524) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.sendSaslClientToken(SaslClientAuthenticator.java:431) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.sendInitialToken(SaslClientAuthenticator.java:330) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.authenticate(SaslClientAuthenticator.java:271) ~[na:na]
  	at org.apache.kafka.common.network.KafkaChannel.prepare(KafkaChannel.java:176) ~[na:na]
  	at org.apache.kafka.common.network.Selector.pollSelectionKeys(Selector.java:543) ~[na:na]
  	at org.apache.kafka.common.network.Selector.poll(Selector.java:481) ~[na:na]
  	at org.apache.kafka.clients.NetworkClient.poll(NetworkClient.java:563) ~[na:na]
  	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:265) ~[na:na]
  	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:236) ~[na:na]
  	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:227) ~[na:na]
  	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.awaitMetadataUpdate(ConsumerNetworkClient.java:164) ~[na:na]
  	at org.apache.kafka.clients.consumer.internals.AbstractCoordinator.ensureCoordinatorReady(AbstractCoordinator.java:257) ~[na:na]
  	at org.apache.kafka.clients.consumer.internals.ConsumerCoordinator.poll(ConsumerCoordinator.java:480) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.updateAssignmentMetadataIfNeeded(KafkaConsumer.java:1257) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1226) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1206) ~[na:na]
  	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doPoll(KafkaMessageListenerContainer.java:1412) ~[na:na]
  	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1249) ~[na:na]
  	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:1161) ~[na:na]
  	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515) ~[na:na]
  	at java.util.concurrent.FutureTask.run(FutureTask.java:264) ~[na:na]
  	at java.lang.Thread.run(Thread.java:829) ~[na:na]
  	at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:567) ~[na:na]
  	at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:192) ~[na:na]
  
  WARN 1 --- [ntainer#0-1-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-consumerKafkaGroup-2, groupId=consumerKafkaGroup] Connection to node -3 (ark-03.srvs.cloudkafka.com/xx.xxx.xxx.xx:9094) terminated during authentication. This may happen due to any of the following reasons: (1) Authentication failed due to invalid credentials with brokers older than 1.0.0, (2) Firewall blocking Kafka TLS traffic (eg it may only allow HTTPS traffic), (3) Transient network issue.
  WARN 1 --- [ntainer#0-1-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-consumerKafkaGroup-2, groupId=consumerKafkaGroup] Bootstrap broker ark-03.srvs.cloudkafka.com:9094 (id: -3 rack: null) disconnected
  ```
