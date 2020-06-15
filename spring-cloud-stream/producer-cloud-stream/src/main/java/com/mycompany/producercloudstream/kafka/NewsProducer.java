package com.mycompany.producercloudstream.kafka;

import com.mycompany.producercloudstream.domain.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableBinding(Source.class)
public class NewsProducer {

    @Value("${spring.cloud.stream.bindings.output.destination}")
    private String kafkaTopic;

    private final Source source;

    public void send(News news) {
        log.info("Sending News '{}' to topic '{}'", news, kafkaTopic);

        Message<News> message = MessageBuilder.withPayload(news)
                .setHeader("partitionKey", news.getId())
                .build();
        source.output().send(message);
    }
}
