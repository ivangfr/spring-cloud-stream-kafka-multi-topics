package com.mycompany.producerkafka.domain;

import lombok.Value;

@Value
public class News {

    String id;
    String source;
    String title;

}
