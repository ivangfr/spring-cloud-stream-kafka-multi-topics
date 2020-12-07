package com.mycompany.consumercloudstream.kafka;

import com.mycompany.consumercloudstream.domain.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class NewsConsumer {

    @Bean
    public Consumer<Message<News>> listen() {
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
