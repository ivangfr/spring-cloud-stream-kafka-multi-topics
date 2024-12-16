package com.ivanfranchin.producerkafka.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AlertEventEmitter {

    private static final Logger log = LoggerFactory.getLogger(AlertEventEmitter.class);

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AlertEventEmitter(KafkaProperties kafkaProperties, KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaProperties = kafkaProperties;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Alert alert) {
        String kafkaTopic = kafkaProperties.getProducer().getProperties().get("alert-topic");
        log.info("Sending Alert '{}' to topic '{}'", alert, kafkaTopic);
        kafkaTemplate.send(kafkaTopic, alert.id(), alert);
    }
}
