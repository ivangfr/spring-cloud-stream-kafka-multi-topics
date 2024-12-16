package com.ivanfranchin.producerkafka.alert;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertEventEmitter alertEventEmitter;

    public AlertController(AlertEventEmitter alertEventEmitter) {
        this.alertEventEmitter = alertEventEmitter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Alert> publish(@Valid @RequestBody CreateAlertRequest createAlertRequest) {
        Alert alert = Alert.from(createAlertRequest);
        alertEventEmitter.send(alert);
        return Mono.just(alert);
    }
}
