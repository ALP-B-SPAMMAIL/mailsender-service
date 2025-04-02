package com.example.mailsender.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailsender.model.Mailsender;

public interface MailsenderRepository extends JpaRepository<Mailsender, Integer> {
}
