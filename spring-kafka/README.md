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
    - Native
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

- After building the `producer-kafka` Docker Native Image and starting it successfully, when sending the first request, it's throwing the following exception.
  ```
    .   ____          _            __ _ _
   /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
  ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
   \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
    '  |____| .__|_| |_|_| |_\__, | / / / /
   =========|_|==============|___/=/_/_/_/
   :: Spring Boot ::                (v2.4.5)
  
  2021-04-25 19:31:06.680  INFO 1 --- [           main] c.m.p.ProducerKafkaApplication           : Starting ProducerKafkaApplication using Java 11.0.10 on 202f6cf2b8eb with PID 1 (/workspace/com.mycompany.producerkafka.ProducerKafkaApplication started by cnb in /workspace)
  2021-04-25 19:31:06.680  INFO 1 --- [           main] c.m.p.ProducerKafkaApplication           : No active profile set, falling back to default profiles: default
  2021-04-25 19:31:06.773  WARN 1 --- [           main] i.m.c.i.binder.jvm.JvmGcMetrics          : GC notifications will not be available because MemoryPoolMXBeans are not provided by the JVM
  2021-04-25 19:31:06.777  INFO 1 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 6 endpoint(s) beneath base path '/actuator'
  2021-04-25 19:31:06.790  INFO 1 --- [           main] o.a.k.clients.admin.AdminClientConfig    : AdminClientConfig values:
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
  	ssl.cipher.suites = null
  	ssl.enabled.protocols = [TLSv1.2, TLSv1.3]
  	ssl.endpoint.identification.algorithm = https
  	ssl.engine.factory.class = null
  	ssl.key.password = null
  	ssl.keymanager.algorithm = SunX509
  	ssl.keystore.location = null
  	ssl.keystore.password = null
  	ssl.keystore.type = JKS
  	ssl.protocol = TLSv1.3
  	ssl.provider = null
  	ssl.secure.random.implementation = null
  	ssl.trustmanager.algorithm = PKIX
  	ssl.truststore.location = null
  	ssl.truststore.password = null
  	ssl.truststore.type = JKS
  
  2021-04-25 19:31:06.794  WARN 1 --- [           main] o.a.kafka.common.utils.AppInfoParser     : Error while loading kafka-version.properties: inStream parameter is null
  2021-04-25 19:31:06.794  INFO 1 --- [           main] o.a.kafka.common.utils.AppInfoParser     : Kafka version: unknown
  2021-04-25 19:31:06.794  INFO 1 --- [           main] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: unknown
  2021-04-25 19:31:06.795  INFO 1 --- [           main] o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1619379066794
  2021-04-25 19:31:06.820  INFO 1 --- [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 8080
  2021-04-25 19:31:06.822  INFO 1 --- [           main] c.m.p.ProducerKafkaApplication           : Started ProducerKafkaApplication in 0.154 seconds (JVM running for 0.157)
  Apr 25, 2021 7:34:26 PM com.fasterxml.jackson.databind.ext.Java7Handlers <clinit>
  WARNING: Unable to load JDK7 types (java.nio.file.Path): no Java7 type support added
  2021-04-25 19:34:26.488  INFO 1 --- [ctor-http-nio-2] c.m.producerkafka.kafka.NewsProducer     : Sending News 'News(id=cda58d50-de0d-49b7-8110-b6e69071770c, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)' to topic 'news.json'
  2021-04-25 19:34:26.490  INFO 1 --- [ctor-http-nio-2] o.a.k.clients.producer.ProducerConfig    : ProducerConfig values:
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
  	key.serializer = class org.apache.kafka.common.serialization.StringSerializer
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
  	ssl.cipher.suites = null
  	ssl.enabled.protocols = [TLSv1.2, TLSv1.3]
  	ssl.endpoint.identification.algorithm = https
  	ssl.engine.factory.class = null
  	ssl.key.password = null
  	ssl.keymanager.algorithm = SunX509
  	ssl.keystore.location = null
  	ssl.keystore.password = null
  	ssl.keystore.type = JKS
  	ssl.protocol = TLSv1.3
  	ssl.provider = null
  	ssl.secure.random.implementation = null
  	ssl.trustmanager.algorithm = PKIX
  	ssl.truststore.location = null
  	ssl.truststore.password = null
  	ssl.truststore.type = JKS
  	transaction.timeout.ms = 60000
  	transactional.id = null
  	value.serializer = class org.springframework.kafka.support.serializer.JsonSerializer
  
  2021-04-25 19:34:26.494  INFO 1 --- [ctor-http-nio-2] o.a.k.clients.producer.KafkaProducer     : [Producer clientId=producer-1] Closing the Kafka producer with timeoutMillis = 0 ms.
  2021-04-25 19:34:26.508 ERROR 1 --- [ctor-http-nio-2] a.w.r.e.AbstractErrorWebExceptionHandler : [a83db33d-1]  500 Server Error for HTTP POST "/api/news"
  
  org.apache.kafka.common.KafkaException: Failed to construct kafka producer
  	at org.apache.kafka.clients.producer.KafkaProducer.<init>(KafkaProducer.java:441) ~[na:na]
  	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:
  Error has been observed at the following site(s):
  	|_ checkpoint ? org.springframework.boot.actuate.metrics.web.reactive.server.MetricsWebFilter [DefaultWebFilterChain]
  	|_ checkpoint ? HTTP POST "/api/news" [ExceptionHandlingWebHandler]
  Stack trace:
  		at org.apache.kafka.clients.producer.KafkaProducer.<init>(KafkaProducer.java:441) ~[na:na]
  		at org.apache.kafka.clients.producer.KafkaProducer.<init>(KafkaProducer.java:290) ~[na:na]
  		at org.springframework.kafka.core.DefaultKafkaProducerFactory.createRawProducer(DefaultKafkaProducerFactory.java:743) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  		at org.springframework.kafka.core.DefaultKafkaProducerFactory.createKafkaProducer(DefaultKafkaProducerFactory.java:584) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  		at org.springframework.kafka.core.DefaultKafkaProducerFactory.doCreateProducer(DefaultKafkaProducerFactory.java:544) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  		at org.springframework.kafka.core.DefaultKafkaProducerFactory.createProducer(DefaultKafkaProducerFactory.java:519) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  		at org.springframework.kafka.core.DefaultKafkaProducerFactory.createProducer(DefaultKafkaProducerFactory.java:513) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  		at org.springframework.kafka.core.KafkaTemplate.getTheProducer(KafkaTemplate.java:666) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  		at org.springframework.kafka.core.KafkaTemplate.doSend(KafkaTemplate.java:552) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
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
  		at reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:94) ~[com.mycompany.producerkafka.ProducerKafkaApplication:1.0.6]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at reactor.netty.http.server.HttpTrafficHandler.channelRead(HttpTrafficHandler.java:253) ~[com.mycompany.producerkafka.ProducerKafkaApplication:1.0.6]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436) ~[na:na]
  		at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:324) ~[na:na]
  		at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:296) ~[na:na]
  		at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  		at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
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
  Caused by: org.apache.kafka.common.KafkaException: Could not find a public no-argument constructor for org.springframework.kafka.support.serializer.JsonSerializer
  	at org.apache.kafka.common.utils.Utils.newInstance(Utils.java:349) ~[na:na]
  	at org.apache.kafka.common.config.AbstractConfig.getConfiguredInstance(AbstractConfig.java:377) ~[na:na]
  	at org.apache.kafka.common.config.AbstractConfig.getConfiguredInstance(AbstractConfig.java:399) ~[na:na]
  	at org.apache.kafka.clients.producer.KafkaProducer.<init>(KafkaProducer.java:374) ~[na:na]
  	at org.apache.kafka.clients.producer.KafkaProducer.<init>(KafkaProducer.java:290) ~[na:na]
  	at org.springframework.kafka.core.DefaultKafkaProducerFactory.createRawProducer(DefaultKafkaProducerFactory.java:743) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  	at org.springframework.kafka.core.DefaultKafkaProducerFactory.createKafkaProducer(DefaultKafkaProducerFactory.java:584) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  	at org.springframework.kafka.core.DefaultKafkaProducerFactory.doCreateProducer(DefaultKafkaProducerFactory.java:544) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  	at org.springframework.kafka.core.DefaultKafkaProducerFactory.createProducer(DefaultKafkaProducerFactory.java:519) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  	at org.springframework.kafka.core.DefaultKafkaProducerFactory.createProducer(DefaultKafkaProducerFactory.java:513) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  	at org.springframework.kafka.core.KafkaTemplate.getTheProducer(KafkaTemplate.java:666) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
  	at org.springframework.kafka.core.KafkaTemplate.doSend(KafkaTemplate.java:552) ~[com.mycompany.producerkafka.ProducerKafkaApplication:2.6.7]
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
  	at reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:94) ~[com.mycompany.producerkafka.ProducerKafkaApplication:1.0.6]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at reactor.netty.http.server.HttpTrafficHandler.channelRead(HttpTrafficHandler.java:253) ~[com.mycompany.producerkafka.ProducerKafkaApplication:1.0.6]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436) ~[na:na]
  	at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:324) ~[na:na]
  	at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:296) ~[na:na]
  	at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
  	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) ~[com.mycompany.producerkafka.ProducerKafkaApplication:4.1.63.Final]
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
  Caused by: java.lang.NoSuchMethodException: org.springframework.kafka.support.serializer.JsonSerializer.<init>()
  	at java.lang.Class.getConstructor0(DynamicHub.java:3349) ~[na:na]
  	at java.lang.Class.getDeclaredConstructor(DynamicHub.java:2553) ~[na:na]
  	at org.apache.kafka.common.utils.Utils.newInstance(Utils.java:347) ~[na:na]
  	... 68 common frames omitted
  ```

