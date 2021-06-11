package com.mycompany.producercloudstream.domain;

import lombok.Value;

@Value
public class Alert {

    String id;
    Integer level;
    String message;

}
