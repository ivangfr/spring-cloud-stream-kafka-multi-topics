package com.mycompany.producerkafka.kafka;

import com.mycompany.producerkafka.kafka.event.Alert;
import com.mycompany.producerkafka.kafka.event.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageProducer {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(News news) {
        String kafkaTopic = kafkaProperties.getProducer().getProperties().get("news-topic");
        log.info("Sending News '{}' to topic '{}'", news, kafkaTopic);
        kafkaTemplate.send(kafkaTopic, news.getId(), news);
    }

    public void send(Alert alert) {
        String kafkaTopic = kafkaProperties.getProducer().getProperties().get("alert-topic");
        log.info("Sending Alert '{}' to topic '{}'", alert, kafkaTopic);
        kafkaTemplate.send(kafkaTopic, alert.getId(), alert);
    }
}
