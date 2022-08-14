package com.ivanfranchin.consumercloudstream.kafka.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.function.Consumer;

@Configuration
public class NewsEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(NewsEventConsumer.class);

    @Bean
    public Consumer<Message<News>> news() {
        return message -> {
            News news = message.getPayload();
            MessageHeaders messageHeaders = message.getHeaders();
            log.info("Received message\n---\nTOPIC: {}; PARTITION: {}; OFFSET: {};\nPAYLOAD: {}\n---",
                    messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class),
                    messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                    messageHeaders.get(KafkaHeaders.OFFSET, Long.class),
                    news);
        };
    }
}
