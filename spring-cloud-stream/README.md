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

- When trying to build the native image of `producer-cloud-stream`, the following exception is thrown, [see issue #2799](https://github.com/spring-cloud/spring-cloud-stream/issues/2799)
  ```
  Exception in thread "main" org.springframework.boot.context.properties.bind.BindException: Failed to bind properties under 'spring.cloud.stream.bindings.news-out-0.producer.partition-key-expression' to org.springframework.expression.Expression
  	at org.springframework.boot.context.properties.bind.Binder.handleBindError(Binder.java:392)
  	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:352)
  	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$4(Binder.java:478)
  	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:99)
  	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:87)
  	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:63)
  	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$5(Binder.java:482)
  	at org.springframework.boot.context.properties.bind.Binder$Context.withIncreasedDepth(Binder.java:596)
  	at org.springframework.boot.context.properties.bind.Binder$Context.withDataObject(Binder.java:582)
  	at org.springframework.boot.context.properties.bind.Binder.bindDataObject(Binder.java:480)
  	at org.springframework.boot.context.properties.bind.Binder.bindObject(Binder.java:419)
  	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:348)
  	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$4(Binder.java:478)
  	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:99)
  	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:87)
  	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:63)
  	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$5(Binder.java:482)
  	at org.springframework.boot.context.properties.bind.Binder$Context.withIncreasedDepth(Binder.java:596)
  	at org.springframework.boot.context.properties.bind.Binder$Context.withDataObject(Binder.java:582)
  	at org.springframework.boot.context.properties.bind.Binder.bindDataObject(Binder.java:480)
  	at org.springframework.boot.context.properties.bind.Binder.bindObject(Binder.java:419)
  	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:348)
  	at org.springframework.boot.context.properties.bind.Binder.lambda$bindAggregate$1(Binder.java:440)
  	at org.springframework.boot.context.properties.bind.Binder$Context.withSource(Binder.java:567)
  	at org.springframework.boot.context.properties.bind.Binder.lambda$bindAggregate$2(Binder.java:441)
  	at org.springframework.boot.context.properties.bind.AggregateElementBinder.bind(AggregateElementBinder.java:39)
  	at org.springframework.boot.context.properties.bind.MapBinder$EntryBinder.lambda$bindEntries$0(MapBinder.java:158)
  	at java.base/java.util.HashMap.computeIfAbsent(HashMap.java:1220)
  	at org.springframework.boot.context.properties.bind.MapBinder$EntryBinder.bindEntries(MapBinder.java:158)
  	at org.springframework.boot.context.properties.bind.MapBinder.bindAggregate(MapBinder.java:69)
  	at org.springframework.boot.context.properties.bind.AggregateBinder.bind(AggregateBinder.java:56)
  	at org.springframework.boot.context.properties.bind.Binder.lambda$bindAggregate$3(Binder.java:443)
  	at org.springframework.boot.context.properties.bind.Binder$Context.withIncreasedDepth(Binder.java:596)
  	at org.springframework.boot.context.properties.bind.Binder.bindAggregate(Binder.java:443)
  	at org.springframework.boot.context.properties.bind.Binder.bindObject(Binder.java:404)
  	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:348)
  	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$4(Binder.java:478)
  	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:99)
  	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:87)
  	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:63)
  	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$5(Binder.java:482)
  	at org.springframework.boot.context.properties.bind.Binder$Context.withIncreasedDepth(Binder.java:596)
  	at org.springframework.boot.context.properties.bind.Binder$Context.withDataObject(Binder.java:582)
  	at org.springframework.boot.context.properties.bind.Binder.bindDataObject(Binder.java:480)
  	at org.springframework.boot.context.properties.bind.Binder.bindObject(Binder.java:419)
  	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:348)
  	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:337)
  	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:267)
  	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:228)
  	at org.springframework.cloud.stream.binder.BinderChildContextInitializer.createBindingServiceProperties(BinderChildContextInitializer.java:110)
  	at org.springframework.cloud.stream.binder.BinderChildContextInitializer.processAheadOfTime(BinderChildContextInitializer.java:95)
  	at org.springframework.beans.factory.aot.BeanDefinitionMethodGeneratorFactory.getAotContributions(BeanDefinitionMethodGeneratorFactory.java:151)
  	at org.springframework.beans.factory.aot.BeanDefinitionMethodGeneratorFactory.getBeanDefinitionMethodGenerator(BeanDefinitionMethodGeneratorFactory.java:99)
  	at org.springframework.beans.factory.aot.BeanDefinitionMethodGeneratorFactory.getBeanDefinitionMethodGenerator(BeanDefinitionMethodGeneratorFactory.java:115)
  	at org.springframework.beans.factory.aot.BeanRegistrationsAotProcessor.processAheadOfTime(BeanRegistrationsAotProcessor.java:49)
  	at org.springframework.beans.factory.aot.BeanRegistrationsAotProcessor.processAheadOfTime(BeanRegistrationsAotProcessor.java:37)
  	at org.springframework.context.aot.BeanFactoryInitializationAotContributions.getContributions(BeanFactoryInitializationAotContributions.java:67)
  	at org.springframework.context.aot.BeanFactoryInitializationAotContributions.<init>(BeanFactoryInitializationAotContributions.java:49)
  	at org.springframework.context.aot.BeanFactoryInitializationAotContributions.<init>(BeanFactoryInitializationAotContributions.java:44)
  	at org.springframework.context.aot.ApplicationContextAotGenerator.lambda$processAheadOfTime$0(ApplicationContextAotGenerator.java:58)
  	at org.springframework.context.aot.ApplicationContextAotGenerator.withCglibClassHandler(ApplicationContextAotGenerator.java:67)
  	at org.springframework.context.aot.ApplicationContextAotGenerator.processAheadOfTime(ApplicationContextAotGenerator.java:53)
  	at org.springframework.context.aot.ContextAotProcessor.performAotProcessing(ContextAotProcessor.java:106)
  	at org.springframework.context.aot.ContextAotProcessor.doProcess(ContextAotProcessor.java:84)
  	at org.springframework.context.aot.ContextAotProcessor.doProcess(ContextAotProcessor.java:49)
  	at org.springframework.context.aot.AbstractAotProcessor.process(AbstractAotProcessor.java:82)
  	at org.springframework.boot.SpringApplicationAotProcessor.main(SpringApplicationAotProcessor.java:80)
  Caused by: org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.String] to type [@com.fasterxml.jackson.databind.annotation.JsonSerialize org.springframework.expression.Expression]
  	at org.springframework.boot.context.properties.bind.BindConverter.convert(BindConverter.java:118)
  	at org.springframework.boot.context.properties.bind.BindConverter.convert(BindConverter.java:100)
  	at org.springframework.boot.context.properties.bind.BindConverter.convert(BindConverter.java:92)
  	at org.springframework.boot.context.properties.bind.Binder.bindProperty(Binder.java:464)
  	at org.springframework.boot.context.properties.bind.Binder.bindObject(Binder.java:408)
  	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:348)
  	... 65 more
  ```
- The `consumer-cloud-stream` native image is built successfully, however, when using the `cloudkarafka` profile, it's throwing the following exception: 
  ```
  ERROR 1 --- [           main] o.s.c.s.b.k.p.KafkaTopicProvisioner      : Failed to obtain partition information for the topic (5......n-spring.cloud.stream.news).
  
  org.apache.kafka.common.KafkaException: Failed to construct kafka consumer
  	at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:830) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:665) ~[na:na]
  	at org.springframework.kafka.core.DefaultKafkaConsumerFactory.createRawConsumer(DefaultKafkaConsumerFactory.java:483) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.0.10]
  	at org.springframework.kafka.core.DefaultKafkaConsumerFactory.createKafkaConsumer(DefaultKafkaConsumerFactory.java:451) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.0.10]
  	at org.springframework.kafka.core.DefaultKafkaConsumerFactory.createKafkaConsumer(DefaultKafkaConsumerFactory.java:391) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.0.10]
  	at org.springframework.kafka.core.DefaultKafkaConsumerFactory.createConsumer(DefaultKafkaConsumerFactory.java:364) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.0.10]
  	at org.springframework.kafka.core.ConsumerFactory.createConsumer(ConsumerFactory.java:69) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.0.10]
  	at org.springframework.kafka.core.ConsumerFactory.createConsumer(ConsumerFactory.java:56) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.0.10]
  	at org.springframework.kafka.core.ConsumerFactory.createConsumer(ConsumerFactory.java:45) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.0.10]
  	at org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder.lambda$getPartitionInfo$7(KafkaMessageChannelBinder.java:1114) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binder.kafka.provisioning.KafkaTopicProvisioner.lambda$getPartitionsForTopic$7(KafkaTopicProvisioner.java:562) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.retry.support.RetryTemplate.doExecute(RetryTemplate.java:329) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at org.springframework.retry.support.RetryTemplate.execute(RetryTemplate.java:209) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at org.springframework.cloud.stream.binder.kafka.provisioning.KafkaTopicProvisioner.getPartitionsForTopic(KafkaTopicProvisioner.java:557) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder.getPartitionInfo(KafkaMessageChannelBinder.java:1111) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder.processTopic(KafkaMessageChannelBinder.java:891) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder.createConsumerEndpoint(KafkaMessageChannelBinder.java:611) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder.createConsumerEndpoint(KafkaMessageChannelBinder.java:168) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binder.AbstractMessageChannelBinder.doBindConsumer(AbstractMessageChannelBinder.java:517) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binder.AbstractMessageChannelBinder.doBindConsumer(AbstractMessageChannelBinder.java:102) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binder.AbstractBinder.bindConsumer(AbstractBinder.java:144) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binding.BindingService.doBindConsumer(BindingService.java:186) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binding.BindingService.bindConsumer(BindingService.java:139) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binding.AbstractBindableProxyFactory.createAndBindInputs(AbstractBindableProxyFactory.java:98) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binding.InputBindingLifecycle.doStartWithBindable(InputBindingLifecycle.java:58) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at java.base@17.0.7/java.util.LinkedHashMap$LinkedValues.forEach(LinkedHashMap.java:647) ~[na:na]
  	at org.springframework.cloud.stream.binding.AbstractBindingLifecycle.start(AbstractBindingLifecycle.java:57) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.cloud.stream.binding.InputBindingLifecycle.start(InputBindingLifecycle.java:34) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:4.0.4]
  	at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:179) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:6.0.11]
  	at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:357) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:6.0.11]
  	at java.base@17.0.7/java.lang.Iterable.forEach(Iterable.java:75) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:156) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:6.0.11]
  	at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:124) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:6.0.11]
  	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:958) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:6.0.11]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:611) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:6.0.11]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:66) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:734) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:436) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:312) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1306) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1295) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:3.1.3]
  	at com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication.main(ConsumerCloudStreamApplication.java:10) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:na]
  Caused by: org.apache.kafka.common.KafkaException: org.apache.kafka.common.KafkaException: Could not find a public no-argument constructor for org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler
  	at org.apache.kafka.common.network.SaslChannelBuilder.configure(SaslChannelBuilder.java:184) ~[na:na]
  	at org.apache.kafka.common.network.ChannelBuilders.create(ChannelBuilders.java:192) ~[na:na]
  	at org.apache.kafka.common.network.ChannelBuilders.clientChannelBuilder(ChannelBuilders.java:81) ~[na:na]
  	at org.apache.kafka.clients.ClientUtils.createChannelBuilder(ClientUtils.java:105) ~[na:na]
  	at org.apache.kafka.clients.consumer.KafkaConsumer.<init>(KafkaConsumer.java:737) ~[na:na]
  	... 41 common frames omitted
  Caused by: org.apache.kafka.common.KafkaException: Could not find a public no-argument constructor for org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler
  	at org.apache.kafka.common.utils.Utils.newInstance(Utils.java:397) ~[na:na]
  	at org.apache.kafka.common.network.SaslChannelBuilder.createClientCallbackHandler(SaslChannelBuilder.java:304) ~[na:na]
  	at org.apache.kafka.common.network.SaslChannelBuilder.configure(SaslChannelBuilder.java:148) ~[na:na]
  	... 45 common frames omitted
  Caused by: java.lang.NoSuchMethodException: org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler.<init>()
  	at java.base@17.0.7/java.lang.Class.checkMethod(DynamicHub.java:1040) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at java.base@17.0.7/java.lang.Class.getConstructor0(DynamicHub.java:1206) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at java.base@17.0.7/java.lang.Class.getDeclaredConstructor(DynamicHub.java:2754) ~[com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at org.apache.kafka.common.utils.Utils.newInstance(Utils.java:395) ~[na:na]
  	... 47 common frames omitted
  ```