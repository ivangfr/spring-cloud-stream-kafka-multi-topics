package com.mycompany.producercloudstream.rest;

import com.mycompany.producercloudstream.domain.News;
import com.mycompany.producercloudstream.kafka.NewsProducer;
import com.mycompany.producercloudstream.rest.dto.CreateNewsDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController("/api/news")
public class NewsController {

    private final NewsProducer newsProducer;

    public NewsController(NewsProducer newsProducer) {
        this.newsProducer = newsProducer;
    }

    @PostMapping
    public void publishNews(@Valid @RequestBody CreateNewsDto createNewsDto) {
        newsProducer.send(new News(UUID.randomUUID().toString(), createNewsDto.getSource(), createNewsDto.getTitle()));
    }

}
