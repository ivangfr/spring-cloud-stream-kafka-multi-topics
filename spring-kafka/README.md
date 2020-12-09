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

#### Using CloudKarafka

- **producer-kafka**

  - Open a terminal and navigate to `springboot-cloudkarafka` root folder

  - Export your `CloudKarafka` credentials to those environment variables
    ```
    export CLOUDKARAFKA_USERNAME=...
    export CLOUDKARAFKA_PASSWORD=...
    ```
    
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-kafka/producer-kafka \
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
    ./mvnw spring-boot:run --projects spring-kafka/consumer-kafka \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9081" \
      -Dspring-boot.run.profiles=cloudkarafka
    ```

#### Using Kafka running locally

> **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/springboot-cloudkarafka#running-kafka-locally)  

- **producer-kafka**

  - Open a terminal and navigate to `springboot-cloudkarafka` root folder
  
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-kafka/producer-kafka \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9080"
    ```

- **consumer-kafka**

  - Open another terminal and make sure you are in `springboot-cloudkarafka` root folder
  
  - Run application
    ```
    ./mvnw spring-boot:run --projects spring-kafka/consumer-kafka \
      -Dspring-boot.run.jvmArguments="-Dserver.port=9081"
    ```

## Running applications as Docker containers

### Build applications Docker images

- In a terminal, make sure you are in `springboot-cloudkarafka` root folder

- Build **producer-kafka** Docker image

  - `JVM`
    ```
    ./mvnw clean compile jib:dockerBuild -DskipTests --projects spring-kafka/producer-kafka
    ```

  - `Native`
    ```
    ./mvnw clean spring-boot:build-image -DskipTests --projects spring-kafka/producer-kafka
    ```
  
  | Environment Variable     | Description |
  | -----------------------  | ----------- |
  | `SPRING_PROFILES_ACTIVE` | Specify the type of profile to run the application. To use `CloudKarafka` set `cloudkarafka`. The `default` profile will use local `Kafka` |
  | `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value for `cloudkarafka` profile is `ark-01.srvs.cloudkafka.com:9094, ark-02.srvs.cloudkafka.com:9094, ark-03.srvs.cloudkafka.com:9094`. Using the `default` profile, the default value is `localhost:29092` |
  | `CLOUDKARAFKA_USERNAME`  | Specify your `CloudKarafka` username. Required when using `cloudkarafka` profile |
  | `CLOUDKARAFKA_PASSWORD`  | Specify your `CloudKarafka` password. Required when using `cloudkarafka` profile |

- Build **consumer-kafka** Docker image

  - `JVM`
    ```
    ./mvnw clean compile jib:dockerBuild -DskipTests --projects spring-kafka/consumer-kafka
    ```

  - `Native`
    ```
    ./mvnw clean spring-boot:build-image -DskipTests --projects spring-kafka/consumer-kafka
    ```

  | Environment Variable     | Description |
  | ------------------------ | ----------- |
  | `SPRING_PROFILES_ACTIVE` | Specify the type of profile to run the application. To use `CloudKarafka` set `cloudkarafka`. The `default` will use local `Kafka` |
  | `KAFKA_URL`              | Specify url(s) of the `Kafka` message broker to use. The default value for `karafka` profile is `ark-01.srvs.cloudkafka.com:9094, ark-02.srvs.cloudkafka.com:9094, ark-03.srvs.cloudkafka.com:9094`. Using the `default` profile, the default value is `localhost:29092` |
  | `CLOUDKARAFKA_USERNAME`  | Specify your `CloudKarafka` username. Required when using `cloudkarafka` profile |
  | `CLOUDKARAFKA_PASSWORD`  | Specify your `CloudKarafka` password. Required when using `cloudkarafka` profile |

### Starting applications Docker containers

#### Using CloudKarafka

- In a terminal, export your `CloudKarafka` credentials to those environment variables
  ```
  export CLOUDKARAFKA_USERNAME=...
  export CLOUDKARAFKA_PASSWORD=...
  ```

- Run **producer-kafka**
  ```
  docker run -d --rm --name producer-kafka -p 9080:8080 \
    -e SPRING_PROFILES_ACTIVE=cloudkarafka \
    -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
    -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
    docker.mycompany.com/producer-kafka:1.0.0
  ```

- Run **consumer-kafka**
  ```
  docker run -d --rm --name consumer-kafka -p 9081:8080 \
    -e SPRING_PROFILES_ACTIVE=cloudkarafka \
    -e CLOUDKARAFKA_USERNAME=$CLOUDKARAFKA_USERNAME \
    -e CLOUDKARAFKA_PASSWORD=$CLOUDKARAFKA_PASSWORD \
    docker.mycompany.com/consumer-kafka:1.0.0
  ```

#### Using Kafka running locally

> **Note:** you must have the `docker-compose.yml` services up and running, as explained in the main [README](https://github.com/ivangfr/springboot-cloudkarafka#running-kafka-locally)

- Run **producer-kafka**
  ```
  docker run -d --rm --name producer-kafka \
    -p 9080:8080 -e KAFKA_URL=kafka:9092 \
    --network springboot-cloudkarafka_default \
    docker.mycompany.com/producer-kafka:1.0.0
  ```

- Run **consumer-kafka**
  ```
  docker run -d --rm --name consumer-kafka \
    -p 9081:8080 -e KAFKA_URL=kafka:9092 \
    --network springboot-cloudkarafka_default \
    docker.mycompany.com/consumer-kafka:1.0.0
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

