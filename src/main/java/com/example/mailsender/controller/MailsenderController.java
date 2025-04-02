package com.example.mailsender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mailsender.service.MailsenderService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class MailsenderController {
    @Autowired
    private MailsenderService mailsenderService;

    @GetMapping("/createReport")
    public String createReport() throws JsonProcessingException {
        mailsenderService.createReport();
        return "success";
    }
}
