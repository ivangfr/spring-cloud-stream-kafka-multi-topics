package com.ivanfranchin.producercloudstream.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class NewsEventEmitter {

    private static final Logger log = LoggerFactory.getLogger(NewsEventEmitter.class);

    private final StreamBridge streamBridge;

    public NewsEventEmitter(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Value("${spring.cloud.stream.bindings.news-out-0.destination}")
    private String kafkaTopic;

    public void send(News news) {
        log.info("Sending News '{}' to topic '{}'", news, kafkaTopic);

        Message<News> message = MessageBuilder.withPayload(news)
                .setHeader("partitionKey", news.id())
                .build();
        streamBridge.send("news-out-0", message);
    }
}
