# springboot-cloudkarafka
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

    - Open a terminal and navigate to `springboot-cloudkarafka` root folder

    - Export your `CloudKarafka` credentials to those environment variables
      ```
      export CLOUDKARAFKA_USERNAME=...
      export CLOUDKARAFKA_PASSWORD=...
      ```
    
    - Run application
      ```
      ./mvnw clean spring-boot:run --projects spring-kafka/producer-kafka \
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
      ./mvnw clean spring-boot:run --projects spring-kafka/consumer-kafka \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9081" \
        -Dspring-boot.run.profiles=cloudkarafka
      ```

- #### Using Kafka running locally

  > **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/springboot-cloudkarafka#running-kafka-locally)  

  - **producer-kafka**

    - Open a terminal and navigate to `springboot-cloudkarafka` root folder
  
    - Run application
      ```
      ./mvnw clean package spring-boot:run --projects spring-kafka/producer-kafka -DskipTests \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9080"
      ```

  - **consumer-kafka**

    - Open another terminal and make sure you are in `springboot-cloudkarafka` root folder
  
    - Run application
      ```
      ./mvnw clean package spring-boot:run --projects spring-kafka/consumer-kafka -DskipTests \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9081"
      ```

## Running applications as Docker containers

- ### Build application's Docker image

  - In a terminal, make sure you are in `springboot-cloudkarafka` root folder

  - Run the following script to build the Docker images
    - JVM
      ```
      ./docker-build-spring-kafka.sh
      ```
    - Native (it's not working, [see Issues](#issues)
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

    - In a terminal, make sure you are in `springboot-cloudkarafka` root folder

    - Export your `CloudKarafka` credentials to these environment variables
      ```
      export CLOUDKARAFKA_USERNAME=...
      export CLOUDKARAFKA_PASSWORD=...
      ```

    - Run the script below to start the docker containers
      ```
      ./start-spring-kafka-apps.sh cloudkarafka
      ```

  - #### Using Kafka running locally

    > **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/springboot-cloudkarafka#running-kafka-locally)

    - In a terminal, make sure you are in `springboot-cloudkarafka` root folder

    - Run the script below to start the docker containers
      ```
      ./start-spring-kafka-apps.sh
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

- If they were started as Docker containers, run the script below
  ```
  ./stop-spring-kafka-apps.sh
  ```

## Issues

`producer-kafka` and `consumer-kafka` Docker native images are not working when running with `cloudkarafka` profile

- `producer-api`
  ```
  WARN 1 --- [ad | producer-1] o.apache.kafka.common.network.Selector   : [Producer clientId=producer-1] Unexpected error from ark-02.srvs.cloudkafka.com/x.xxx.xxx.xx; closing connection
  
  java.lang.NullPointerException: null
  	at java.util.regex.Matcher.getTextLength(Matcher.java:1770) ~[na:na]
  	at java.util.regex.Matcher.reset(Matcher.java:416) ~[na:na]
  	at java.util.regex.Matcher.<init>(Matcher.java:253) ~[na:na]
  	at java.util.regex.Pattern.matcher(Pattern.java:1133) ~[na:na]
  	at org.apache.kafka.common.security.scram.internals.ScramFormatter.saslName(ScramFormatter.java:106) ~[na:na]
  	at org.apache.kafka.common.security.scram.internals.ScramSaslClient.evaluateChallenge(ScramSaslClient.java:115) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.lambda$createSaslToken$1(SaslClientAuthenticator.java:524) ~[na:na]
  	at java.security.AccessController.doPrivileged(AccessController.java:147) ~[na:na]
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
  	at java.lang.Thread.run(Thread.java:834) ~[na:na]
  	at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:519) ~[na:na]
  	at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:192) ~[na:na]
  
  WARN 1 --- [ad | producer-1] org.apache.kafka.clients.NetworkClient   : [Producer clientId=producer-1] Connection to node -2 (ark-02.srvs.cloudkafka.com/x.xxx.xxx.xx:xxxx) terminated during authentication. This may happen due to any of the following reasons: (1) Authentication failed due to invalid credentials with brokers older than 1.0.0, (2) Firewall blocking Kafka TLS traffic (eg it may only allow HTTPS traffic), (3) Transient network issue.
  WARN 1 --- [ad | producer-1] org.apache.kafka.clients.NetworkClient   : [Producer clientId=producer-1] Bootstrap broker ark-02.srvs.cloudkafka.com:9094 (id: -2 rack: null) disconnected
  WARN 1 --- [ad | producer-1] o.apache.kafka.common.network.Selector   : [Producer clientId=producer-1] Unexpected error from ark-03.srvs.cloudkafka.com/xx.xxx.xxx.xx; closing connection
  ```

- `consumer-api`
  ```
  WARN 1 --- [ntainer#1-0-C-1] o.apache.kafka.common.network.Selector   : [Consumer clientId=consumer-consumerKafkaGroup-3, groupId=consumerKafkaGroup] Unexpected error from ark-02.srvs.cloudkafka.com/x.xxx.xxx.xx; closing connection
  
  java.lang.NullPointerException: null
  	at java.util.regex.Matcher.getTextLength(Matcher.java:1770) ~[na:na]
  	at java.util.regex.Matcher.reset(Matcher.java:416) ~[na:na]
  	at java.util.regex.Matcher.<init>(Matcher.java:253) ~[na:na]
  	at java.util.regex.Pattern.matcher(Pattern.java:1133) ~[na:na]
  	at org.apache.kafka.common.security.scram.internals.ScramFormatter.saslName(ScramFormatter.java:106) ~[na:na]
  	at org.apache.kafka.common.security.scram.internals.ScramSaslClient.evaluateChallenge(ScramSaslClient.java:115) ~[na:na]
  	at org.apache.kafka.common.security.authenticator.SaslClientAuthenticator.lambda$createSaslToken$1(SaslClientAuthenticator.java:524) ~[na:na]
  	at java.security.AccessController.doPrivileged(AccessController.java:147) ~[na:na]
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
  	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:215) ~[na:na]
  	at org.apache.kafka.clients.consumer.internals.AbstractCoordinator.ensureCoordinatorReady(AbstractCoordinator.java:245) ~[na:na]
  	at org.apache.kafka.clients.consumer.internals.ConsumerCoordinator.poll(ConsumerCoordinator.java:480) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.updateAssignmentMetadataIfNeeded(KafkaConsumer.java:1257) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1226) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1206) ~[na:na]
  	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doPoll(KafkaMessageListenerContainer.java:1410) ~[na:na]
  	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1249) ~[na:na]
  	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:1161) ~[na:na]
  	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515) ~[na:na]
  	at java.util.concurrent.FutureTask.run(FutureTask.java:264) ~[na:na]
  	at java.lang.Thread.run(Thread.java:834) ~[na:na]
  	at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:519) ~[na:na]
  	at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:192) ~[na:na]
  
  WARN 1 --- [ntainer#1-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-consumerKafkaGroup-3, groupId=consumerKafkaGroup] Connection to node -2 (ark-02.srvs.cloudkafka.com/x.xxx.xxx.xx:xxxx) terminated during authentication. This may happen due to any of the following reasons: (1) Authentication failed due to invalid credentials with brokers older than 1.0.0, (2) Firewall blocking Kafka TLS traffic (eg it may only allow HTTPS traffic), (3) Transient network issue.
  WARN 1 --- [ntainer#1-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-consumerKafkaGroup-3, groupId=consumerKafkaGroup] Bootstrap broker ark-02.srvs.cloudkafka.com:9094 (id: -2 rack: null) disconnected
  WARN 1 --- [ntainer#0-1-C-1] o.apache.kafka.common.network.Selector   : [Consumer clientId=consumer-consumerKafkaGroup-2, groupId=consumerKafkaGroup] Unexpected error from ark-02.srvs.cloudkafka.com/x.xxx.xxx.xx; closing connection  
  ```
