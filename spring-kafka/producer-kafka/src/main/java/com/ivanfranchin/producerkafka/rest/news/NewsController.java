package com.ivanfranchin.producerkafka.rest.news;

import com.ivanfranchin.producerkafka.kafka.news.News;
import com.ivanfranchin.producerkafka.kafka.news.NewsEventProducer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsEventProducer newsEventProducer;

    public NewsController(NewsEventProducer newsEventProducer) {
        this.newsEventProducer = newsEventProducer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<News> publish(@Valid @RequestBody CreateNewsRequest createNewsRequest) {
        News news = new News(
                UUID.randomUUID().toString(), createNewsRequest.source(), createNewsRequest.title());
        newsEventProducer.send(news);
        return Mono.just(news);
    }
}
