package com.ivanfranchin.producerkafka.kafka.alert;

public record Alert(String id, Integer level, String message) {
}
