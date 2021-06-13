package com.mycompany.consumercloudstream;

import com.mycompany.consumercloudstream.domain.Alert;
import com.mycompany.consumercloudstream.domain.News;
import org.apache.kafka.common.security.authenticator.DefaultLogin;
import org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler;
import org.apache.kafka.common.security.scram.ScramLoginModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeHint;

@TypeHint(
        types = {
                News.class, Alert.class, // 1
                SaslClientCallbackHandler.class, // 2
                DefaultLogin.class, // 3
                ScramLoginModule.class, // 5
        },
        typeNames = {
                "org.apache.kafka.common.security.authenticator.AbstractLogin$DefaultLoginCallbackHandler", // 4
                "org.apache.kafka.common.security.scram.internals.ScramSaslClient$ScramSaslClientFactory" // 8
        }
)
@SpringBootApplication
public class ConsumerCloudStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerCloudStreamApplication.class, args);
    }

}
