package com.example.demo.config;

import com.example.demo.documents.Customer; 
import com.example.demo.documents.Product; 
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumerConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @KafkaListener(topics = "productorder")
    public void consumeMessage(String messageJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(messageJson);
            String type = jsonNode.get("type").asText();
            
            if ("customer".equals(type)) {
                Customer customer = mapper.treeToValue(jsonNode.get("data"), Customer.class);
                customerRepository.save(customer);
            } else if ("product".equals(type)) {
                Product product = mapper.treeToValue(jsonNode.get("data"), Product.class);
                productRepository.save(product);
            }
        } catch (IOException e) {
            log.error("Error processing JSON", e);
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}
