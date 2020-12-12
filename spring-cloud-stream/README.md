# springboot-cloudkarafka
## `> spring-cloud-stream`

In this example, we use [`Spring Cloud Stream`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/) library to implement the configuration between `Spring Boot` applications and `Kafka`.

## Applications

- ### producer-cloud-stream

  `Spring Boot` Web Java application that exposes one endpoint at which users can post `news`. Once a request is made, `producer-cloud-stream` pushes a message about the `news` to Kafka.

  Endpoint
  ```
  POST /api/news {"source": "...", "title": "..."}
  ```

- ### consumer-cloud-stream

  `Spring Boot` Web Java application that listens to messages (published by the `producer-cloud-stream`) and logs it.

## Running applications using Maven

#### Using CloudKarafka

- **producer-cloud-stream**

  - Open a terminal and navigate to `springboot-cloudkarafka` root folder

  - Export your `CloudKarafka` credentials to those environment variables
    ```
    export CLOUDKARAFKA_USERNAME=...
    export CLOUDKARAFKA_PASSWORD=...
    ```
    
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-cloud-stream/producer-cloud-stream \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9082" \
      -Dspring-boot.run.profiles=cloudkarafka
    ```

- **consumer-cloud-stream**

  - Open another terminal and make sure you are in `springboot-cloudkarafka` root folder
  
  - Export your `CloudKarafka` credentials to those environment variables
    ```
    export CLOUDKARAFKA_USERNAME=...
    export CLOUDKARAFKA_PASSWORD=...
    ```
  
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9083" \
      -Dspring-boot.run.profiles=cloudkarafka
    ```

#### Using Kafka running locally

> **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/springboot-cloudkarafka#running-kafka-locally)  

- **producer-cloud-stream**

  - Open a terminal and navigate to `springboot-cloudkarafka` root folder
  
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-cloud-stream/producer-cloud-stream \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9082"
    ```

- **consumer-cloud-stream**

  - Open another terminal and make sure you are in `springboot-cloudkarafka` root folder
  
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9083"
    ```

## Running applications as Docker containers

### Build application's Docker image

- In a terminal, make sure you are in `springboot-cloudkarafka` root folder

- Run the following script to build the Docker images
  - JVM
    ```
    ./build-spring-cloud-stream-apps.sh
    ```
  - Native
    ```
    ./build-spring-cloud-stream-apps.sh native
    ```

### Application's Environment Variables

