package com.mycompany.producerkafka.rest;

import com.mycompany.producerkafka.kafka.MessageProducer;
import com.mycompany.producerkafka.kafka.event.Alert;
import com.mycompany.producerkafka.kafka.event.News;
import com.mycompany.producerkafka.rest.dto.CreateAlertRequest;
import com.mycompany.producerkafka.rest.dto.CreateNewsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class NewsController {

    private final MessageProducer messageProducer;

    @PostMapping("/news")
    public Mono<String> publishNews(@Valid @RequestBody CreateNewsRequest createNewsRequest) {
        String id = UUID.randomUUID().toString();
        messageProducer.send(News.of(id, createNewsRequest.getSource(), createNewsRequest.getTitle()));
        return Mono.just(id);
    }

    @PostMapping("/alert")
    public Mono<String> publishAlert(@Valid @RequestBody CreateAlertRequest createAlertRequest) {
        String id = UUID.randomUUID().toString();
        messageProducer.send(Alert.of(id, createAlertRequest.getLevel(), createAlertRequest.getMessage()));
        return Mono.just(id);
    }
}
