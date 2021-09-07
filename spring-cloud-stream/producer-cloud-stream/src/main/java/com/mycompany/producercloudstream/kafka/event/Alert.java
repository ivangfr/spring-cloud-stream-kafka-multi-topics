package com.mycompany.producercloudstream.kafka.event;

import lombok.Value;

@Value
public class Alert {

    String id;
    Integer level;
    String message;
}
