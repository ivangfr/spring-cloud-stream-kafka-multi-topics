package com.ivanfranchin.consumercloudstream.kafka.alert;

import com.ivanfranchin.consumercloudstream.ConsumerCloudStreamApplication;
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
class AlertEventConsumerTest {

    @Test
    void testAlert(CapturedOutput output) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ConsumerCloudStreamApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            Alert alert = new Alert("id", 1, "message");
            Message<Alert> alertMessage = MessageBuilder.withPayload(alert).build();

            InputDestination inputDestination = context.getBean(InputDestination.class);
            inputDestination.send(alertMessage, "spring.cloud.stream.alert");

            assertThat(output).contains("Received message");
            assertThat(output).contains("PAYLOAD: Alert(id=id, level=1, message=message)");
        }
    }
}
