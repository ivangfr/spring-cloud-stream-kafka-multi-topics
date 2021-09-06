package com.mycompany.producercloudstream.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.producercloudstream.ProducerCloudStreamApplication;
import com.mycompany.producercloudstream.kafka.event.Alert;
import com.mycompany.producercloudstream.kafka.event.News;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class MessageProducerTest {

    @Test
    void testSendNews() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ProducerCloudStreamApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            MessageProducer messageProducer = context.getBean(MessageProducer.class);
            News news = News.of("id", "source", "title");
            messageProducer.send(news);

            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> outputMessage = outputDestination.receive(0, "news.json");
            MessageHeaders headers = outputMessage.getHeaders();
            News payload = deserializeFromString(
                    objectMapper, new String(outputMessage.getPayload(), StandardCharsets.UTF_8), News.class);

            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(payload).isNotNull();
            assertThat(payload.getId()).isEqualTo(news.getId());
            assertThat(payload.getSource()).isEqualTo(news.getSource());
            assertThat(payload.getTitle()).isEqualTo(news.getTitle());
        }
    }

    @Test
    void testSendAlert() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ProducerCloudStreamApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            MessageProducer messageProducer = context.getBean(MessageProducer.class);
            Alert alert = Alert.of("id", 1, "message");
            messageProducer.send(alert);

            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> outputMessage = outputDestination.receive(0, "alert.json");
            MessageHeaders headers = outputMessage.getHeaders();
            Alert payload = deserializeFromString(
                    objectMapper, new String(outputMessage.getPayload(), StandardCharsets.UTF_8), Alert.class);

            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(payload).isNotNull();
            assertThat(payload.getId()).isEqualTo(alert.getId());
            assertThat(payload.getLevel()).isEqualTo(alert.getLevel());
            assertThat(payload.getMessage()).isEqualTo(alert.getMessage());
        }
    }

    private <T> T deserializeFromString(ObjectMapper objectMapper, String s, Class<T> clazz) {
        try {
            return objectMapper.readValue(s, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}