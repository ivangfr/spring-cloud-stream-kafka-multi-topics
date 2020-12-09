package com.mycompany.producercloudstream.kafka;

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
public class NewsProducer {

    @Value("${spring.cloud.stream.bindings.news-out-0.destination}")
    private String kafkaTopic;

    private final StreamBridge streamBridge;

    public void send(News news) {
        log.info("Sending News '{}' to topic '{}'", news, kafkaTopic);

        Message<News> message = MessageBuilder.withPayload(news)
                .setHeader("partitionKey", news.getId())
                .build();
        streamBridge.send("news-out-0", message);
    }
}
