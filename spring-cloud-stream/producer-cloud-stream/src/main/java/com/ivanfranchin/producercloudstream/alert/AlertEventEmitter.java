package com.ivanfranchin.producercloudstream.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class AlertEventEmitter {

    private static final Logger log = LoggerFactory.getLogger(AlertEventEmitter.class);

    private final StreamBridge streamBridge;

    public AlertEventEmitter(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Value("${spring.cloud.stream.bindings.alert-out-0.destination}")
    private String kafkaTopic;

    public void send(Alert alert) {
        log.info("Sending Alert '{}' to topic '{}'", alert, kafkaTopic);

        Message<Alert> message = MessageBuilder.withPayload(alert)
                .setHeader("partitionKey", alert.id())
                .build();
        streamBridge.send("alert-out-0", message);
    }
}
