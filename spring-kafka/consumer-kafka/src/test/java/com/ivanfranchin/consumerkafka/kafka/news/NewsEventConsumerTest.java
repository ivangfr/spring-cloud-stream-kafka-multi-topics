package com.ivanfranchin.consumerkafka.kafka.news;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class NewsEventConsumerTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ObjectMapper objectMapper;

    private Producer<String, String> producer;

    @BeforeAll
    void setUp() {
        Map<String, Object> configs = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer())
                .createProducer();
    }

    @AfterAll
    void shutdown() {
        producer.close();
    }

    @Test
    void testNews(CapturedOutput output) throws JsonProcessingException {
        String id = "id";
        String payload = objectMapper.writeValueAsString(new News(id, "source", "title"));
        producer.send(new ProducerRecord<>("spring.kafka.news", 0, id, payload));
        producer.flush();

        await().atMost(AT_MOST_DURATION).pollInterval(POLL_INTERVAL_DURATION).untilAsserted(() -> {
            assertThat(output).contains("Received message");
            assertThat(output).contains("TOPIC: spring.kafka.news; PARTITION: 0; OFFSET: 0;");
            assertThat(output).contains("PAYLOAD: News[id=id, source=source, title=title]");
        });
    }

    private static final Duration AT_MOST_DURATION = Duration.ofSeconds(5);
    private static final Duration POLL_INTERVAL_DURATION = Duration.ofSeconds(1);
}
