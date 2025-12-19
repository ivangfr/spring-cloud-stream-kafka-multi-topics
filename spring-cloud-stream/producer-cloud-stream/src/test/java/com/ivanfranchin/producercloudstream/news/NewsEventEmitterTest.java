package com.ivanfranchin.producercloudstream.news;

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
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

class NewsEventEmitterTest {

    @Test
    void testSend() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ProducerCloudStreamApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            NewsEventEmitter newsEventEmitter = context.getBean(NewsEventEmitter.class);
            News news = new News("id", "source", "title");
            newsEventEmitter.send(news);

            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> outputMessage = outputDestination.receive(0, "spring.cloud.stream.news");
            MessageHeaders headers = outputMessage.getHeaders();
            News payload = objectMapper.readValue(outputMessage.getPayload(), News.class);

            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(payload).isNotNull();
            assertThat(payload.id()).isEqualTo(news.id());
            assertThat(payload.source()).isEqualTo(news.source());
            assertThat(payload.title()).isEqualTo(news.title());
        }
    }
}
