package com.ivanfranchin.producerkafka.rest.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanfranchin.producerkafka.kafka.alert.Alert;
import com.ivanfranchin.producerkafka.kafka.alert.AlertEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The tests in this class are identical to the ones in AlertControllerTest of producer-cloud-stream application
 */
@WebFluxTest(AlertController.class)
class AlertControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlertEventProducer alertEventProducer;

    @Test
    void testPublish() {
        CreateAlertRequest request = new CreateAlertRequest(1, "message");

        webTestClient.post()
                .uri("/api/alerts")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateAlertRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Alert.class)
                .value(alert -> {
                    assertThat(alert.id()).isNotNull();
                    assertThat(alert.level()).isEqualTo(request.level());
                    assertThat(alert.message()).isEqualTo(request.message());
                });
    }
}
