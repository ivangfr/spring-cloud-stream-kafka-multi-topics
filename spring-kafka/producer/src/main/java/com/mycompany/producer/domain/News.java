package com.mycompany.producer.domain;

import lombok.Value;

@Value
public class News {

    private String id;
    private String source;
    private String title;

}
