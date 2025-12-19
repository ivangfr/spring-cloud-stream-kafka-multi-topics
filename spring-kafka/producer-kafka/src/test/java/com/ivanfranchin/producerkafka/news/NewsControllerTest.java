package com.ivanfranchin.producerkafka.news;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The tests in this class are identical to the ones in NewsControllerTest of producer-cloud-stream application
 */
@WebFluxTest(NewsController.class)
class NewsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NewsEventEmitter newsEventEmitter;

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
                    assertThat(news.id()).isNotNull();
                    assertThat(news.source()).isEqualTo(request.source());
                    assertThat(news.title()).isEqualTo(request.title());
                });
    }
}
