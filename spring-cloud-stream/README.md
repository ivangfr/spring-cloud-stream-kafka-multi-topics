# spring-cloud-stream-kafka-multi-topics-cloudkarafka
## `> spring-cloud-stream`

In this example, we use [`Spring Cloud Stream`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/) library to implement the configuration between `Spring Boot` applications and `Kafka`.

## Applications

- ### producer-cloud-stream

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news` or `alert`. Once a request is made, `producer-cloud-stream` pushes a message related to the `news` or `alert` to Kafka.

  Endpoints
  ```
  POST /api/news {"source": "...", "title": "..."}
  POST /api/alerts {"level": "...", "message": "..."}
  ```

- ### consumer-cloud-stream

  `Spring Boot` Web Java application that listens to the messages (published by `producer-cloud-stream`) and logs it.

## Running applications using Maven

- #### Using CloudKarafka

  - **producer-cloud-stream**

    - In a terminal, make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder

    - Export your `CloudKarafka` credentials to those environment variables
      ```
      export CLOUDKARAFKA_USERNAME=...
      export CLOUDKARAFKA_PASSWORD=...
      ```
    
    - Run the Maven command below to start the application
      ```
      ./mvnw clean spring-boot:run --projects spring-cloud-stream/producer-cloud-stream \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9082" \
        -Dspring-boot.run.profiles=cloudkarafka
      ```

  - **consumer-cloud-stream**

    - Open a new terminal and navigate to `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder
  
    - Export your `CloudKarafka` credentials to those environment variables
      ```
      export CLOUDKARAFKA_USERNAME=...
      export CLOUDKARAFKA_PASSWORD=...
      ```
  
    - Run the Maven command below to start the application
      ```
      ./mvnw clean spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9083" \
        -Dspring-boot.run.profiles=cloudkarafka
      ```

- #### Using Kafka running locally

  > **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka#using-kafka-running-locally)  

  - **producer-cloud-stream**

    - In a terminal, make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder
  
    - Run the Maven command below to start the application
      ```
      ./mvnw clean spring-boot:run --projects spring-cloud-stream/producer-cloud-stream \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9082"
      ```

  - **consumer-cloud-stream**

    - Open a new terminal and navigate to `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder
  
    - Run the Maven command below to start the application
      ```
      ./mvnw clean spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9083"
      ```

## Running applications as Docker containers

- ### Build application's Docker image

  - In a terminal, make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder

  - Run the following script to build the Docker images
    - JVM
      ```
      ./docker-build-spring-cloud-stream.sh
      ```
    - Native
      ```
      ./docker-build-spring-cloud-stream.sh native
      ```

- ### Application's Environment Variables

  - **producer-cloud-stream** and **consumer-cloud-stream**

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
        docker run --rm --name producer-cloud-stream -p 9082:8080 \
          -e SPRING_PROFILES_ACTIVE=cloudkarafka \
          -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
          -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
          ivanfranchin/producer-cloud-stream:1.0.0
        ```

    - **consumer-kafka**

      - Open a new terminal and export your `CloudKarafka` credentials to these environment variables
        ```
        export CLOUDKARAFKA_USERNAME=...
        export CLOUDKARAFKA_PASSWORD=...
        ```

      - Run the command below to start the Docker container
        ```
        docker run --rm --name consumer-cloud-stream -p 9083:8080 \
          -e SPRING_PROFILES_ACTIVE=cloudkarafka \
          -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
          -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
          ivanfranchin/consumer-cloud-stream:1.0.0
        ```

  - #### Using Kafka running locally

    > **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka#using-kafka-running-locally)

    - **producer-kafka**

      In a terminal, run the command below to start the Docker container
      ```
      docker run --rm --name producer-cloud-stream -p 9082:8080 \
        -e KAFKA_URL=kafka:9092 \
        --network spring-cloud-stream-kafka-multi-topics-cloudkarafka_default \
        ivanfranchin/producer-cloud-stream:1.0.0
      ```

    - **consumer-kafka**

      Open a new terminal and run the command below to start the Docker container
      ```
      docker run --rm --name consumer-cloud-stream -p 9083:8080 \
        -e KAFKA_URL=kafka:9092 \
        --network spring-cloud-stream-kafka-multi-topics-cloudkarafka_default \
        ivanfranchin/consumer-cloud-stream:1.0.0
      ```

## Applications URLs

| Application           | URL                   |
| --------------------- | --------------------- |
| producer-cloud-stream | http://localhost:9082 |
| consumer-cloud-stream | http://localhost:9083 |

## Example of execution using CloudKarafka

