package com.mycompany.consumercloudstream.kafka.news;

import com.mycompany.consumercloudstream.ConsumerCloudStreamApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class NewsEventConsumerTest {

    @Test
    void testNews(CapturedOutput output) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ConsumerCloudStreamApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            News news = new News("id", "source", "title");
            Message<News> newsMessage = MessageBuilder.withPayload(news).build();

            InputDestination inputDestination = context.getBean(InputDestination.class);
            inputDestination.send(newsMessage, "spring.cloud.stream.news");

            assertThat(output).contains("Received message");
            assertThat(output).contains("PAYLOAD: News(id=id, source=source, title=title)");
        }
    }
}
