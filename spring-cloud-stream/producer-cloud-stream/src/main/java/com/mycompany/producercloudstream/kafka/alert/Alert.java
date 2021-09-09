package com.mycompany.producercloudstream.kafka.alert;

import lombok.Value;

@Value
public class Alert {

    String id;
    Integer level;
    String message;
}