- After building the `consumer-kafka` Docker Native Image successfully, when starting it, it's throwing the following exception.
  ```
    .   ____          _            __ _ _
   /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
  ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
   \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
    '  |____| .__|_| |_|_| |_\__, | / / / /
   =========|_|==============|___/=/_/_/_/
   :: Spring Boot ::                (v2.4.5)
  
  2021-04-25 19:31:26.526  INFO 1 --- [           main] c.m.c.ConsumerKafkaApplication           : Starting ConsumerKafkaApplication using Java 11.0.10 on 439f11873949 with PID 1 (/workspace/com.mycompany.consumerkafka.ConsumerKafkaApplication started by cnb in /workspace)
  2021-04-25 19:31:26.527  INFO 1 --- [           main] c.m.c.ConsumerKafkaApplication           : No active profile set, falling back to default profiles: default
  2021-04-25 19:31:26.588  WARN 1 --- [           main] onfigReactiveWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'newsConsumer' defined in class path resource [com/mycompany/consumerkafka/kafka/NewsConsumer.class]: Initialization of bean failed; nested exception is java.lang.NullPointerException
  2021-04-25 19:31:26.591  INFO 1 --- [           main] ConditionEvaluationReportLoggingListener :
  
  Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
  2021-04-25 19:31:26.592 ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed
  
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
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:782) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.4.5]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:774) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.4.5]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.4.5]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:339) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.4.5]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1340) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.4.5]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1329) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.4.5]
  	at com.mycompany.consumerkafka.ConsumerKafkaApplication.main(ConsumerKafkaApplication.java:16) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:na]
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