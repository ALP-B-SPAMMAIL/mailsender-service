package com.example.mailsender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.example.mailsender.repository.MailsenderRepository;
import com.example.mailsender.eventDto.ReportCreatedEventDto;
import com.example.mailsender.model.Mailsender;
import java.time.LocalDateTime;

@Service
public class MailsenderService {
    @Autowired
    private MailsenderRepository mailsenderRepository;
    
    @Autowired
    private JavaMailSender javaMailSender;

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
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(targetAddress);
            message.setSubject(mailTitle);
            message.setText(mailContent);
            javaMailSender.send(message);
        } catch (Exception e) {
            // 메일 전송 실패 시 로깅
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}