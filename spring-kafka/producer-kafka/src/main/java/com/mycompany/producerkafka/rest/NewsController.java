package com.mycompany.producerkafka.rest;

import com.mycompany.producerkafka.domain.News;
import com.mycompany.producerkafka.kafka.NewsProducer;
import com.mycompany.producerkafka.rest.dto.CreateNewsDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsProducer newsProducer;

    public NewsController(NewsProducer newsProducer) {
        this.newsProducer = newsProducer;
    }

    @PostMapping
    public String publishNews(@Valid @RequestBody CreateNewsDto createNewsDto) {
        String id = UUID.randomUUID().toString();
        newsProducer.send(new News(id, createNewsDto.getSource(), createNewsDto.getTitle()));
        return id;
    }

}
