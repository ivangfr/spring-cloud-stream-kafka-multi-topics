package com.ivanfranchin.producercloudstream.kafka.alert;

public record Alert(String id, Integer level, String message) {
}
