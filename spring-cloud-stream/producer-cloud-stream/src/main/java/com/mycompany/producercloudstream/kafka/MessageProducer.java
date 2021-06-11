package com.mycompany.producercloudstream.kafka;

import com.mycompany.producercloudstream.domain.Alert;
import com.mycompany.producercloudstream.domain.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageProducer {

    @Value("${spring.cloud.stream.bindings.news-out-0.destination}")
    private String newsKafkaTopic;

    @Value("${spring.cloud.stream.bindings.alert-out-0.destination}")
    private String alertKafkaTopic;

    private final StreamBridge streamBridge;

    public void send(News news) {
        log.info("Sending News '{}' to topic '{}'", news, newsKafkaTopic);

        Message<News> message = MessageBuilder.withPayload(news)
                .setHeader("partitionKey", news.getId())
                .build();
        streamBridge.send("news-out-0", message);
    }

    public void send(Alert alert) {
        log.info("Sending News '{}' to topic '{}'", alert, alertKafkaTopic);

        Message<Alert> message = MessageBuilder.withPayload(alert)
                .setHeader("partitionKey", alert.getId())
                .build();
        streamBridge.send("alert-out-0", message);
    }
}
