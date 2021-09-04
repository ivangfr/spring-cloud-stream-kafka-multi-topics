package com.mycompany.producercloudstream.kafka.event;

import lombok.Value;

@Value(staticConstructor = "of")
public class News {

    String id;
    String source;
    String title;
}
