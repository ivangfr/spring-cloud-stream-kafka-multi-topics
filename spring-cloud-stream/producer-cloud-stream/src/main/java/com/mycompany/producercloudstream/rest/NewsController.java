package com.mycompany.producercloudstream.rest;

import com.mycompany.producercloudstream.domain.News;
import com.mycompany.producercloudstream.kafka.NewsProducer;
import com.mycompany.producercloudstream.rest.dto.CreateNewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsProducer newsProducer;

    @PostMapping
    public String publishNews(@Valid @RequestBody CreateNewsDto createNewsDto) {
        String id = UUID.randomUUID().toString();
        newsProducer.send(new News(id, createNewsDto.getSource(), createNewsDto.getTitle()));
        return id;
    }

}
