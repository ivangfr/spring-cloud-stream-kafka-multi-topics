package com.ivanfranchin.consumercloudstream.kafka.alert;

public record Alert(String id, Integer level, String message) {
}
