package com.mycompany.producerkafka.rest.news;

import com.mycompany.producerkafka.kafka.news.News;
import com.mycompany.producerkafka.kafka.news.NewsEventProducer;
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
@RequestMapping("/api/news")
public class NewsController {

    private final NewsEventProducer newsEventProducer;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<News> publish(@Valid @RequestBody CreateNewsRequest createNewsRequest) {
        News news = new News(
                UUID.randomUUID().toString(), createNewsRequest.getSource(), createNewsRequest.getTitle());
        newsEventProducer.send(news);
        return Mono.just(news);
    }
}
