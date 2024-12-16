package com.ivanfranchin.producercloudstream.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanfranchin.producercloudstream.ProducerCloudStreamApplication;
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

class AlertEventEmitterTest {

    @Test
    void testSend() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ProducerCloudStreamApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            AlertEventEmitter alertEventEmitter = context.getBean(AlertEventEmitter.class);
            Alert alert = new Alert("id", 1, "message");
            alertEventEmitter.send(alert);

            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> outputMessage = outputDestination.receive(0, "spring.cloud.stream.alert");
            MessageHeaders headers = outputMessage.getHeaders();
            Alert payload = deserialize(objectMapper, outputMessage.getPayload(), Alert.class);

            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(payload).isNotNull();
            assertThat(payload.id()).isEqualTo(alert.id());
            assertThat(payload.level()).isEqualTo(alert.level());
            assertThat(payload.message()).isEqualTo(alert.message());
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
