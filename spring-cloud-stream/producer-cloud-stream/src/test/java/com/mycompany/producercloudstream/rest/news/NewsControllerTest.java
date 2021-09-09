package com.mycompany.producercloudstream.rest.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.producercloudstream.kafka.news.News;
import com.mycompany.producercloudstream.kafka.news.NewsEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The tests in this class are identical to the ones in NewsControllerTest of producer-kafka application
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(NewsController.class)
class NewsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NewsEventProducer newsEventProducer;

    @Test
    void testPublish() {
        CreateNewsRequest request = new CreateNewsRequest("source", "title");

        webTestClient.post()
                .uri("/api/news")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(News.class)
                .value(news -> {
                    assertThat(news.getId()).isNotNull();
                    assertThat(news.getSource()).isEqualTo(request.getSource());
                    assertThat(news.getTitle()).isEqualTo(request.getTitle());
                });
    }
}
