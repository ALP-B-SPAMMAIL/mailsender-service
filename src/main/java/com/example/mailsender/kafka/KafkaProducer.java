package com.example.mailsender.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.mailsender.event.AbstractEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaProducer {
    private static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void publish(AbstractEvent event) throws JsonProcessingException {
        kafkaTemplate.send(event.getTopic(), objectMapper.writeValueAsString(event));
    }
}
