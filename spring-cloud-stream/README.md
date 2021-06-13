# spring-cloud-stream-kafka-multi-topics-cloudkarafka
## `> spring-cloud-stream`

In this example, we use [`Spring Cloud Stream`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/) library to implement the configuration between `Spring Boot` applications and `Kafka`.

## Applications

- ### producer-cloud-stream

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news` or `alert`. Once a request is made, `producer-cloud-stream` pushes a message related to the `news` or `alert` to Kafka.

  Endpoints
  ```
  POST /api/news {"source": "...", "title": "..."}
  POST /api/alert {"level": "...", "message": "..."}
  ```

- ### consumer-cloud-stream

  `Spring Boot` Web Java application that listens to the messages (published by `producer-cloud-stream`) and logs it.

## Running applications using Maven

- #### Using CloudKarafka

  - **producer-cloud-stream**

    - Open a terminal and navigate to `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder

    - Export your `CloudKarafka` credentials to those environment variables
      ```
      export CLOUDKARAFKA_USERNAME=...
      export CLOUDKARAFKA_PASSWORD=...
      ```
    
    - Run application
      ```
      ./mvnw clean spring-boot:run --projects spring-cloud-stream/producer-cloud-stream \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9082" \
        -Dspring-boot.run.profiles=cloudkarafka
      ```

  - **consumer-cloud-stream**

    - Open another terminal and make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder
  
    - Export your `CloudKarafka` credentials to those environment variables
      ```
      export CLOUDKARAFKA_USERNAME=...
      export CLOUDKARAFKA_PASSWORD=...
      ```
  
    - Run application
      ```
      ./mvnw clean spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9083" \
        -Dspring-boot.run.profiles=cloudkarafka
      ```

- #### Using Kafka running locally

  > **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka#using-kafka-running-locally)  

  - **producer-cloud-stream**

    - Open a terminal and navigate to `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder
  
    - Run application
      ```
      ./mvnw clean spring-boot:run --projects spring-cloud-stream/producer-cloud-stream \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9082"
      ```

  - **consumer-cloud-stream**

    - Open another terminal and make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder
  
    - Run application
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

    - In a terminal, make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder

    - Export your `CloudKarafka` credentials to these environment variables
      ```
      export CLOUDKARAFKA_USERNAME=...
      export CLOUDKARAFKA_PASSWORD=...
      ```

    - Run the script below to start the docker containers
      ```
      ./start-spring-cloud-stream-apps.sh cloudkarafka
      ```

  - #### Using Kafka running locally

    > **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/spring-cloud-stream-kafka-multi-topics-cloudkarafka#using-kafka-running-locally)

    - In a terminal, make sure you are in `spring-cloud-stream-kafka-multi-topics-cloudkarafka` root folder

    - Run the script below to start the docker containers
      ```
      ./start-spring-cloud-stream-apps.sh
      ```

## Applications URLs

| Application           | URL                   |
| --------------------- | --------------------- |
| producer-cloud-stream | http://localhost:9082 |
| consumer-cloud-stream | http://localhost:9083 |

## Example of execution using CloudKarafka

> **Note:** In the call below, I am using [HTTPie](https://httpie.org/)

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
  http :9082/api/alert level=4 message="Tsunami is coming"
  ```

  **producer-kafka** logs
  ```
  INFO c.m.producerkafka.kafka.AlertProducer    : Sending Alert 'Alert(id=756a8dc8-21ca-4856-9a4d-a0b34c158b43, level=4, message=Tsunami is coming)' to topic '2gxxxxxx-alert.json'
  ```

  **consumer-kafka** logs
  ```
  INFO c.m.consumerkafka.kafka.NewsConsumer     : Received message
  ---
  TOPIC: 2gxxxxxx-alert.json; PARTITION: 0; OFFSET: 2;
  PAYLOAD: Alert(id=756a8dc8-21ca-4856-9a4d-a0b34c158b43, level=4, message=Tsunami is coming)
  ---
  ```

