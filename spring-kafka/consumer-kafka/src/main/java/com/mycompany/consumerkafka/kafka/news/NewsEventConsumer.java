package com.mycompany.consumerkafka.kafka.news;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NewsEventConsumer {

    @KafkaListener(
            topics = "${spring.kafka.consumer.news-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "newsKafkaListenerContainerFactory")
    public void news(@Payload News news, ConsumerRecordMetadata metadata) {
        log.info("Received message\n---\nTOPIC: {}; PARTITION: {}; OFFSET: {};\nPAYLOAD: {}\n---",
                metadata.topic(), metadata.partition(), metadata.offset(), news);
    }
}
