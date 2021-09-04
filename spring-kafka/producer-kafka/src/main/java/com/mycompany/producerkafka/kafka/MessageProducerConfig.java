package com.mycompany.producerkafka.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class MessageProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    // As the application will create a topic in Kafka when the profile is not 'cloudkarafka', it is better to update
    // the KafkaAdmin's BOOTSTRAP_SERVERS_CONFIG property with the bootstrap servers' url configured, otherwise it will
    // get the default 'localhost:9082'
    @Bean
    @Profile("!cloudkarafka")
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    @Profile("!cloudkarafka")
    public NewTopic newsTopic() {
        Map<String, String> producerProperties = kafkaProperties.getProducer().getProperties();
        return new NewTopic(producerProperties.get("news-topic"), Integer.parseInt(producerProperties.get("num-partitions")), (short) 1);
    }

    @Bean
    @Profile("!cloudkarafka")
    public NewTopic alertTopic() {
        Map<String, String> producerProperties = kafkaProperties.getProducer().getProperties();
        return new NewTopic(producerProperties.get("alert-topic"), Integer.parseInt(producerProperties.get("num-partitions")), (short) 1);
    }
}
