package com.mycompany.consumercloudstream.domain;

import lombok.Data;

@Data
public class Alert {

    private String id;
    private Integer level;
    private String message;

}
