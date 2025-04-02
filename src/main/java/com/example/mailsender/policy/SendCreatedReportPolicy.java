package com.example.mailsender.policy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.mailsender.event.ReportCreatedEvent;
import com.example.mailsender.eventDto.ReportCreatedEventDto;
import com.example.mailsender.service.MailsenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;



@Service
public class SendCreatedReportPolicy {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MailsenderService mailsenderService;
    
    @KafkaListener(topics = "mail", groupId = "ai-mail-sender-report-created")
    public void listen(
            @Header(value = "type", required = false) String type,
            @Payload String data
    ) {
        objectMapper.registerModule(new JavaTimeModule());
        if (type != null && type.equals("ReportCreatedEvent")) {
            try {
                ReportCreatedEvent event = objectMapper.readValue(data, ReportCreatedEvent.class);
                ReportCreatedEventDto payload = event.getPayload();
                if (payload != null) {
                    mailsenderService.sendReport(payload);
                } else {
                    System.out.println("Warning: Payload is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
