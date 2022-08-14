package com.ivanfranchin.consumerkafka.kafka.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class NewsEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(NewsEventConsumer.class);

    @KafkaListener(
            topics = "${spring.kafka.consumer.news-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "newsKafkaListenerContainerFactory")
    public void news(@Payload News news, ConsumerRecordMetadata metadata) {
        log.info("Received message\n---\nTOPIC: {}; PARTITION: {}; OFFSET: {};\nPAYLOAD: {}\n---",
                metadata.topic(), metadata.partition(), metadata.offset(), news);
    }
}
