package com.mycompany.consumerkafka.domain;

import lombok.Data;

@Data
public class Alert {

    private String id;
    private Integer level;
    private String message;

}
