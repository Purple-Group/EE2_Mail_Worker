package com.ee2.mail_worker.config;

import com.purplegroup.avro.ClientTradeEventDTO;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value(value = "${spring.kafka.properties.bootstrap.servers}")
    private String bootstrapAddress;
    @Value(value = "${spring.kafka.consumer.group-id}")
    private String userGroupId;
    @Value(value = "${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;
    @Value(value = "${spring.kafka.properties.sasl.mechanism}")
    private String sasl;
    @Value(value = "${spring.kafka.properties.sasl.jaas.config:}")
    private String sasljass;
    @Value(value = "${spring.kafka.properties.security.protocol:}")
    private String securityProc;
    @Value(value = "${spring.kafka.properties.basic.auth.credentials.source:}")
    private String secSource;
    @Value(value = "${spring.kafka.properties.schema.registry.basic.auth.user.info:}")
    private String regAuth;
    @Value(value = "${spring.kafka.properties.cloud}")
    private boolean cloudInstance;

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, userGroupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        if (cloudInstance) {
            props.put("sasl.jaas.config", sasljass);
            props.put("sasl.mechanism", sasl);
            props.put("security.protocol", securityProc);
            props.put("basic.auth.credentials.source", secSource);
            props.put("schema.registry.basic.auth.user.info", regAuth);
        }
        props.put("schema.registry.url", schemaRegistryUrl);
        props.put("delivery.timeout.ms", 2147483647);

        return props;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, ClientTradeEventDTO>
    clientTradeEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ClientTradeEventDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clientTradeEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ClientTradeEventDTO> clientTradeEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }
}