- **producer-cloud-stream** and **consumer-cloud-stream**

  | Environment Variable     | Description |
  | -----------------------  | ----------- |
  | `SPRING_PROFILES_ACTIVE` | Specify the type of profile to run the application. To use `CloudKarafka` set `cloudkarafka`. The `default` profile will use local `Kafka` |
  | `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value for `cloudkarafka` profile is `ark-01.srvs.cloudkafka.com:9094, ark-02.srvs.cloudkafka.com:9094, ark-03.srvs.cloudkafka.com:9094`. Using the `default` profile, the default value is `localhost:29092` |
  | `CLOUDKARAFKA_USERNAME`  | Specify your `CloudKarafka` username. Required when using `cloudkarafka` profile |
  | `CLOUDKARAFKA_PASSWORD`  | Specify your `CloudKarafka` password. Required when using `cloudkarafka` profile |

### Starting application's Docker container

#### Using CloudKarafka

- In a terminal, make sure you are in `springboot-cloudkarafka` root folder

- Export your `CloudKarafka` credentials to these environment variables
  ```
  export CLOUDKARAFKA_USERNAME=...
  export CLOUDKARAFKA_PASSWORD=...
  ```

- Run the script below to start the docker containers
  ```
  ./start-spring-cloud-stream-apps.sh cloudkarafka
  ```

#### Using Kafka running locally

> **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/springboot-cloudkarafka#running-kafka-locally)

- In a terminal, make sure you are in `springboot-cloudkarafka` root folder

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

- In a terminal, post a news
  ```
  http :9082/api/news source="Spring Boot Blog" title="Spring Boot and CloudKarafka"
  ```

  **producer-cloud-stream** logs
  ```
  INFO 5090 --- [nio-9082-exec-1] c.m.p.kafka.NewsProducer : Sending News 'News(id=04253f40-ff8e-4293-91ba-a570febda5e1, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)' to topic '2gxxxxxx-news.json'
  ```

  **consumer-cloud-stream** logs
  ```
  INFO 5066 --- [container-0-C-1] c.m.c.kafka.NewsConsumer : Received message
  ---
  TOPIC: 2gxxxxxx-news.json; PARTITION: 0; OFFSET: 1;
  PAYLOAD: News(id=04253f40-ff8e-4293-91ba-a570febda5e1, source=Spring Boot Blog, title=Spring Boot and CloudKarafka)
  ---
  ```

## Stop applications

- If they were started with `Maven`, go to the terminals where they are running and press `Ctrl+C`

- If they were started as Docker containers, run the script below
  ```
  ./stop-spring-cloud-stream-apps.sh
  ```

## Issues

- After building the `producer-cloud-stream` Docker Native Image, when running it, it's throwing the following exception.
  ```
    .   ____          _            __ _ _
   /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
  ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
   \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
    '  |____| .__|_| |_|_| |_\__, | / / / /
   =========|_|==============|___/=/_/_/_/
   :: Spring Boot ::                (v2.4.0)
  
  2020-12-09 18:44:16.355  INFO 1 --- [           main] c.m.p.ProducerCloudStreamApplication     : Starting ProducerCloudStreamApplication using Java 11.0.9 on ee2279b450ee with PID 1 (/workspace/com.mycompany.producercloudstream.ProducerCloudStreamApplication started by cnb in /workspace)
  2020-12-09 18:44:16.355  INFO 1 --- [           main] c.m.p.ProducerCloudStreamApplication     : The following profiles are active: cloudkarafka
  2020-12-09 18:44:16.402  WARN 1 --- [           main] onfigReactiveWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanDefinitionStoreException: Failed to process import candidates for configuration class [com.mycompany.producercloudstream.ProducerCloudStreamApplication]; nested exception is java.lang.IllegalArgumentException: Could not find class [java.util.function.Function]
  2020-12-09 18:44:16.403  INFO 1 --- [           main] ConditionEvaluationReportLoggingListener :
  
  Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
  2020-12-09 18:44:16.406 ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed
  
  org.springframework.beans.factory.BeanDefinitionStoreException: Failed to process import candidates for configuration class [com.mycompany.producercloudstream.ProducerCloudStreamApplication]; nested exception is java.lang.IllegalArgumentException: Could not find class [java.util.function.Function]
  	at org.springframework.context.annotation.ConfigurationClassParser.processImports(ConfigurationClassParser.java:610) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.access$800(ConfigurationClassParser.java:111) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser$DeferredImportSelectorGroupingHandler.lambda$processGroupImports$1(ConfigurationClassParser.java:812) ~[na:na]
  	at java.util.ArrayList.forEach(ArrayList.java:1541) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at org.springframework.context.annotation.ConfigurationClassParser$DeferredImportSelectorGroupingHandler.processGroupImports(ConfigurationClassParser.java:809) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser$DeferredImportSelectorHandler.process(ConfigurationClassParser.java:780) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.parse(ConfigurationClassParser.java:193) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassPostProcessor.processConfigBeanDefinitions(ConfigurationClassPostProcessor.java:336) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:5.3.1]
  	at org.springframework.context.annotation.ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry(ConfigurationClassPostProcessor.java:252) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:5.3.1]
  	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanDefinitionRegistryPostProcessors(PostProcessorRegistrationDelegate.java:285) ~[na:na]
  	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:99) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(AbstractApplicationContext.java:751) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:569) ~[na:na]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:63) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:767) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:759) ~[na:na]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:426) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:326) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1309) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1298) ~[na:na]
  	at com.mycompany.producercloudstream.ProducerCloudStreamApplication.main(ProducerCloudStreamApplication.java:10) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  Caused by: java.lang.IllegalArgumentException: Could not find class [java.util.function.Function]
  	at org.springframework.util.ClassUtils.resolveClassName(ClassUtils.java:334) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.adapt(TypeMappedAnnotation.java:446) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.getValue(TypeMappedAnnotation.java:369) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.asMap(TypeMappedAnnotation.java:284) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.adaptValueForMapOptions(TypeMappedAnnotation.java:315) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.asMap(TypeMappedAnnotation.java:287) ~[na:na]
  	at org.springframework.core.annotation.AbstractMergedAnnotation.asAnnotationAttributes(AbstractMergedAnnotation.java:193) ~[na:na]
  	at org.springframework.core.type.AnnotatedTypeMetadata.getAnnotationAttributes(AnnotatedTypeMetadata.java:106) ~[na:na]
  	at org.springframework.context.annotation.AnnotationConfigUtils.attributesForRepeatable(AnnotationConfigUtils.java:301) ~[na:na]
  	at org.springframework.context.annotation.AnnotationConfigUtils.attributesForRepeatable(AnnotationConfigUtils.java:291) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.doProcessConfigurationClass(ConfigurationClassParser.java:289) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.processConfigurationClass(ConfigurationClassParser.java:250) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.processMemberClasses(ConfigurationClassParser.java:372) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.doProcessConfigurationClass(ConfigurationClassParser.java:272) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.processConfigurationClass(ConfigurationClassParser.java:250) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.processImports(ConfigurationClassParser.java:600) ~[na:na]
  	... 20 common frames omitted
  Caused by: java.lang.ClassNotFoundException: java.util.function.Function
  	at com.oracle.svm.core.hub.ClassForNameSupport.forName(ClassForNameSupport.java:60) ~[na:na]
  	at java.lang.Class.forName(DynamicHub.java:1292) ~[na:na]
  	at org.springframework.util.ClassUtils.forName(ClassUtils.java:284) ~[na:na]
  	at org.springframework.util.ClassUtils.resolveClassName(ClassUtils.java:324) ~[na:na]
  	... 35 common frames omitted
  ```

- After building the `consumer-cloud-stream` Docker Native Image, when running it, it's throwing the following exception.
  ```
    .   ____          _            __ _ _
   /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
  ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
   \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
    '  |____| .__|_| |_|_| |_\__, | / / / /
   =========|_|==============|___/=/_/_/_/
   :: Spring Boot ::                (v2.4.0)
  
  2020-12-09 19:22:19.124  INFO 1 --- [           main] c.m.c.ConsumerCloudStreamApplication     : Starting ConsumerCloudStreamApplication using Java 11.0.9 on 9711c8998d5b with PID 1 (/workspace/com.mycompany.consumercloudstream.ConsumerCloudStreamApplication started by cnb in /workspace)
  2020-12-09 19:22:19.124  INFO 1 --- [           main] c.m.c.ConsumerCloudStreamApplication     : The following profiles are active: cloudkarafka
  2020-12-09 19:22:19.154  WARN 1 --- [           main] onfigReactiveWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanDefinitionStoreException: Failed to process import candidates for configuration class [com.mycompany.consumercloudstream.ConsumerCloudStreamApplication]; nested exception is java.lang.IllegalArgumentException: Could not find class [java.util.function.Function]
  2020-12-09 19:22:19.155  INFO 1 --- [           main] ConditionEvaluationReportLoggingListener :
  
  Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
  2020-12-09 19:22:19.157 ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed
  
  org.springframework.beans.factory.BeanDefinitionStoreException: Failed to process import candidates for configuration class [com.mycompany.consumercloudstream.ConsumerCloudStreamApplication]; nested exception is java.lang.IllegalArgumentException: Could not find class [java.util.function.Function]
  	at org.springframework.context.annotation.ConfigurationClassParser.processImports(ConfigurationClassParser.java:610) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.access$800(ConfigurationClassParser.java:111) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser$DeferredImportSelectorGroupingHandler.lambda$processGroupImports$1(ConfigurationClassParser.java:812) ~[na:na]
  	at java.util.ArrayList.forEach(ArrayList.java:1541) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at org.springframework.context.annotation.ConfigurationClassParser$DeferredImportSelectorGroupingHandler.processGroupImports(ConfigurationClassParser.java:809) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser$DeferredImportSelectorHandler.process(ConfigurationClassParser.java:780) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.parse(ConfigurationClassParser.java:193) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassPostProcessor.processConfigBeanDefinitions(ConfigurationClassPostProcessor.java:336) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.1]
  	at org.springframework.context.annotation.ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry(ConfigurationClassPostProcessor.java:252) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.1]
  	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanDefinitionRegistryPostProcessors(PostProcessorRegistrationDelegate.java:285) ~[na:na]
  	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:99) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(AbstractApplicationContext.java:751) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:569) ~[na:na]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:63) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:767) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:759) ~[na:na]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:426) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:326) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1309) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1298) ~[na:na]
  	at com.mycompany.consumercloudstream.ConsumerCloudStreamApplication.main(ConsumerCloudStreamApplication.java:10) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:na]
  Caused by: java.lang.IllegalArgumentException: Could not find class [java.util.function.Function]
  	at org.springframework.util.ClassUtils.resolveClassName(ClassUtils.java:334) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.adapt(TypeMappedAnnotation.java:446) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.getValue(TypeMappedAnnotation.java:369) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.asMap(TypeMappedAnnotation.java:284) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.adaptValueForMapOptions(TypeMappedAnnotation.java:315) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.asMap(TypeMappedAnnotation.java:287) ~[na:na]
  	at org.springframework.core.annotation.AbstractMergedAnnotation.asAnnotationAttributes(AbstractMergedAnnotation.java:193) ~[na:na]
  	at org.springframework.core.type.AnnotatedTypeMetadata.getAnnotationAttributes(AnnotatedTypeMetadata.java:106) ~[na:na]
  	at org.springframework.context.annotation.AnnotationConfigUtils.attributesForRepeatable(AnnotationConfigUtils.java:301) ~[na:na]
  	at org.springframework.context.annotation.AnnotationConfigUtils.attributesForRepeatable(AnnotationConfigUtils.java:291) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.doProcessConfigurationClass(ConfigurationClassParser.java:289) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.processConfigurationClass(ConfigurationClassParser.java:250) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.processMemberClasses(ConfigurationClassParser.java:372) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.doProcessConfigurationClass(ConfigurationClassParser.java:272) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.processConfigurationClass(ConfigurationClassParser.java:250) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassParser.processImports(ConfigurationClassParser.java:600) ~[na:na]
  	... 20 common frames omitted
  Caused by: java.lang.ClassNotFoundException: java.util.function.Function
  	at com.oracle.svm.core.hub.ClassForNameSupport.forName(ClassForNameSupport.java:60) ~[na:na]
  	at java.lang.Class.forName(DynamicHub.java:1292) ~[na:na]
  	at org.springframework.util.ClassUtils.forName(ClassUtils.java:284) ~[na:na]
  	at org.springframework.util.ClassUtils.resolveClassName(ClassUtils.java:324) ~[na:na]
  	... 35 common frames omitted
  ```