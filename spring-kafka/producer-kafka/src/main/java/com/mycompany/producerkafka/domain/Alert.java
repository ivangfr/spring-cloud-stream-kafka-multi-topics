package com.mycompany.producerkafka.domain;

import lombok.Value;

@Value
public class Alert {

    String id;
    Integer level;
    String message;

}
