package com.mycompany.producerkafka.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.producerkafka.kafka.MessageProducer;
import com.mycompany.producerkafka.rest.dto.CreateAlertRequest;
import com.mycompany.producerkafka.rest.dto.CreateNewsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The test cases in this file is identical to the ones present in NewsControllerTest of producer-cloud-stream application
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(NewsController.class)
class NewsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageProducer messageProducer;

    @Test
    void testPublishNews() {
        CreateNewsRequest request = new CreateNewsRequest("source", "title");

        webTestClient.post()
                .uri(BASE_URL + "/news")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(HashMap.class)
                .value(map -> {
                    assertThat(map.get("id")).isNotNull();
                    assertThat(map.get("source")).isEqualTo(request.getSource());
                    assertThat(map.get("title")).isEqualTo(request.getTitle());
                });
    }

    @Test
    void testPublishAlert() {
        CreateAlertRequest request = new CreateAlertRequest(1, "message");

        webTestClient.post()
                .uri(BASE_URL + "/alerts")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateAlertRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(HashMap.class)
                .value(map -> {
                    assertThat(map.get("id")).isNotNull();
                    assertThat(map.get("level")).isEqualTo(request.getLevel());
                    assertThat(map.get("message")).isEqualTo(request.getMessage());
                });
    }

    private static final String BASE_URL = "/api";
}