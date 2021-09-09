package com.mycompany.producercloudstream.rest.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.producercloudstream.kafka.alert.Alert;
import com.mycompany.producercloudstream.kafka.alert.AlertEventProducer;
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
 * The tests in this class are identical to the ones in AlertControllerTest of producer-kafka application
 */
@ExtendWith(SpringExtension.class)
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
                    assertThat(alert.getId()).isNotNull();
                    assertThat(alert.getLevel()).isEqualTo(request.getLevel());
                    assertThat(alert.getMessage()).isEqualTo(request.getMessage());
                });
    }
}
