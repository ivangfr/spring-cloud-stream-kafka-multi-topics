# `springboot-cloudkarafka`

The goal of this project is to implement a [`Spring Boot`](https://spring.io/projects/spring-boot) application (called
`producer`) that _produces_ messages to some [`Kafka`](https://kafka.apache.org/) topic and another `Spring Boot`
application (called `consumer`) that _consumes_ those messages. I have implemented similar projects before like,
for example [`springboot-spring-kafka`](https://github.com/ivangfr/springboot-spring-kafka). However, this one is
different because instead of running `Kafka` locally using [`Docker`](https://www.docker.com/) containers, it uses
a cloud-based messaging service called [`CloudKarafka`](https://www.cloudkarafka.com/).

## Examples

### [spring-kafka](https://github.com/ivangfr/springboot-cloudkarafka/tree/master/spring-kafka)

In this example, we use [`Spring Kafka`](https://docs.spring.io/spring-kafka/reference/html/) library to implement the
configuration between `Spring Boot` applications and `CloudKarafka`.

### spring-cloud-stream (TODO)

Here, we use [`Spring Cloud Stream`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/)
library to implement the configuration between `Spring Boot` applications and `CloudKarafka`.

## CloudKarafka

First of all, access https://www.cloudkarafka.com/ and create an account. There is the `Developer Duck` plan that is
totally free.

Once you are logged in `CloudKarafka`, click on `Details` menu. You should see something like the picture below. In
this page you have all the credentials and the URLs of the Kafka brokers.

![cloudkarafka-details](images/cloudkarafka-details.png)

In order to get information about the topic you will use, click on the `Topics` menu.

![cloudkarafka-topics](images/cloudkarafka-topics.png)

You can use the default topic or create a new one. In my case, I created a new one with suffix `news.json`.