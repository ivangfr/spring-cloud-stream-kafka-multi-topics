package com.mycompany.consumerkafka.kafka.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {

    private String id;
    private String source;
    private String title;
}
