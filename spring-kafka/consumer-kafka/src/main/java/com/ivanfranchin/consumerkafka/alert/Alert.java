package com.ivanfranchin.consumerkafka.alert;

public record Alert(String id, Integer level, String message) {
}
