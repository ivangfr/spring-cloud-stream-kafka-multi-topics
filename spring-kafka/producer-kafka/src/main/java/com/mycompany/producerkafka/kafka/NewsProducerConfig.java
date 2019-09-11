package com.mycompany.producerkafka.kafka;

import com.mycompany.producerkafka.domain.News;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class NewsProducerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.producer.topic}")
    private String topic;

    @Value("${kafka.producer.num-partitions}")
    private Integer numPartitions;

    @Value("${kafka.configuration.security.protocol}")
    private String securityProtocol;

    @Value("${kafka.configuration.sasl.mechanism}")
    private String saslMechanism;

    @Value("${kafka.configuration.sasl.jaas.config}")
    private String saslJaasConfig;

    @Bean
    ProducerFactory<String, News> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put("security.protocol", securityProtocol);
        props.put("sasl.mechanism", saslMechanism);
        props.put("sasl.jaas.config", saslJaasConfig);
        return props;
    }

    @Bean
    public KafkaTemplate<String, News> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
