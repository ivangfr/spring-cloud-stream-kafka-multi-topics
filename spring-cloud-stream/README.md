# spring-cloud-stream-kafka-multi-topics-cloudkarafka
## `> spring-cloud-stream`

In this example, we use [`Spring Cloud Stream`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/) library to implement the configuration between `Spring Boot` applications and `Kafka`.

## Applications

- ### producer-cloud-stream

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news` or `alert`. Once a request is made, `producer-cloud-stream` pushes a message related to the `news` or `alert` to Kafka.

  Endpoints
  ```
  POST /api/news {"source":"...", "title":"..."}
  POST /api/alerts {"level":"...", "message":"..."}
  ```

- ### consumer-cloud-stream

  `Spring Boot` Web Java application that listens to the messages (published by `producer-cloud-stream`) and logs it.

## Running applications using Maven

- #### Using CloudKarafka

  - **producer-cloud-stream**

    - In a terminal, make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder

    - Export your `CloudKarafka` credentials to those environment variables
      ```
      export KAFKA_URL=...
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
      export KAFKA_URL=...
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

  > **Note**: you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka#using-kafka-running-locally)  

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
        docker run --rm --name producer-cloud-stream -p 9082:8080 \
          -e SPRING_PROFILES_ACTIVE=cloudkarafka \
          -e KAFKA_URL=$KAFKA_URL \
          -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
          -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
          ivanfranchin/producer-cloud-stream:1.0.0
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
        docker run --rm --name consumer-cloud-stream -p 9083:8080 \
          -e SPRING_PROFILES_ACTIVE=cloudkarafka \
          -e KAFKA_URL=$KAFKA_URL \
          -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
          -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
          ivanfranchin/consumer-cloud-stream:1.0.0
        ```

  - #### Using Kafka running locally

    > **Note**: you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka#using-kafka-running-locally)

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
|-----------------------|-----------------------|
| producer-cloud-stream | http://localhost:9082 |
| consumer-cloud-stream | http://localhost:9083 |

## Playing around

In a terminal, submit the following POST requests to `producer-cloud-stream` and check its logs and `consumer-cloud-stream` logs

> **Note**: [HTTPie](https://httpie.org/) is being used in the calls bellow

- **news**
  ```
  http :9082/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
  ```
  
- **alerts**
  ```
  http :9082/api/alerts level=4 message="Tsunami is coming"
  ```

## Stop applications

Go to the terminals where they are running and press `Ctrl+C`

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

## Cleanup

To remove the Docker images created by this example, go to a terminal and run the following commands
```
docker rmi ivanfranchin/producer-cloud-stream:1.0.0
docker rmi ivanfranchin/consumer-cloud-stream:1.0.0
```

## Issues

- Unable to run `producer-cloud-stream` tests as **Mockito** is still not supported in AOT. See `spring-native` issues [#1343](https://github.com/spring-projects-experimental/spring-native/issues/1343) and [#1063](https://github.com/spring-projects-experimental/spring-native/issues/1063)

- Unable to run `consumer-cloud-stream` tests due to the following exception
  ```
  ...
  [ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 3.088 s <<< FAILURE! - in alert.kafka.com.ivanfranchin.consumercloudstream.AlertEventConsumerTest
  [ERROR] testAlert{CapturedOutput}  Time elapsed: 3.065 s  <<< ERROR!
  java.lang.NullPointerException
  	at alert.kafka.com.ivanfranchin.consumercloudstream.AlertEventConsumerTest.testAlert(AlertEventConsumerTest.java:33)
  ...
  [ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 0.474 s <<< FAILURE! - in news.kafka.com.ivanfranchin.consumercloudstream.NewsEventConsumerTest
  [ERROR] testNews{CapturedOutput}  Time elapsed: 0.474 s  <<< ERROR!
  java.lang.NullPointerException
  	at news.kafka.com.ivanfranchin.consumercloudstream.NewsEventConsumerTest.testNews(NewsEventConsumerTest.java:33)
  ...
  ```

- `producer-service` Docker native image is built successfully. However, the following exception is thrown when a `news` or an `alert` is submitted
  ```
  ERROR 1 --- [ctor-http-nio-2] a.w.r.e.AbstractErrorWebExceptionHandler : [1e3bbda0-1]  500 Server Error for HTTP POST "/api/news"
  
  org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.boot.autoconfigure.condition.OnBeanCondition]: No default constructor found; nested exception is java.lang.NoSuchMethodException: org.springframework.boot.autoconfigure.condition.OnBeanCondition.<init>()
      at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:29) ~[na:na]
      Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:
  Error has been observed at the following site(s):
      *__checkpoint ? org.springframework.boot.actuate.metrics.web.reactive.server.MetricsWebFilter [DefaultWebFilterChain]
      *__checkpoint ? HTTP POST "/api/news" [ExceptionHandlingWebHandler]
  Original Stack Trace:
          at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:29) ~[na:na]
          at org.springframework.context.annotation.ConditionEvaluator.getCondition(ConditionEvaluator.java:125) ~[na:na]
          at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:96) ~[na:na]
          at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:88) ~[na:na]
          at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:71) ~[na:na]
          at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.doRegisterBean(AnnotatedBeanDefinitionReader.java:254) ~[na:na]
          at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.registerBean(AnnotatedBeanDefinitionReader.java:147) ~[na:na]
          at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.register(AnnotatedBeanDefinitionReader.java:137) ~[na:na]
          at org.springframework.context.annotation.AnnotationConfigApplicationContext.register(AnnotationConfigApplicationContext.java:168) ~[na:na]
          at org.springframework.cloud.stream.binder.DefaultBinderFactory.initializeBinderContextSimple(DefaultBinderFactory.java:410) ~[na:na]
          at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinderInstance(DefaultBinderFactory.java:265) ~[na:na]
          at org.springframework.cloud.stream.binder.DefaultBinderFactory.doGetBinder(DefaultBinderFactory.java:223) ~[na:na]
          at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinder(DefaultBinderFactory.java:151) ~[na:na]
          at org.springframework.cloud.stream.binding.BindingService.getBinder(BindingService.java:394) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
          at org.springframework.cloud.stream.binding.BindingService.bindProducer(BindingService.java:277) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
          at org.springframework.cloud.stream.function.StreamBridge.resolveDestination(StreamBridge.java:296) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
          at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:213) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
          at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:170) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
          at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:150) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
          at news.kafka.com.ivanfranchin.producercloudstream.NewsEventProducer.send(NewsEventProducer.java:27) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
          at news.rest.com.ivanfranchin.producercloudstream.NewsController.publish(NewsController.java:29) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
          at java.lang.reflect.Method.invoke(Method.java:566) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
          at org.springframework.web.reactive.result.method.InvocableHandlerMethod.lambda$invoke$0(InvocableHandlerMethod.java:144) ~[na:na]
          at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:125) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
          at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
          at reactor.core.publisher.MonoZip$ZipCoordinator.signal(MonoZip.java:251) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
          at reactor.core.publisher.MonoZip$ZipInner.onNext(MonoZip.java:336) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
          at reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.onNext(MonoPeekTerminal.java:180) ~[na:na]
          at reactor.core.publisher.FluxDefaultIfEmpty$DefaultIfEmptySubscriber.onNext(FluxDefaultIfEmpty.java:101) ~[na:na]
          at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:200) ~[na:na]
          at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:74) ~[na:na]
          at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79) ~[na:na]
          at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
          at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:151) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
          at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onNext(FluxContextWrite.java:107) ~[na:na]
          at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onNext(FluxMapFuseable.java:299) ~[na:na]
          at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onNext(FluxFilterFuseable.java:337) ~[na:na]
          at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
          at reactor.core.publisher.MonoCollect$CollectSubscriber.onComplete(MonoCollect.java:159) ~[na:na]
          at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:144) ~[na:na]
          at reactor.core.publisher.FluxPeek$PeekSubscriber.onComplete(FluxPeek.java:260) ~[na:na]
          at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:144) ~[na:na]
          at reactor.netty.channel.FluxReceive.onInboundComplete(FluxReceive.java:400) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:1.0.19]
          at reactor.netty.channel.ChannelOperations.onInboundComplete(ChannelOperations.java:419) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:1.0.19]
          at reactor.netty.http.server.HttpServerOperations.onInboundNext(HttpServerOperations.java:600) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:1.0.19]
          at reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:93) ~[na:na]
          at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at reactor.netty.http.server.HttpTrafficHandler.channelRead(HttpTrafficHandler.java:266) ~[na:na]
          at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:327) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:299) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) ~[na:na]
          at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
          at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:722) ~[na:na]
          at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:658) ~[na:na]
          at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:584) ~[na:na]
          at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496) ~[na:na]
          at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:995) ~[na:na]
          at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[na:na]
          at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[na:na]
          at java.lang.Thread.run(Thread.java:829) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
          at com.oracle.svm.core.thread.PlatformThreads.threadStartRoutine(PlatformThreads.java:704) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
          at com.oracle.svm.core.posix.thread.PosixPlatformThreads.pthreadStartRoutine(PosixPlatformThreads.java:202) ~[na:na]
  Caused by: java.lang.NoSuchMethodException: org.springframework.boot.autoconfigure.condition.OnBeanCondition.<init>()
      at java.lang.Class.getConstructor0(DynamicHub.java:3349) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
      at java.lang.Class.getDeclaredConstructor(DynamicHub.java:2553) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
      at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:25) ~[na:na]
      at org.springframework.context.annotation.ConditionEvaluator.getCondition(ConditionEvaluator.java:125) ~[na:na]
      at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:96) ~[na:na]
      at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:88) ~[na:na]
      at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:71) ~[na:na]
      at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.doRegisterBean(AnnotatedBeanDefinitionReader.java:254) ~[na:na]
      at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.registerBean(AnnotatedBeanDefinitionReader.java:147) ~[na:na]
      at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.register(AnnotatedBeanDefinitionReader.java:137) ~[na:na]
      at org.springframework.context.annotation.AnnotationConfigApplicationContext.register(AnnotationConfigApplicationContext.java:168) ~[na:na]
      at org.springframework.cloud.stream.binder.DefaultBinderFactory.initializeBinderContextSimple(DefaultBinderFactory.java:410) ~[na:na]
      at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinderInstance(DefaultBinderFactory.java:265) ~[na:na]
      at org.springframework.cloud.stream.binder.DefaultBinderFactory.doGetBinder(DefaultBinderFactory.java:223) ~[na:na]
      at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinder(DefaultBinderFactory.java:151) ~[na:na]
      at org.springframework.cloud.stream.binding.BindingService.getBinder(BindingService.java:394) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
      at org.springframework.cloud.stream.binding.BindingService.bindProducer(BindingService.java:277) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
      at org.springframework.cloud.stream.function.StreamBridge.resolveDestination(StreamBridge.java:296) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
      at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:213) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
      at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:170) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
      at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:150) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.2.4]
      at news.kafka.com.ivanfranchin.producercloudstream.NewsEventProducer.send(NewsEventProducer.java:27) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
      at news.rest.com.ivanfranchin.producercloudstream.NewsController.publish(NewsController.java:29) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
      at java.lang.reflect.Method.invoke(Method.java:566) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
      at org.springframework.web.reactive.result.method.InvocableHandlerMethod.lambda$invoke$0(InvocableHandlerMethod.java:144) ~[na:na]
      at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:125) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
      at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
      at reactor.core.publisher.MonoZip$ZipCoordinator.signal(MonoZip.java:251) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
      at reactor.core.publisher.MonoZip$ZipInner.onNext(MonoZip.java:336) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
      at reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.onNext(MonoPeekTerminal.java:180) ~[na:na]
      at reactor.core.publisher.FluxDefaultIfEmpty$DefaultIfEmptySubscriber.onNext(FluxDefaultIfEmpty.java:101) ~[na:na]
      at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:200) ~[na:na]
      at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:74) ~[na:na]
      at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79) ~[na:na]
      at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
      at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:151) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
      at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onNext(FluxContextWrite.java:107) ~[na:na]
      at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onNext(FluxMapFuseable.java:299) ~[na:na]
      at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onNext(FluxFilterFuseable.java:337) ~[na:na]
      at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:3.4.18]
      at reactor.core.publisher.MonoCollect$CollectSubscriber.onComplete(MonoCollect.java:159) ~[na:na]
      at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:144) ~[na:na]
      at reactor.core.publisher.FluxPeek$PeekSubscriber.onComplete(FluxPeek.java:260) ~[na:na]
      at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:144) ~[na:na]
      at reactor.netty.channel.FluxReceive.onInboundComplete(FluxReceive.java:400) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:1.0.19]
      at reactor.netty.channel.ChannelOperations.onInboundComplete(ChannelOperations.java:419) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:1.0.19]
      at reactor.netty.http.server.HttpServerOperations.onInboundNext(HttpServerOperations.java:600) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:1.0.19]
      at reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:93) ~[na:na]
      at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at reactor.netty.http.server.HttpTrafficHandler.channelRead(HttpTrafficHandler.java:266) ~[na:na]
      at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:327) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:299) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) ~[na:na]
      at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:4.1.77.Final]
      at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:722) ~[na:na]
      at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:658) ~[na:na]
      at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:584) ~[na:na]
      at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496) ~[na:na]
      at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:995) ~[na:na]
      at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[na:na]
      at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[na:na]
      at java.lang.Thread.run(Thread.java:829) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
      at com.oracle.svm.core.thread.PlatformThreads.threadStartRoutine(PlatformThreads.java:704) ~[com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication:na]
      at com.oracle.svm.core.posix.thread.PosixPlatformThreads.pthreadStartRoutine(PosixPlatformThreads.java:202) ~[na:na]
  ```

- `consumer-service` Docker native images is built successfully. However, the following exception is thrown at startup
  ```
  ERROR 1 --- [           main] o.s.b.d.LoggingFailureAnalysisReporter   :
  
  ***************************
  APPLICATION FAILED TO START
  ***************************
  
  Description:
  
  Native reflection configuration for org.springframework.boot.autoconfigure.condition.OnBeanCondition.<init>() is missing.
  
  Action:
  
  Native configuration for a method accessed reflectively is likely missing.
  You can try to configure native hints in order to specify it explicitly.
  See https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#native-hints for more details.  
  ```
