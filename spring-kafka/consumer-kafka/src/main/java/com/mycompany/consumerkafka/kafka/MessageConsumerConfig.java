package com.mycompany.consumerkafka.kafka;

import com.mycompany.consumerkafka.domain.Alert;
import com.mycompany.consumerkafka.domain.News;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@RequiredArgsConstructor
@EnableKafka
@Configuration
public class MessageConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, News> newsKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, News> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(newsConsumerFactory());
        factory.setConcurrency(kafkaProperties.getListener().getConcurrency());
        return factory;
    }

    ConsumerFactory<String, News> newsConsumerFactory() {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(News.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Alert> alertKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Alert> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(alertConsumerFactory());
        factory.setConcurrency(kafkaProperties.getListener().getConcurrency());
        return factory;
    }

    ConsumerFactory<String, Alert> alertConsumerFactory() {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Alert.class));
    }

}
