# springboot-cloudkarafka
## `> spring-kafka`

In this example, we use [`Spring Kafka`](https://docs.spring.io/spring-kafka/reference/html/) library to implement the configuration between `Spring Boot` applications and `Kafka`.

## Applications

- ### producer-kafka

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news`. Once a request is made, `producer-kafka` pushes a message about the `news` to Kafka.

  Endpoint
  ```
  POST /api/news {"source": "...", "title": "..."}
  ```

- ### consumer-kafka

  `Spring Boot` Web Java application that listens to messages (published by the `producer-kafka`) and logs it.

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
      ./mvnw clean spring-boot:run --projects spring-kafka/producer-kafka \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9080"
      ```

  - **consumer-kafka**

    - Open another terminal and make sure you are in `springboot-cloudkarafka` root folder
  
    - Run application
      ```
      ./mvnw clean spring-boot:run --projects spring-kafka/consumer-kafka \
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
    | -----------------------  | ----------- |
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

- In a terminal, post a news
  ```
  http :9080/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
  ```

  **producer-kafka** logs
  ```
  INFO 6818 --- [nio-9080-exec-1] c.m.producerkafka.kafka.NewsProducer : Sending News 'News(id=17d3ddb1-ec35-449f-89b2-4e93ddb5e88b, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)' to topic '2gxxxxxx-news.json'
  ```

  **consumer-kafka** logs
  ```
  INFO 6834 --- [ntainer#0-0-C-1] c.m.consumerkafka.kafka.NewsConsumer : Received message
  ---
  TOPIC: 2gxxxxxx-news.json; PARTITION: 0; OFFSET: 2;
  PAYLOAD: News(id=17d3ddb1-ec35-449f-89b2-4e93ddb5e88b, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)
  ---
  ```

## Stop applications

- If they were started with `Maven`, go to the terminals where they are running and press `Ctrl+C`

- If they were started as Docker containers, run the script below
  ```
  ./stop-spring-kafka-apps.sh
  ```

## Issues

- After building the `producer-kafka` Docker Native Image and starting it successfully, when sending the first request, the exception below is thrown
  > **Note:** It can be fixed by adding `@TypeHint(types = News.class)` in `ProducerKafkaApplication` as explained in this [issue #659](https://github.com/spring-projects-experimental/spring-native/issues/659). However, `spring-native` dependency (together with `spring-aot-maven-plugin`) is defined inside `native` profile. This way `@TypeHint` annotation is not available.
  ```
  ERROR 1 --- [ctor-http-nio-2] a.w.r.e.AbstractErrorWebExceptionHandler : [63d5385d-1]  500 Server Error for HTTP POST "/api/news"
  
  org.apache.kafka.common.errors.SerializationException: Can't serialize data [News(id=70424098-ca76-4876-8520-8a0a5c6a294d, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)] for topic [news.json]
  	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:
  Error has been observed at the following site(s):
  	|_ checkpoint ? org.springframework.boot.actuate.metrics.web.reactive.server.MetricsWebFilter [DefaultWebFilterChain]
  	|_ checkpoint ? HTTP POST "/api/news" [ExceptionHandlingWebHandler]
  Stack trace:
  Caused by: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class com.mycompany.producerkafka.domain.News and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)
  	at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1277) ~[na:na]
  	at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:400) ~[na:na]
  	at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:71) ~[na:na]
  	at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:33) ~[na:na]
  	at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480) ~[na:na]
  	at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319) ~[na:na]
  	at com.fasterxml.jackson.databind.ObjectWriter$Prefetch.serialize(ObjectWriter.java:1516) ~[na:na]
  	at com.fasterxml.jackson.databind.ObjectWriter._writeValueAndClose(ObjectWriter.java:1217) ~[na:na]
  	at com.fasterxml.jackson.databind.ObjectWriter.writeValueAsBytes(ObjectWriter.java:1110) ~[na:na]
  	at org.springframework.kafka.support.serializer.JsonSerializer.serialize(JsonSerializer.java:195) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  	at org.springframework.kafka.support.serializer.JsonSerializer.serialize(JsonSerializer.java:185) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  	at org.apache.kafka.clients.producer.KafkaProducer.doSend(KafkaProducer.java:910) ~[na:na]
  	at org.apache.kafka.clients.producer.KafkaProducer.send(KafkaProducer.java:870) ~[na:na]
  	at org.springframework.kafka.core.DefaultKafkaProducerFactory$CloseSafeProducer.send(DefaultKafkaProducerFactory.java:862) ~[na:na]
  	at org.springframework.kafka.core.KafkaTemplate.doSend(KafkaTemplate.java:563) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  	at org.springframework.kafka.core.KafkaTemplate.send(KafkaTemplate.java:369) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  	at com.mycompany.producerkafka.kafka.NewsProducer.send(NewsProducer.java:21) ~[com.mycompany.producerkafka.ProducerKafkaApplication:na]
  	at com.mycompany.producerkafka.rest.NewsController.publishNews(NewsController.java:25) ~[com.mycompany.producerkafka.ProducerKafkaApplication:na]
  	at java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
  	at org.springframework.web.reactive.result.method.InvocableHandlerMethod.lambda$invoke$0(InvocableHandlerMethod.java:146) ~[na:na]
  	at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:125) ~[com.mycompany.producerkafka.ProducerKafkaApplication:3.4.5]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1815) ~[com.mycompany.producerkafka.ProducerKafkaApplication:3.4.5]
  	at reactor.core.publisher.MonoZip$ZipCoordinator.signal(MonoZip.java:251) ~[com.mycompany.producerkafka.ProducerKafkaApplication:3.4.5]
  	at reactor.core.publisher.MonoZip$ZipInner.onNext(MonoZip.java:336) ~[com.mycompany.producerkafka.ProducerKafkaApplication:3.4.5]
  	at reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.onNext(MonoPeekTerminal.java:180) ~[na:na]
  	at reactor.core.publisher.FluxDefaultIfEmpty$DefaultIfEmptySubscriber.onNext(FluxDefaultIfEmpty.java:100) ~[na:na]
  	at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:199) ~[na:na]
  	at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:73) ~[na:na]
  	at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79) ~[na:na]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1815) ~[com.mycompany.producerkafka.ProducerKafkaApplication:3.4.5]
  	at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:151) ~[com.mycompany.producerkafka.ProducerKafkaApplication:3.4.5]
  	at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onNext(FluxContextWrite.java:107) ~[na:na]
  	at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onNext(FluxMapFuseable.java:295) ~[na:na]
  	at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onNext(FluxFilterFuseable.java:337) ~[na:na]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1815) ~[com.mycompany.producerkafka.ProducerKafkaApplication:3.4.5]
  	at reactor.core.publisher.MonoCollect$CollectSubscriber.onComplete(MonoCollect.java:159) ~[na:na]
  	at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
  	at reactor.core.publisher.FluxPeek$PeekSubscriber.onComplete(FluxPeek.java:259) ~[na:na]
  	at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
  	at reactor.netty.channel.FluxReceive.onInboundComplete(FluxReceive.java:401) ~[com.mycompany.producerkafka.ProducerKafkaApplication:1.0.6]
  	at reactor.netty.channel.ChannelOperations.onInboundComplete(ChannelOperations.java:416) ~[com.mycompany.producerkafka.ProducerKafkaApplication:1.0.6]
  	at reactor.netty.http.server.HttpServerOperations.onInboundNext(HttpServerOperations.java:556) ~[na:na]
  	at reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:94) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[na:na]
  	at reactor.netty.http.server.HttpTrafficHandler.channelRead(HttpTrafficHandler.java:253) ~[na:na]
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
  	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989) ~[na:na]
  	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[na:na]
  	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[na:na]
  	at java.lang.Thread.run(Thread.java:834) ~[na:na]
  	at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:519) ~[na:na]
  	at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:192) ~[na:na]
  ```

- After building the `consumer-kafka` Docker Native Image successfully, the following exception is thrown at runtime
  ```
  ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed
  
  org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'newsConsumer' defined in class path resource [com/mycompany/consumerkafka/kafka/NewsConsumer.class]: Initialization of bean failed; nested exception is java.lang.NullPointerException
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:610) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:524) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[na:na]
  	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[na:na]
  	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:944) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:918) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:583) ~[na:na]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:63) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:782) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:774) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:na]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:339) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1340) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1329) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:na]
  	at com.mycompany.consumerkafka.ConsumerKafkaApplication.main(ConsumerKafkaApplication.java:10) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:na]
  Caused by: java.lang.NullPointerException: null
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.resolveExpression(KafkaListenerAnnotationBeanPostProcessor.java:735) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.7]
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.resolveExpressionAsString(KafkaListenerAnnotationBeanPostProcessor.java:689) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.7]
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.getEndpointGroupId(KafkaListenerAnnotationBeanPostProcessor.java:507) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.7]
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.processListener(KafkaListenerAnnotationBeanPostProcessor.java:429) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.7]
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.processKafkaListener(KafkaListenerAnnotationBeanPostProcessor.java:382) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.7]
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.postProcessAfterInitialization(KafkaListenerAnnotationBeanPostProcessor.java:310) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.7]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsAfterInitialization(AbstractAutowireCapableBeanFactory.java:437) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1790) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:602) ~[na:na]
  	... 16 common frames omitted
  ```