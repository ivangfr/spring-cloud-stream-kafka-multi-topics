package com.mycompany.producercloudstream.kafka.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.producercloudstream.ProducerCloudStreamApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class NewsEventProducerTest {

    @Test
    void testSend() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ProducerCloudStreamApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            NewsEventProducer newsEventProducer = context.getBean(NewsEventProducer.class);
            News news = new News("id", "source", "title");
            newsEventProducer.send(news);

            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> outputMessage = outputDestination.receive(0, "news.json");
            MessageHeaders headers = outputMessage.getHeaders();
            News payload = deserialize(objectMapper, outputMessage.getPayload(), News.class);

            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(payload).isNotNull();
            assertThat(payload.getId()).isEqualTo(news.getId());
            assertThat(payload.getSource()).isEqualTo(news.getSource());
            assertThat(payload.getTitle()).isEqualTo(news.getTitle());
        }
    }

    private <T> T deserialize(ObjectMapper objectMapper, byte[] bytes, Class<T> clazz) {
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            return null;
        }
    }
}
