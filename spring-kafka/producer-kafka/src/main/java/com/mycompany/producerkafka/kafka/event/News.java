package com.mycompany.producerkafka.kafka.event;

import lombok.Value;

@Value
public class News {

    String id;
    String source;
    String title;
}
