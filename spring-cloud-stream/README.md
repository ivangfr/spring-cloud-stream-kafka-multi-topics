# springboot-cloudkarafka
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

    - Open a terminal and navigate to `springboot-cloudkarafka` root folder

    - Export your `CloudKarafka` credentials to those environment variables
      ```
      export CLOUDKARAFKA_USERNAME=...
      export CLOUDKARAFKA_PASSWORD=...
      ```
    
    - Run application
      ```
      ./mvnw clean package spring-boot:run --projects spring-cloud-stream/producer-cloud-stream -DskipTests \
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
      ./mvnw clean package spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream -DskipTests \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9083" \
        -Dspring-boot.run.profiles=cloudkarafka
      ```

- #### Using Kafka running locally

  > **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/springboot-cloudkarafka#running-kafka-locally)  

  - **producer-cloud-stream**

    - Open a terminal and navigate to `springboot-cloudkarafka` root folder
  
    - Run application
      ```
      ./mvnw clean package spring-boot:run --projects spring-cloud-stream/producer-cloud-stream -DskipTests \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9082"
      ```

  - **consumer-cloud-stream**

    - Open another terminal and make sure you are in `springboot-cloudkarafka` root folder
  
    - Run application
      ```
      ./mvnw clean package spring-boot:run --projects spring-cloud-stream/consumer-cloud-stream -DskipTests \
        -Dspring-boot.run.jvmArguments="-Dserver.port=9083"
      ```

## Running applications as Docker containers

