package com.mycompany.consumerkafka.kafka;

import com.mycompany.consumerkafka.kafka.event.Alert;
import com.mycompany.consumerkafka.kafka.event.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageConsumer {

    @KafkaListener(
            topics = "${spring.kafka.consumer.news-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "newsKafkaListenerContainerFactory")
    public void listenNews(@Payload News news, ConsumerRecordMetadata metadata) {
        log.info("Received message\n---\nTOPIC: {}; PARTITION: {}; OFFSET: {};\nPAYLOAD: {}\n---",
                metadata.topic(), metadata.partition(), metadata.offset(), news);
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.alert-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "alertKafkaListenerContainerFactory")
    public void listenAlert(@Payload Alert alert, ConsumerRecordMetadata metadata) {
        log.info("Received message\n---\nTOPIC: {}; PARTITION: {}; OFFSET: {};\nPAYLOAD: {}\n---",
                metadata.topic(), metadata.partition(), metadata.offset(), alert);
    }
}
