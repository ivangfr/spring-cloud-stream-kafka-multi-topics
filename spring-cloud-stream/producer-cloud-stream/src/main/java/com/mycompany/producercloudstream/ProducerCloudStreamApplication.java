package com.mycompany.producercloudstream;

import com.mycompany.producercloudstream.kafka.event.Alert;
import com.mycompany.producercloudstream.kafka.event.News;
import org.apache.kafka.common.security.authenticator.DefaultLogin;
import org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler;
import org.apache.kafka.common.security.scram.ScramLoginModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.NativeHint;
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
                "org.apache.kafka.common.security.scram.internals.ScramSaslClient$ScramSaslClientFactory" // 7
        }
)
@NativeHint(options = "-H:IncludeResourceBundles=sun.security.util.Resources") // 6
@SpringBootApplication
public class ProducerCloudStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerCloudStreamApplication.class, args);
    }

}
