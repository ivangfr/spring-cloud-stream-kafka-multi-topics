package com.mycompany.consumerkafka.kafka.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    private String id;
    private Integer level;
    private String message;
}