## Stop applications

- If they were started with `Maven`, go to the terminals where they are running and press `Ctrl+C`

- If they were started as Docker containers, run the script below
  ```
  ./stop-spring-cloud-stream-apps.sh
  ```

## Issues

### Profile `default`

- `producer-cloud-stream`

  After building and starting the application in Native mode, the following exception is thrown when it pushes a `news` or an `alert`. As there is a problem with the Message Converters, the message cannot be serialized. See https://github.com/spring-projects-experimental/spring-native/issues/816

  ```
  WARN 1 --- [ctor-http-nio-2] o.s.c.s.binder.DefaultBinderFactory      : Failed to add additional Message Converters from child context
  
  java.lang.NullPointerException: null
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinderInstance(DefaultBinderFactory.java:277) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.doGetBinder(DefaultBinderFactory.java:224) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinder(DefaultBinderFactory.java:152) ~[na:na]
  	at org.springframework.cloud.stream.binding.BindingService.getBinder(BindingService.java:386) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.binding.BindingService.bindProducer(BindingService.java:270) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.1.3]
  	at org.springframework.cloud.stream.function.StreamBridge.resolveDestination(StreamBridge.java:256) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:202) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:156) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:136) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at com.mycompany.producercloudstream.kafka.MessageProducer.send(MessageProducer.java:41) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at com.mycompany.producercloudstream.rest.NewsController.publishAlert(NewsController.java:34) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
  	at org.springframework.web.reactive.result.method.InvocableHandlerMethod.lambda$invoke$0(InvocableHandlerMethod.java:146) ~[na:na]
  	at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:125) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1815) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  	at reactor.core.publisher.MonoZip$ZipCoordinator.signal(MonoZip.java:251) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  	at reactor.core.publisher.MonoZip$ZipInner.onNext(MonoZip.java:336) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  	at reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.onNext(MonoPeekTerminal.java:180) ~[na:na]
  	at reactor.core.publisher.FluxDefaultIfEmpty$DefaultIfEmptySubscriber.onNext(FluxDefaultIfEmpty.java:100) ~[na:na]
  	at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:199) ~[na:na]
  	at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:73) ~[na:na]
  	at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79) ~[na:na]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1815) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  	at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:151) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  	at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onNext(FluxContextWrite.java:107) ~[na:na]
  	at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onNext(FluxMapFuseable.java:295) ~[na:na]
  	at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onNext(FluxFilterFuseable.java:337) ~[na:na]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1815) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  	at reactor.core.publisher.MonoCollect$CollectSubscriber.onComplete(MonoCollect.java:159) ~[na:na]
  	at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
  	at reactor.core.publisher.FluxPeek$PeekSubscriber.onComplete(FluxPeek.java:259) ~[na:na]
  	at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
  	at reactor.netty.channel.FluxReceive.onInboundComplete(FluxReceive.java:401) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:1.0.7]
  	at reactor.netty.channel.ChannelOperations.onInboundComplete(ChannelOperations.java:416) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:1.0.7]
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
  
  INFO 1 --- [ctor-http-nio-2] o.s.c.s.binder.DefaultBinderFactory      : Caching the binder: kafka
  INFO 1 --- [ctor-http-nio-2] o.s.c.s.binder.DefaultBinderFactory      : Retrieving cached binder: kafka
  INFO 1 --- [ctor-http-nio-2] o.s.c.s.b.k.p.KafkaTopicProvisioner      : Using kafka topic for outbound: alert.json
  INFO 1 --- [ctor-http-nio-2] o.a.k.clients.admin.AdminClientConfig    : AdminClientConfig values:
  	bootstrap.servers = [kafka:9092]
  	client.dns.lookup = use_all_dns_ips
  	client.id =
  	connections.max.idle.ms = 300000
  	default.api.timeout.ms = 60000
  	metadata.max.age.ms = 300000
  	metric.reporters = []
  	metrics.num.samples = 2
  	metrics.recording.level = INFO
  	metrics.sample.window.ms = 30000
  	receive.buffer.bytes = 65536
  	reconnect.backoff.max.ms = 1000
  	reconnect.backoff.ms = 50
  	request.timeout.ms = 30000
  	retries = 2147483647
  	retry.backoff.ms = 100
  	sasl.client.callback.handler.class = null
  	sasl.jaas.config = null
  	sasl.kerberos.kinit.cmd = /usr/bin/kinit
  	sasl.kerberos.min.time.before.relogin = 60000
  	sasl.kerberos.service.name = null
  	sasl.kerberos.ticket.renew.jitter = 0.05
  	sasl.kerberos.ticket.renew.window.factor = 0.8
  	sasl.login.callback.handler.class = null
  	sasl.login.class = null
  	sasl.login.refresh.buffer.seconds = 300
  	sasl.login.refresh.min.period.seconds = 60
  	sasl.login.refresh.window.factor = 0.8
  	sasl.login.refresh.window.jitter = 0.05
  	sasl.mechanism = GSSAPI
  	security.protocol = PLAINTEXT
  	security.providers = null
  	send.buffer.bytes = 131072
  	socket.connection.setup.timeout.max.ms = 127000
  	socket.connection.setup.timeout.ms = 10000
  	ssl.cipher.suites = null
  	ssl.enabled.protocols = [TLSv1.2, TLSv1.3]
  	ssl.endpoint.identification.algorithm = https
  	ssl.engine.factory.class = null
  	ssl.key.password = null
  	ssl.keymanager.algorithm = SunX509
  	ssl.keystore.certificate.chain = null
  	ssl.keystore.key = null
  	ssl.keystore.location = null
  	ssl.keystore.password = null
  	ssl.keystore.type = JKS
  	ssl.protocol = TLSv1.3
  	ssl.provider = null
  	ssl.secure.random.implementation = null
  	ssl.trustmanager.algorithm = PKIX
  	ssl.truststore.certificates = null
  	ssl.truststore.location = null
  	ssl.truststore.password = null
  	ssl.truststore.type = JKS
  
  INFO 1 --- [ctor-http-nio-2] o.a.kafka.common.utils.AppInfoParser     : Kafka version: 2.7.1
  INFO 1 --- [ctor-http-nio-2] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: 61dbce85d0d41457
  INFO 1 --- [ctor-http-nio-2] o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1623512235143
  INFO 1 --- [| adminclient-1] o.a.kafka.common.utils.AppInfoParser     : App info kafka.admin.client for adminclient-1 unregistered
  INFO 1 --- [| adminclient-1] org.apache.kafka.common.metrics.Metrics  : Metrics scheduler closed
  INFO 1 --- [| adminclient-1] org.apache.kafka.common.metrics.Metrics  : Closing reporter org.apache.kafka.common.metrics.JmxReporter
  INFO 1 --- [| adminclient-1] org.apache.kafka.common.metrics.Metrics  : Metrics reporters closed
  INFO 1 --- [ctor-http-nio-2] o.a.k.clients.producer.ProducerConfig    : ProducerConfig values:
  	acks = 1
  	batch.size = 16384
  	bootstrap.servers = [kafka:9092]
  	buffer.memory = 33554432
  	client.dns.lookup = use_all_dns_ips
  	client.id = producer-1
  	compression.type = none
  	connections.max.idle.ms = 540000
  	delivery.timeout.ms = 120000
  	enable.idempotence = false
  	interceptor.classes = []
  	internal.auto.downgrade.txn.commit = true
  	key.serializer = class org.apache.kafka.common.serialization.ByteArraySerializer
  	linger.ms = 0
  	max.block.ms = 60000
  	max.in.flight.requests.per.connection = 5
  	max.request.size = 1048576
  	metadata.max.age.ms = 300000
  	metadata.max.idle.ms = 300000
  	metric.reporters = []
  	metrics.num.samples = 2
  	metrics.recording.level = INFO
  	metrics.sample.window.ms = 30000
  	partitioner.class = class org.apache.kafka.clients.producer.internals.DefaultPartitioner
  	receive.buffer.bytes = 32768
  	reconnect.backoff.max.ms = 1000
  	reconnect.backoff.ms = 50
  	request.timeout.ms = 30000
  	retries = 2147483647
  	retry.backoff.ms = 100
  	sasl.client.callback.handler.class = null
  	sasl.jaas.config = null
  	sasl.kerberos.kinit.cmd = /usr/bin/kinit
  	sasl.kerberos.min.time.before.relogin = 60000
  	sasl.kerberos.service.name = null
  	sasl.kerberos.ticket.renew.jitter = 0.05
  	sasl.kerberos.ticket.renew.window.factor = 0.8
  	sasl.login.callback.handler.class = null
  	sasl.login.class = null
  	sasl.login.refresh.buffer.seconds = 300
  	sasl.login.refresh.min.period.seconds = 60
  	sasl.login.refresh.window.factor = 0.8
  	sasl.login.refresh.window.jitter = 0.05
  	sasl.mechanism = GSSAPI
  	security.protocol = PLAINTEXT
  	security.providers = null
  	send.buffer.bytes = 131072
  	socket.connection.setup.timeout.max.ms = 127000
  	socket.connection.setup.timeout.ms = 10000
  	ssl.cipher.suites = null
  	ssl.enabled.protocols = [TLSv1.2, TLSv1.3]
  	ssl.endpoint.identification.algorithm = https
  	ssl.engine.factory.class = null
  	ssl.key.password = null
  	ssl.keymanager.algorithm = SunX509
  	ssl.keystore.certificate.chain = null
  	ssl.keystore.key = null
  	ssl.keystore.location = null
  	ssl.keystore.password = null
  	ssl.keystore.type = JKS
  	ssl.protocol = TLSv1.3
  	ssl.provider = null
  	ssl.secure.random.implementation = null
  	ssl.trustmanager.algorithm = PKIX
  	ssl.truststore.certificates = null
  	ssl.truststore.location = null
  	ssl.truststore.password = null
  	ssl.truststore.type = JKS
  	transaction.timeout.ms = 60000
  	transactional.id = null
  	value.serializer = class org.apache.kafka.common.serialization.ByteArraySerializer
  
   INFO 1 --- [ctor-http-nio-2] o.a.kafka.common.utils.AppInfoParser     : Kafka version: 2.7.1
   INFO 1 --- [ctor-http-nio-2] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: 61dbce85d0d41457
   INFO 1 --- [ctor-http-nio-2] o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1623512235158
   INFO 1 --- [ad | producer-1] org.apache.kafka.clients.Metadata        : [Producer clientId=producer-1] Cluster ID: -P0GVXdJRGe539p8SEzbQg
   INFO 1 --- [ctor-http-nio-2] o.a.k.clients.producer.KafkaProducer     : [Producer clientId=producer-1] Closing the Kafka producer with timeoutMillis = 30000 ms.
   INFO 1 --- [ctor-http-nio-2] org.apache.kafka.common.metrics.Metrics  : Metrics scheduler closed
   INFO 1 --- [ctor-http-nio-2] org.apache.kafka.common.metrics.Metrics  : Closing reporter org.apache.kafka.common.metrics.JmxReporter
   INFO 1 --- [ctor-http-nio-2] org.apache.kafka.common.metrics.Metrics  : Metrics reporters closed
   INFO 1 --- [ctor-http-nio-2] o.a.kafka.common.utils.AppInfoParser     : App info kafka.producer for producer-1 unregistered
   INFO 1 --- [ctor-http-nio-2] o.s.c.s.m.DirectWithAttributesChannel    : Channel 'unknown.channel.name' has 1 subscriber(s).
  ERROR 1 --- [ctor-http-nio-2] a.w.r.e.AbstractErrorWebExceptionHandler : [2384230a-1]  500 Server Error for HTTP POST "/api/alert"
  
  org.springframework.expression.spel.SpelEvaluationException: EL1008E: Property or field 'headers' cannot be found on object of type 'org.springframework.messaging.support.GenericMessage' - maybe not public or not valid?
  	at org.springframework.expression.spel.ast.PropertyOrFieldReference.readProperty(PropertyOrFieldReference.java:217) ~[na:na]
  	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:
  Error has been observed at the following site(s):
  	|_ checkpoint ? org.springframework.boot.actuate.metrics.web.reactive.server.MetricsWebFilter [DefaultWebFilterChain]
  	|_ checkpoint ? HTTP POST "/api/alert" [ExceptionHandlingWebHandler]
  Stack trace:
  		at org.springframework.expression.spel.ast.PropertyOrFieldReference.readProperty(PropertyOrFieldReference.java:217) ~[na:na]
  		at org.springframework.expression.spel.ast.PropertyOrFieldReference.getValueInternal(PropertyOrFieldReference.java:104) ~[na:na]
  		at org.springframework.expression.spel.ast.PropertyOrFieldReference.getValueInternal(PropertyOrFieldReference.java:91) ~[na:na]
  		at org.springframework.expression.spel.ast.CompoundExpression.getValueRef(CompoundExpression.java:55) ~[na:na]
  		at org.springframework.expression.spel.ast.CompoundExpression.getValueInternal(CompoundExpression.java:91) ~[na:na]
  		at org.springframework.expression.spel.ast.SpelNodeImpl.getValue(SpelNodeImpl.java:112) ~[na:na]
  		at org.springframework.expression.spel.standard.SpelExpression.getValue(SpelExpression.java:337) ~[na:na]
  		at org.springframework.cloud.stream.binder.PartitionHandler.extractKey(PartitionHandler.java:140) ~[na:na]
  		at org.springframework.cloud.stream.binder.PartitionHandler.determinePartition(PartitionHandler.java:121) ~[na:na]
  		at org.springframework.cloud.stream.function.PartitionAwareFunctionWrapper.lambda$new$0(PartitionAwareFunctionWrapper.java:63) ~[na:na]
  		at org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry$FunctionInvocationWrapper.convertOutputIfNecessary(SimpleFunctionRegistry.java:1003) ~[na:na]
  		at org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry$FunctionInvocationWrapper.apply(SimpleFunctionRegistry.java:492) ~[na:na]
  		at org.springframework.cloud.stream.function.PartitionAwareFunctionWrapper.apply(PartitionAwareFunctionWrapper.java:77) ~[na:na]
  		at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:214) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  		at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:156) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  		at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:136) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  		at com.mycompany.producercloudstream.kafka.MessageProducer.send(MessageProducer.java:41) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  		at com.mycompany.producercloudstream.rest.NewsController.publishAlert(NewsController.java:34) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  		at java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
  		at org.springframework.web.reactive.result.method.InvocableHandlerMethod.lambda$invoke$0(InvocableHandlerMethod.java:146) ~[na:na]
  		at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:125) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  		at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1815) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  		at reactor.core.publisher.MonoZip$ZipCoordinator.signal(MonoZip.java:251) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  		at reactor.core.publisher.MonoZip$ZipInner.onNext(MonoZip.java:336) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  		at reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.onNext(MonoPeekTerminal.java:180) ~[na:na]
  		at reactor.core.publisher.FluxDefaultIfEmpty$DefaultIfEmptySubscriber.onNext(FluxDefaultIfEmpty.java:100) ~[na:na]
  		at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:199) ~[na:na]
  		at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:73) ~[na:na]
  		at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79) ~[na:na]
  		at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1815) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  		at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:151) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  		at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onNext(FluxContextWrite.java:107) ~[na:na]
  		at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onNext(FluxMapFuseable.java:295) ~[na:na]
  		at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onNext(FluxFilterFuseable.java:337) ~[na:na]
  		at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1815) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.4.6]
  		at reactor.core.publisher.MonoCollect$CollectSubscriber.onComplete(MonoCollect.java:159) ~[na:na]
  		at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
  		at reactor.core.publisher.FluxPeek$PeekSubscriber.onComplete(FluxPeek.java:259) ~[na:na]
  		at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
  		at reactor.netty.channel.FluxReceive.onInboundComplete(FluxReceive.java:401) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:1.0.7]
  		at reactor.netty.channel.ChannelOperations.onInboundComplete(ChannelOperations.java:416) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:1.0.7]
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

### Profile `cloudkarafka`

- `producer-cloud-stream`

  After building and starting the application in Native mode, the following exception is thrown the first `news` or `alert` is submitted
  ```
  WARN 1 --- [ad | producer-1] o.apache.kafka.common.network.Selector   : [Producer clientId=producer-1] Unexpected error from ark-01.srvs.cloudkafka.com/xx.xxx.xxx.xxx; closing connection
  
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
  
  WARN 1 --- [ad | producer-1] org.apache.kafka.clients.NetworkClient   : [Producer clientId=producer-1] Connection to node -1 (ark-01.srvs.cloudkafka.com/xx.xxx.xxx.xxx:xxxx) terminated during authentication. This may happen due to any of the following reasons: (1) Authentication failed due to invalid credentials with brokers older than 1.0.0, (2) Firewall blocking Kafka TLS traffic (eg it may only allow HTTPS traffic), (3) Transient network issue.
  WARN 1 --- [ad | producer-1] org.apache.kafka.clients.NetworkClient   : [Producer clientId=producer-1] Bootstrap broker ark-01.srvs.cloudkafka.com:9094 (id: -1 rack: null) disconnected
  ```

- `consumer-cloud-stream`

  After building the application in Native mode, the following exception is thrown at startup time
  ```
  WARN 1 --- [           main] o.apache.kafka.common.network.Selector   : [Consumer clientId=consumer-consumerCloudStreamGroup-1, groupId=consumerCloudStreamGroup] Unexpected error from ark-02.srvs.cloudkafka.com/x.xxx.xxx.xx; closing connection
  
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
  	at org.apache.kafka.clients.consumer.internals.Fetcher.getTopicMetadata(Fetcher.java:375) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.partitionsFor(KafkaConsumer.java:1953) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.partitionsFor(KafkaConsumer.java:1921) ~[na:na]
  	at java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
  	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:344) ~[na:na]
  	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:208) ~[na:na]
  	at com.sun.proxy.$Proxy297.partitionsFor(Unknown Source) ~[na:na]
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
  	at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:178) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.8]
  	at org.springframework.context.support.DefaultLifecycleProcessor.access$200(DefaultLifecycleProcessor.java:54) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.8]
  	at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:356) ~[na:na]
  	at java.lang.Iterable.forEach(Iterable.java:75) ~[na:na]
  	at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:155) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.8]
  	at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:123) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.8]
  	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:935) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:586) ~[na:na]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:64) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:2.5.1]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:434) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:2.5.1]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:338) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:2.5.1]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1343) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:2.5.1]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1332) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:2.5.1]
  	at com.mycompany.consumercloudstream.ConsumerCloudStreamApplication.main(ConsumerCloudStreamApplication.java:28) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:na]
  
  WARN 1 --- [           main] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-consumerCloudStreamGroup-1, groupId=consumerCloudStreamGroup] Connection to node -2 (ark-02.srvs.cloudkafka.com/x.xxx.xxx.xx:xxx) terminated during authentication. This may happen due to any of the following reasons: (1) Authentication failed due to invalid credentials with brokers older than 1.0.0, (2) Firewall blocking Kafka TLS traffic (eg it may only allow HTTPS traffic), (3) Transient network issue.
  WARN 1 --- [           main] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-consumerCloudStreamGroup-1, groupId=consumerCloudStreamGroup] Bootstrap broker ark-02.srvs.cloudkafka.com:9094 (id: -2 rack: null) disconnected
  ```