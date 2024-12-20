package com.ivanfranchin.consumerkafka.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AlertEventListener {

    private static final Logger log = LoggerFactory.getLogger(AlertEventListener.class);

    @KafkaListener(
            topics = "${spring.kafka.consumer.alert-topic}",
            groupId = "${spring.kafka.consumer.alert-group-id}",
            containerFactory = "alertKafkaListenerContainerFactory")
    public void alert(@Payload Alert alert, ConsumerRecordMetadata metadata) {
        log.info("Received message\n---\nTOPIC: {}; PARTITION: {}; OFFSET: {};\nPAYLOAD: {}\n---",
                metadata.topic(), metadata.partition(), metadata.offset(), alert);
    }
}
