package com.ivanfranchin.producercloudstream.alert;

import java.util.UUID;

public record Alert(String id, Integer level, String message) {

    public static Alert from(CreateAlertRequest createAlertRequest) {
        return new Alert(UUID.randomUUID().toString(), createAlertRequest.level(), createAlertRequest.message());
    }
}
