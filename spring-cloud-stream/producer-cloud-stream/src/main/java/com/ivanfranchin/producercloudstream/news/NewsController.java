package com.ivanfranchin.producercloudstream.news;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsEventEmitter newsEventEmitter;

    public NewsController(NewsEventEmitter newsEventEmitter) {
        this.newsEventEmitter = newsEventEmitter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<News> publish(@Valid @RequestBody CreateNewsRequest createNewsRequest) {
        News news = News.from(createNewsRequest);
        newsEventEmitter.send(news);
        return Mono.just(news);
    }
}
