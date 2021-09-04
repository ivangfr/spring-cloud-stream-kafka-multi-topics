package com.mycompany.producerkafka.kafka.event;

import lombok.Value;

@Value(staticConstructor = "of")
public class Alert {

    String id;
    Integer level;
    String message;
}
