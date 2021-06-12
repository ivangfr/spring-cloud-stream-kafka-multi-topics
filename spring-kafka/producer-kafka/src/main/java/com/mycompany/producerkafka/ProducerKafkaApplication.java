package com.mycompany.producerkafka;

import com.mycompany.producerkafka.domain.Alert;
import com.mycompany.producerkafka.domain.News;
import org.apache.kafka.common.security.authenticator.DefaultLogin;
import org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler;
import org.apache.kafka.common.security.scram.ScramLoginModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeHint;

@TypeHint(
        types = {
                News.class, Alert.class, // 7
                SaslClientCallbackHandler.class, // 1
                DefaultLogin.class, // 2
                ScramLoginModule.class, // 4
        },
        typeNames = {
                "org.apache.kafka.common.security.authenticator.AbstractLogin$DefaultLoginCallbackHandler", // 3
                "org.apache.kafka.common.security.scram.internals.ScramSaslClient$ScramSaslClientFactory" // 6
        }
)
@SpringBootApplication
public class ProducerKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerKafkaApplication.class, args);
    }

}
