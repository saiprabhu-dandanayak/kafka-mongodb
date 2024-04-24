package com.prabhu.kafka.consumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prabhu.kafka.consumer.document.Book;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.data.mongodb.core.MongoOperations;
import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Autowired
    private MongoOperations mongoOperations;

    @Bean
    public ConsumerFactory<String, Book> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(Book.class, false)));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Book> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Book> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @KafkaListener(topics = "book-topic", groupId = "my-group")
    public void consumeMessage(ConsumerRecord<String, Book> record) {
        try {
            Book book = record.value();
            mongoOperations.save(book);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
