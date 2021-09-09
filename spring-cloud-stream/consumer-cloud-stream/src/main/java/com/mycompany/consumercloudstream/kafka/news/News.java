package com.mycompany.consumercloudstream.kafka.news;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class News {

    private String id;
    private String source;
    private String title;
}