- ### Build application's Docker image

  - In a terminal, make sure you are in `springboot-cloudkarafka` root folder

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

  - #### Using Kafka running locally

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
  http :9082/api/alert level=4 message="Tsunami is comming"
  ```

  **producer-kafka** logs
  ```
  INFO c.m.producerkafka.kafka.AlertProducer    : Sending Alert 'Alert(id=756a8dc8-21ca-4856-9a4d-a0b34c158b43, level=4, message=Tsunami is comming)' to topic '2gxxxxxx-alert.json'
  ```

  **consumer-kafka** logs
  ```
  INFO c.m.consumerkafka.kafka.NewsConsumer     : Received message
  ---
  TOPIC: 2gxxxxxx-alert.json; PARTITION: 0; OFFSET: 2;
  PAYLOAD: Alert(id=756a8dc8-21ca-4856-9a4d-a0b34c158b43, level=4, message=Tsunami is comming)
  ---
  ```

## Stop applications

- If they were started with `Maven`, go to the terminals where they are running and press `Ctrl+C`

- If they were started as Docker containers, run the script below
  ```
  ./stop-spring-cloud-stream-apps.sh
  ```

## Issues

- After building the `producer-cloud-stream` Docker Native Image, the following exception is thrown at runtime. It's related to this [issue #693](https://github.com/spring-projects-experimental/spring-native/issues/693)
  ```
  ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed
  
  com.oracle.svm.core.jdk.UnsupportedFeatureError: Proxy class defined by interfaces [interface org.springframework.cloud.stream.annotation.EnableBinding, interface org.springframework.core.annotation.SynthesizedAnnotation] not found. Generating proxy classes at runtime is not supported. Proxy classes need to be defined at image build time by specifying the list of interfaces that they implement. To define proxy classes use -H:DynamicProxyConfigurationFiles=<comma-separated-config-files> and -H:DynamicProxyConfigurationResources=<comma-separated-config-resources> options.
  	at com.oracle.svm.core.util.VMError.unsupportedFeature(VMError.java:87) ~[na:na]
  	at com.oracle.svm.reflect.proxy.DynamicProxySupport.getProxyClass(DynamicProxySupport.java:113) ~[na:na]
  	at java.lang.reflect.Proxy.getProxyConstructor(Proxy.java:66) ~[na:na]
  	at java.lang.reflect.Proxy.newProxyInstance(Proxy.java:1006) ~[na:na]
  	at org.springframework.core.annotation.SynthesizedMergedAnnotationInvocationHandler.createProxy(SynthesizedMergedAnnotationInvocationHandler.java:271) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.createSynthesized(TypeMappedAnnotation.java:335) ~[na:na]
  	at org.springframework.core.annotation.AbstractMergedAnnotation.synthesize(AbstractMergedAnnotation.java:210) ~[na:na]
  	at org.springframework.core.annotation.AnnotationUtils.synthesizeAnnotation(AnnotationUtils.java:1249) ~[na:na]
  	at org.springframework.cloud.stream.config.BindingBeansRegistrar.collectClasses(BindingBeansRegistrar.java:56) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.1.2]
  	at org.springframework.cloud.stream.config.BindingBeansRegistrar.registerBeanDefinitions(BindingBeansRegistrar.java:43) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:3.1.2]
  	at org.springframework.context.annotation.ImportBeanDefinitionRegistrar.registerBeanDefinitions(ImportBeanDefinitionRegistrar.java:86) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:5.3.6]
  	at org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.lambda$loadBeanDefinitionsFromRegistrars$1(ConfigurationClassBeanDefinitionReader.java:396) ~[na:na]
  	at java.util.LinkedHashMap.forEach(LinkedHashMap.java:684) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.loadBeanDefinitionsFromRegistrars(ConfigurationClassBeanDefinitionReader.java:395) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.loadBeanDefinitionsForConfigurationClass(ConfigurationClassBeanDefinitionReader.java:157) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.loadBeanDefinitions(ConfigurationClassBeanDefinitionReader.java:129) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassPostProcessor.processConfigBeanDefinitions(ConfigurationClassPostProcessor.java:343) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:5.3.6]
  	at org.springframework.context.annotation.ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry(ConfigurationClassPostProcessor.java:247) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:5.3.6]
  	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanDefinitionRegistryPostProcessors(PostProcessorRegistrationDelegate.java:311) ~[na:na]
  	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:112) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(AbstractApplicationContext.java:746) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:564) ~[na:na]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:63) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:782) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:774) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:339) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1340) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1329) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  	at com.mycompany.producercloudstream.ProducerCloudStreamApplication.main(ProducerCloudStreamApplication.java:10) ~[com.mycompany.producercloudstream.ProducerCloudStreamApplication:na]
  ```

- After building the `consumer-cloud-stream` Docker Native Image, the following exception is thrown at runtime. It's related to this [issue #693](https://github.com/spring-projects-experimental/spring-native/issues/693)
  ```
  ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed
  
  com.oracle.svm.core.jdk.UnsupportedFeatureError: Proxy class defined by interfaces [interface org.springframework.cloud.stream.annotation.EnableBinding, interface org.springframework.core.annotation.SynthesizedAnnotation] not found. Generating proxy classes at runtime is not supported. Proxy classes need to be defined at image build time by specifying the list of interfaces that they implement. To define proxy classes use -H:DynamicProxyConfigurationFiles=<comma-separated-config-files> and -H:DynamicProxyConfigurationResources=<comma-separated-config-resources> options.
  	at com.oracle.svm.core.util.VMError.unsupportedFeature(VMError.java:87) ~[na:na]
  	at com.oracle.svm.reflect.proxy.DynamicProxySupport.getProxyClass(DynamicProxySupport.java:113) ~[na:na]
  	at java.lang.reflect.Proxy.getProxyConstructor(Proxy.java:66) ~[na:na]
  	at java.lang.reflect.Proxy.newProxyInstance(Proxy.java:1006) ~[na:na]
  	at org.springframework.core.annotation.SynthesizedMergedAnnotationInvocationHandler.createProxy(SynthesizedMergedAnnotationInvocationHandler.java:271) ~[na:na]
  	at org.springframework.core.annotation.TypeMappedAnnotation.createSynthesized(TypeMappedAnnotation.java:335) ~[na:na]
  	at org.springframework.core.annotation.AbstractMergedAnnotation.synthesize(AbstractMergedAnnotation.java:210) ~[na:na]
  	at org.springframework.core.annotation.AnnotationUtils.synthesizeAnnotation(AnnotationUtils.java:1249) ~[na:na]
  	at org.springframework.cloud.stream.config.BindingBeansRegistrar.collectClasses(BindingBeansRegistrar.java:56) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.2]
  	at org.springframework.cloud.stream.config.BindingBeansRegistrar.registerBeanDefinitions(BindingBeansRegistrar.java:43) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:3.1.2]
  	at org.springframework.context.annotation.ImportBeanDefinitionRegistrar.registerBeanDefinitions(ImportBeanDefinitionRegistrar.java:86) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.6]
  	at org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.lambda$loadBeanDefinitionsFromRegistrars$1(ConfigurationClassBeanDefinitionReader.java:396) ~[na:na]
  	at java.util.LinkedHashMap.forEach(LinkedHashMap.java:684) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.loadBeanDefinitionsFromRegistrars(ConfigurationClassBeanDefinitionReader.java:395) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.loadBeanDefinitionsForConfigurationClass(ConfigurationClassBeanDefinitionReader.java:157) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.loadBeanDefinitions(ConfigurationClassBeanDefinitionReader.java:129) ~[na:na]
  	at org.springframework.context.annotation.ConfigurationClassPostProcessor.processConfigBeanDefinitions(ConfigurationClassPostProcessor.java:343) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.6]
  	at org.springframework.context.annotation.ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry(ConfigurationClassPostProcessor.java:247) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:5.3.6]
  	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanDefinitionRegistryPostProcessors(PostProcessorRegistrationDelegate.java:311) ~[na:na]
  	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:112) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(AbstractApplicationContext.java:746) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:564) ~[na:na]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:63) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:782) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:774) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:339) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1340) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1329) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:na]
  	at com.mycompany.consumercloudstream.ConsumerCloudStreamApplication.main(ConsumerCloudStreamApplication.java:10) ~[com.mycompany.consumercloudstream.ConsumerCloudStreamApplication:na]
  ```