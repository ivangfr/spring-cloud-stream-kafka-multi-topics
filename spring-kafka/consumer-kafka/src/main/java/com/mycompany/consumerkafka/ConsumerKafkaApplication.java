package com.mycompany.consumerkafka;

import com.mycompany.consumerkafka.domain.News;
import org.apache.kafka.common.security.authenticator.AbstractLogin;
import org.apache.kafka.common.security.authenticator.DefaultLogin;
import org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler;
import org.apache.kafka.common.security.scram.ScramLoginModule;
import org.apache.kafka.common.security.scram.internals.ScramSaslClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeHint;

@TypeHint(
        types = {
                News.class,
                SaslClientCallbackHandler.class,
                DefaultLogin.class,
                AbstractLogin.class,
                ScramLoginModule.class,
                ScramSaslClient.class
        },
        typeNames = {
                "org.apache.kafka.common.security.authenticator.AbstractLogin$DefaultLoginCallbackHandler",
                "org.apache.kafka.common.security.scram.internals.ScramSaslClient$ScramSaslClientFactory"
        }
)
@SpringBootApplication
public class ConsumerKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerKafkaApplication.class, args);
    }

}
