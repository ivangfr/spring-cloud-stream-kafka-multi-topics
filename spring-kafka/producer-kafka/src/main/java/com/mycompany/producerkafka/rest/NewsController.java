package com.mycompany.producerkafka.rest;

import com.mycompany.producerkafka.kafka.MessageProducer;
import com.mycompany.producerkafka.kafka.event.Alert;
import com.mycompany.producerkafka.kafka.event.News;
import com.mycompany.producerkafka.rest.dto.CreateAlertRequest;
import com.mycompany.producerkafka.rest.dto.CreateNewsRequest;
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
@RequestMapping("/api")
public class NewsController {

    private final MessageProducer messageProducer;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/news")
    public Mono<News> publishNews(@Valid @RequestBody CreateNewsRequest createNewsRequest) {
        News news = News.of(getId(), createNewsRequest.getSource(), createNewsRequest.getTitle());
        messageProducer.send(news);
        return Mono.just(news);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/alerts")
    public Mono<Alert> publishAlert(@Valid @RequestBody CreateAlertRequest createAlertRequest) {
        Alert alert = Alert.of(getId(), createAlertRequest.getLevel(), createAlertRequest.getMessage());
        messageProducer.send(alert);
        return Mono.just(alert);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