- If they were started as Docker containers, run the command below
  ```
  docker stop producer-kafka consumer-kafka
  ```

## Issues

- After building the `producer-kafka` Docker Native Image, when running it, it's throwing the following exception. It's related to this [issue](https://github.com/spring-projects-experimental/spring-graalvm-native/issues/297)
  ```
    .   ____          _            __ _ _
   /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
  ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
   \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
    '  |____| .__|_| |_|_| |_\__, | / / / /
   =========|_|==============|___/=/_/_/_/
   :: Spring Boot ::
  
  2020-12-09 18:29:51.414  INFO 1 --- [           main] c.m.p.ProducerKafkaApplication           : Starting ProducerKafkaApplication using Java 11.0.9 on 9974f64933f3 with PID 1 (/workspace/com.mycompany.producerkafka.ProducerKafkaApplication started by cnb in /workspace)
  2020-12-09 18:29:51.414  INFO 1 --- [           main] c.m.p.ProducerKafkaApplication           : The following profiles are active: cloudkarafka
  2020-12-09 18:29:51.638  WARN 1 --- [           main] onfigReactiveWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'defaultValidator' defined in class path resource [org/springframework/boot/autoconfigure/validation/ValidationAutoConfiguration.class]: Invocation of init method failed; nested exception is java.lang.ExceptionInInitializerError
  2020-12-09 18:29:51.648  INFO 1 --- [           main] ConditionEvaluationReportLoggingListener :
  
  Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
  2020-12-09 18:29:51.651 ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed
  
  org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'defaultValidator' defined in class path resource [org/springframework/boot/autoconfigure/validation/ValidationAutoConfiguration.class]: Invocation of init method failed; nested exception is java.lang.ExceptionInInitializerError
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1788) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:609) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:531) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[na:na]
  	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[na:na]
  	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:944) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:925) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:588) ~[na:na]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:63) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:767) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:759) ~[na:na]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:426) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:326) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1309) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1298) ~[na:na]
  	at com.mycompany.producerkafka.ProducerKafkaApplication.main(ProducerKafkaApplication.java:10) ~[com.mycompany.producerkafka.ProducerKafkaApplication:na]
  Caused by: java.lang.ExceptionInInitializerError: null
  	at com.oracle.svm.core.classinitialization.ClassInitializationInfo.initialize(ClassInitializationInfo.java:291) ~[na:na]
  	at org.hibernate.validator.internal.engine.ValidatorFactoryConfigurationHelper.determineGetterPropertySelectionStrategy(ValidatorFactoryConfigurationHelper.java:286) ~[na:na]
  	at org.hibernate.validator.internal.engine.ValidatorFactoryImpl.<init>(ValidatorFactoryImpl.java:176) ~[na:na]
  	at org.hibernate.validator.HibernateValidator.buildValidatorFactory(HibernateValidator.java:38) ~[com.mycompany.producerkafka.ProducerKafkaApplication:6.1.6.Final]
  	at org.hibernate.validator.internal.engine.AbstractConfigurationImpl.buildValidatorFactory(AbstractConfigurationImpl.java:448) ~[na:na]
  	at org.springframework.validation.beanvalidation.LocalValidatorFactoryBean.afterPropertiesSet(LocalValidatorFactoryBean.java:310) ~[com.mycompany.producerkafka.ProducerKafkaApplication:5.3.1]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1847) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1784) ~[na:na]
  	... 17 common frames omitted
  Caused by: java.lang.IllegalStateException: java.lang.InstantiationException: Type `org.apache.logging.log4j.message.ReusableMessageFactory` can not be instantiated reflectively as it does not have a no-parameter constructor or the no-parameter constructor has not been added explicitly to the native image.
  	at org.apache.logging.log4j.spi.AbstractLogger.createDefaultMessageFactory(AbstractLogger.java:231) ~[na:na]
  	at org.apache.logging.log4j.spi.AbstractLogger.<init>(AbstractLogger.java:132) ~[na:na]
  	at org.apache.logging.slf4j.SLF4JLogger.<init>(SLF4JLogger.java:44) ~[na:na]
  	at org.apache.logging.slf4j.SLF4JLoggerContext.getLogger(SLF4JLoggerContext.java:39) ~[na:na]
  	at org.apache.logging.log4j.LogManager.getLogger(LogManager.java:669) ~[na:na]
  	at org.jboss.logging.Log4j2Logger.<init>(Log4j2Logger.java:36) ~[na:na]
  	at org.jboss.logging.Log4j2LoggerProvider.getLogger(Log4j2LoggerProvider.java:30) ~[na:na]
  	at org.jboss.logging.Log4j2LoggerProvider.getLogger(Log4j2LoggerProvider.java:26) ~[na:na]
  	at org.jboss.logging.Logger.getLogger(Logger.java:2465) ~[na:na]
  	at org.jboss.logging.Logger.doGetMessageLogger(Logger.java:2573) ~[na:na]
  	at org.jboss.logging.Logger.getMessageLogger(Logger.java:2530) ~[na:na]
  	at org.jboss.logging.Logger.getMessageLogger(Logger.java:2516) ~[na:na]
  	at org.hibernate.validator.internal.util.logging.LoggerFactory.make(LoggerFactory.java:22) ~[na:na]
  	at org.hibernate.validator.internal.properties.DefaultGetterPropertySelectionStrategy.<clinit>(DefaultGetterPropertySelectionStrategy.java:26) ~[na:na]
  	at com.oracle.svm.core.classinitialization.ClassInitializationInfo.invokeClassInitializer(ClassInitializationInfo.java:351) ~[na:na]
  	at com.oracle.svm.core.classinitialization.ClassInitializationInfo.initialize(ClassInitializationInfo.java:271) ~[na:na]
  	... 24 common frames omitted
  Caused by: java.lang.InstantiationException: Type `org.apache.logging.log4j.message.ReusableMessageFactory` can not be instantiated reflectively as it does not have a no-parameter constructor or the no-parameter constructor has not been added explicitly to the native image.
  	at java.lang.Class.newInstance(DynamicHub.java:915) ~[na:na]
  	at org.apache.logging.log4j.spi.AbstractLogger.createDefaultMessageFactory(AbstractLogger.java:228) ~[na:na]
  	... 39 common frames omitted
  ```

- After building the `consumer-kafka` Docker Native Image, when running it, it's throwing the following exception.
  ```
    .   ____          _            __ _ _
   /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
  ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
   \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
    '  |____| .__|_| |_|_| |_\__, | / / / /
   =========|_|==============|___/=/_/_/_/
   :: Spring Boot ::                (v2.4.0)
  
  2020-12-09 19:19:05.654  INFO 1 --- [           main] c.m.c.ConsumerKafkaApplication           : Starting ConsumerKafkaApplication using Java 11.0.9 on c13b226a27ca with PID 1 (/workspace/com.mycompany.consumerkafka.ConsumerKafkaApplication started by cnb in /workspace)
  2020-12-09 19:19:05.654  INFO 1 --- [           main] c.m.c.ConsumerKafkaApplication           : The following profiles are active: cloudkarafka
  2020-12-09 19:19:05.866  WARN 1 --- [           main] onfigReactiveWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'newsConsumer' defined in class path resource [com/mycompany/consumerkafka/kafka/NewsConsumer.class]: Initialization of bean failed; nested exception is java.lang.NullPointerException
  2020-12-09 19:19:05.872  INFO 1 --- [           main] ConditionEvaluationReportLoggingListener :
  
  Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
  2020-12-09 19:19:05.877 ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed
  
  org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'newsConsumer' defined in class path resource [com/mycompany/consumerkafka/kafka/NewsConsumer.class]: Initialization of bean failed; nested exception is java.lang.NullPointerException
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:617) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:531) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[na:na]
  	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[na:na]
  	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:944) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:925) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:588) ~[na:na]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:63) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:767) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:759) ~[na:na]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:426) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:326) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1309) ~[na:na]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1298) ~[na:na]
  	at com.mycompany.consumerkafka.ConsumerKafkaApplication.main(ConsumerKafkaApplication.java:10) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:na]
  Caused by: java.lang.NullPointerException: null
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.resolveExpression(KafkaListenerAnnotationBeanPostProcessor.java:751) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.3]
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.resolveExpressionAsString(KafkaListenerAnnotationBeanPostProcessor.java:705) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.3]
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.getEndpointGroupId(KafkaListenerAnnotationBeanPostProcessor.java:504) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.3]
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.processListener(KafkaListenerAnnotationBeanPostProcessor.java:426) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.3]
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.processKafkaListener(KafkaListenerAnnotationBeanPostProcessor.java:379) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.3]
  	at org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor.postProcessAfterInitialization(KafkaListenerAnnotationBeanPostProcessor.java:307) ~[com.mycompany.consumerkafka.ConsumerKafkaApplication:2.6.3]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsAfterInitialization(AbstractAutowireCapableBeanFactory.java:444) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1792) ~[na:na]
  	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:609) ~[na:na]
  	... 16 common frames omitted
  ```