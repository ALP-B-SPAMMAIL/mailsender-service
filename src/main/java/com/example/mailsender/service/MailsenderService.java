package com.example.mailsender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.example.mailsender.repository.MailsenderRepository;
import com.example.mailsender.repository.SpamStaticsRepository;
import com.example.mailsender.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.mailsender.event.ReportRequestCreatedEvent;
import com.example.mailsender.eventDto.ReportCreatedEventDto;
import com.example.mailsender.eventDto.ReportRequestCreatedEventDto;
import com.example.mailsender.kafka.KafkaProducer;
import com.example.mailsender.model.Mailsender;
import com.example.mailsender.model.SpamStatics;
import com.example.mailsender.model.User;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MailsenderService {
    @Autowired
    private MailsenderRepository mailsenderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SpamStaticsRepository spamStaticsRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    public void sendReport(ReportCreatedEventDto reportCreatedEventDto) {
        // 메일 전송 로직
        String targetAddress = reportCreatedEventDto.getTargetAddress();
        String mailTitle = "AI 생성 리포트를 보내드립니다.";
        String mailContent = reportCreatedEventDto.getReport();
        
        sendMail(targetAddress, mailTitle, mailContent);
        
        // 메일 전송 기록 저장
        Mailsender mailsender = Mailsender.builder()
            .targetAddress(targetAddress)
            .mailTitle(mailTitle)
            .mailContent(mailContent)
            .sendAt(LocalDateTime.now())
            .build();
        mailsenderRepository.save(mailsender);
    }

    private void sendMail(String targetAddress, String mailTitle, String mailContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(targetAddress);
            helper.setSubject(mailTitle);
            helper.setText(mailContent, true); // true indicates HTML content
            javaMailSender.send(message);
        } catch (Exception e) {
            // 메일 전송 실패 시 로깅
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void createReport() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 리포트 생성 로직
        List<User> users = userRepository.findByEmailIsNotNull();
        List<SpamStatics> spamStatics = spamStaticsRepository.findAll();
        int size = spamStatics.size();
        spamStatics = spamStatics.subList(0, size > 10 ? 10 : size);
        for (User user : users) {
            ReportRequestCreatedEventDto reportRequestCreatedEventDto = new ReportRequestCreatedEventDto();
            reportRequestCreatedEventDto.setTargetAddress(user.getEmail());
            reportRequestCreatedEventDto.setTargetAge(user.getBirthDate());
            reportRequestCreatedEventDto.setTargetGender(user.getGender());
            reportRequestCreatedEventDto.setTargetInterest(user.getInterest());
            reportRequestCreatedEventDto.setTopic(objectMapper.writeValueAsString(spamStatics));
            ReportRequestCreatedEvent reportRequestCreatedEvent = new ReportRequestCreatedEvent(reportRequestCreatedEventDto);
            kafkaProducer.publish(reportRequestCreatedEvent);
        }
    }
}