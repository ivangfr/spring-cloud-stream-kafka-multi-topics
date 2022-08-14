package com.ivanfranchin.producerkafka.kafka.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AlertEventProducer {

    private static final Logger log = LoggerFactory.getLogger(AlertEventProducer.class);

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AlertEventProducer(KafkaProperties kafkaProperties, KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaProperties = kafkaProperties;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Alert alert) {
        String kafkaTopic = kafkaProperties.getProducer().getProperties().get("alert-topic");
        log.info("Sending Alert '{}' to topic '{}'", alert, kafkaTopic);
        kafkaTemplate.send(kafkaTopic, alert.id(), alert);
    }
}
