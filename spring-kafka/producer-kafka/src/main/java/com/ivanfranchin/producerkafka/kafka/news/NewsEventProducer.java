package com.ivanfranchin.producerkafka.kafka.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NewsEventProducer {

    private static final Logger log = LoggerFactory.getLogger(NewsEventProducer.class);

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public NewsEventProducer(KafkaProperties kafkaProperties, KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaProperties = kafkaProperties;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(News news) {
        String kafkaTopic = kafkaProperties.getProducer().getProperties().get("news-topic");
        log.info("Sending News '{}' to topic '{}'", news, kafkaTopic);
        kafkaTemplate.send(kafkaTopic, news.id(), news);
    }
}
