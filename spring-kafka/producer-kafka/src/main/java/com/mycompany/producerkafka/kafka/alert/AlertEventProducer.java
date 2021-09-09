package com.mycompany.producerkafka.kafka.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AlertEventProducer {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(Alert alert) {
        String kafkaTopic = kafkaProperties.getProducer().getProperties().get("alert-topic");
        log.info("Sending Alert '{}' to topic '{}'", alert, kafkaTopic);
        kafkaTemplate.send(kafkaTopic, alert.getId(), alert);
    }
}
