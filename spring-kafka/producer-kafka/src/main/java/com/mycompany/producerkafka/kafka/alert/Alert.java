package com.mycompany.producerkafka.kafka.alert;

import lombok.Value;

@Value
public class Alert {

    String id;
    Integer level;
    String message;
}