> **Note:** [HTTPie](https://httpie.org/) is being used in the calls bellow

- In a terminal, the following command will post a news
  ```
  http :9082/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
  ```

  **producer-cloud-stream** logs
  ```
  INFO c.m.p.kafka.NewsProducer : Sending News 'News(id=04253f40-ff8e-4293-91ba-a570febda5e1, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)' to topic '2gxxxxxx-news.json'
  ```

  **consumer-cloud-stream** logs
  ```
  INFO c.m.c.kafka.NewsConsumer : Received message
  ---
  TOPIC: 2gxxxxxx-news.json; PARTITION: 0; OFFSET: 2;
  PAYLOAD: News(id=04253f40-ff8e-4293-91ba-a570febda5e1, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)
  ---
  ```

- In a terminal, the following command will post an `alert`
  ```
  http :9082/api/alerts level=4 message="Tsunami is coming"
  ```

  **producer-cloud-stream** logs
  ```
  INFO c.m.producerkafka.kafka.AlertProducer    : Sending Alert 'Alert(id=756a8dc8-21ca-4856-9a4d-a0b34c158b43, level=4, message=Tsunami is coming)' to topic '2gxxxxxx-alert.json'
  ```

  **consumer-cloud-stream** logs
  ```
  INFO c.m.consumerkafka.kafka.NewsConsumer     : Received message
  ---
  TOPIC: 2gxxxxxx-alert.json; PARTITION: 0; OFFSET: 2;
  PAYLOAD: Alert(id=756a8dc8-21ca-4856-9a4d-a0b34c158b43, level=4, message=Tsunami is coming)
  ---
  ```

## Stop applications

- If they were started with `Maven`, go to the terminals where they are running and press `Ctrl+C`
- If they were started as Docker containers, go to a terminal and run the command below
  ```
  docker stop producer-cloud-stream consumer-cloud-stream
  ```

## Cleanup

To remove the Docker images created by this example, go to a terminal and run the following commands
```
docker rmi ivanfranchin/producer-cloud-stream:1.0.0
docker rmi ivanfranchin/consumer-cloud-stream:1.0.0
```

## Running Test Cases

In a terminal, make sure you are inside `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder

- **producer-cloud-stream**
  ```
  ./mvnw clean test --projects spring-cloud-stream/producer-cloud-stream
  ```

- **consumer-cloud-stream**
  ```
  ./mvnw clean test --projects spring-cloud-stream/consumer-cloud-stream
  ```

## Issues

### Profile `default`

- `producer-cloud-stream`

  After building and starting the application in Native mode, the following warning is thrown when it pushes a `news` or an `alert`. It doesn't prevent the application to work normally
  ```
  WARN 1 --- [ctor-http-nio-2] o.s.c.s.binder.DefaultBinderFactory      : Failed to add additional Message Converters from child context
  
  java.lang.NullPointerException: null
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinderInstance(DefaultBinderFactory.java:277) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.doGetBinder(DefaultBinderFactory.java:224) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinder(DefaultBinderFactory.java:152) ~[na:na]
  	at org.springframework.cloud.stream.binding.BindingService.getBinder(BindingService.java:386) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.binding.BindingService.bindProducer(BindingService.java:270) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.function.StreamBridge.resolveDestination(StreamBridge.java:256) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:202) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:156) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:136) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.1.3]
  	at com.mycompany.producercloudstream.kafka.MessageProducer.send(MessageProducer.java:32) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at com.mycompany.producercloudstream.rest.NewsController.publishNews(NewsController.java:27) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
  	at org.springframework.web.reactive.result.method.InvocableHandlerMethod.lambda$invoke$0(InvocableHandlerMethod.java:146) ~[na:na]
  	at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:125) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.9]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.9]
  	at reactor.core.publisher.MonoZip$ZipCoordinator.signal(MonoZip.java:251) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.9]
  	at reactor.core.publisher.MonoZip$ZipInner.onNext(MonoZip.java:336) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.9]
  	at reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.onNext(MonoPeekTerminal.java:180) ~[na:na]
  	at reactor.core.publisher.FluxDefaultIfEmpty$DefaultIfEmptySubscriber.onNext(FluxDefaultIfEmpty.java:101) ~[na:na]
  	at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:200) ~[na:na]
  	at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:74) ~[na:na]
  	at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79) ~[na:na]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.9]
  	at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:151) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.9]
  	at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onNext(FluxContextWrite.java:107) ~[na:na]
  	at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onNext(FluxMapFuseable.java:295) ~[na:na]
  	at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onNext(FluxFilterFuseable.java:337) ~[na:na]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.9]
  	at reactor.core.publisher.MonoCollect$CollectSubscriber.onComplete(MonoCollect.java:159) ~[na:na]
  	at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
  	at reactor.core.publisher.FluxPeek$PeekSubscriber.onComplete(FluxPeek.java:260) ~[na:na]
  	at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
  	at reactor.netty.channel.FluxReceive.onInboundComplete(FluxReceive.java:400) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:1.0.10]
  	at reactor.netty.channel.ChannelOperations.onInboundComplete(ChannelOperations.java:419) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:1.0.10]
  	at reactor.netty.http.server.HttpServerOperations.onInboundNext(HttpServerOperations.java:547) ~[na:na]
  	at reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:93) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[na:na]
  	at reactor.netty.http.server.HttpTrafficHandler.channelRead(HttpTrafficHandler.java:252) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[na:na]
  	at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436) ~[na:na]
  	at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:324) ~[na:na]
  	at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:296) ~[na:na]
  	at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[na:na]
  	at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
  	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) ~[na:na]
  	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166) ~[na:na]
  	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:719) ~[na:na]
  	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:655) ~[na:na]
  	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:581) ~[na:na]
  	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493) ~[na:na]
  	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:986) ~[na:na]
  	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[na:na]
  	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[na:na]
  	at java.lang.Thread.run(Thread.java:829) ~[na:na]
  	at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:567) ~[na:na]
  	at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:192) ~[na:na]
  ```

### Profile `cloudkarafka`

- `producer-cloud-stream`

  After building and starting the application in Native mode, the following exception is thrown when the first `news` or `alert` is submitted
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

- `consumer-cloud-stream`

  After building the application in Native mode, the following exception is thrown at startup time
  ```
  WARN 1 --- [           main] o.apache.kafka.common.network.Selector   : [Consumer clientId=consumer-consumerCloudStreamGroup-1, groupId=consumerCloudStreamGroup] Unexpected error from ark-01.srvs.cloudkafka.com/xx.xxx.xxx.xxx; closing connection
  
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
  	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:215) ~[na:na]
  	at org.apache.kafka.clients.consumer.internals.Fetcher.getTopicMetadata(Fetcher.java:375) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.partitionsFor(KafkaConsumer.java:1953) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.partitionsFor(KafkaConsumer.java:1921) ~[na:na]
  	at java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
  	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:344) ~[na:na]
  	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:208) ~[na:na]
  	at com.sun.proxy.$Proxy291.partitionsFor(Unknown Source) ~[na:na]
  	at org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder.lambda$getPartitionInfo$4(KafkaMessageChannelBinder.java:1124) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.binder.kafka.provisioning.KafkaTopicProvisioner.lambda$getPartitionsForTopic$6(KafkaTopicProvisioner.java:535) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.retry.support.RetryTemplate.doExecute(RetryTemplate.java:329) ~[na:na]
  	at org.springframework.retry.support.RetryTemplate.execute(RetryTemplate.java:209) ~[na:na]
  	at org.springframework.cloud.stream.binder.kafka.provisioning.KafkaTopicProvisioner.getPartitionsForTopic(KafkaTopicProvisioner.java:530) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder.getPartitionInfo(KafkaMessageChannelBinder.java:1120) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder.processTopic(KafkaMessageChannelBinder.java:869) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder.createConsumerEndpoint(KafkaMessageChannelBinder.java:625) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder.createConsumerEndpoint(KafkaMessageChannelBinder.java:158) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.binder.AbstractMessageChannelBinder.doBindConsumer(AbstractMessageChannelBinder.java:408) ~[na:na]
  	at org.springframework.cloud.stream.binder.AbstractMessageChannelBinder.doBindConsumer(AbstractMessageChannelBinder.java:91) ~[na:na]
  	at org.springframework.cloud.stream.binder.AbstractBinder.bindConsumer(AbstractBinder.java:143) ~[na:na]
  	at org.springframework.cloud.stream.binding.BindingService.doBindConsumer(BindingService.java:177) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.binding.BindingService.bindConsumer(BindingService.java:134) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.binding.AbstractBindableProxyFactory.createAndBindInputs(AbstractBindableProxyFactory.java:118) ~[na:na]
  	at org.springframework.cloud.stream.binding.InputBindingLifecycle.doStartWithBindable(InputBindingLifecycle.java:58) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at java.util.LinkedHashMap$LinkedValues.forEach(LinkedHashMap.java:608) ~[na:na]
  	at org.springframework.cloud.stream.binding.AbstractBindingLifecycle.start(AbstractBindingLifecycle.java:57) ~[na:na]
  	at org.springframework.cloud.stream.binding.InputBindingLifecycle.start(InputBindingLifecycle.java:34) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:178) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.9]
  	at org.springframework.context.support.DefaultLifecycleProcessor.access$200(DefaultLifecycleProcessor.java:54) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.9]
  	at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:356) ~[na:na]
  	at java.lang.Iterable.forEach(Iterable.java:75) ~[na:na]
  	at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:155) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.9]
  	at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:123) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.9]
  	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:935) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:586) ~[na:na]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:64) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:2.5.4]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:434) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:2.5.4]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:338) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:2.5.4]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1343) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:2.5.4]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1332) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:2.5.4]
  	at com.mycompany.consumercloudstream.ConsumerCloudStreamApplication.main(ConsumerCloudStreamApplication.java:30) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:na]
  
  WARN 1 --- [           main] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-consumerCloudStreamGroup-1, groupId=consumerCloudStreamGroup] Connection to node -1 (ark-01.srvs.cloudkafka.com/xx.xxx.xxx.xxx:9094) terminated during authentication. This may happen due to any of the following reasons: (1) Authentication failed due to invalid credentials with brokers older than 1.0.0, (2) Firewall blocking Kafka TLS traffic (eg it may only allow HTTPS traffic), (3) Transient network issue.
  WARN 1 --- [           main] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-consumerCloudStreamGroup-1, groupId=consumerCloudStreamGroup] Bootstrap broker ark-01.srvs.cloudkafka.com:9094 (id: -1 rack: null) disconnected
  ```