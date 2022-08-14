package com.ivanfranchin.consumerkafka.kafka.alert;

public record Alert(String id, Integer level, String message) {
}
