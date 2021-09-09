package com.mycompany.producercloudstream.rest.alert;

import com.mycompany.producercloudstream.kafka.alert.Alert;
import com.mycompany.producercloudstream.kafka.alert.AlertEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertEventProducer alertEventProducer;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Alert> publish(@Valid @RequestBody CreateAlertRequest createAlertRequest) {
        Alert alert = new Alert(
                UUID.randomUUID().toString(), createAlertRequest.getLevel(), createAlertRequest.getMessage());
        alertEventProducer.send(alert);
        return Mono.just(alert);
    }
}
