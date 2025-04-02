package com.example.mailsender.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailsender.model.SpamStatics;

public interface SpamStaticsRepository extends JpaRepository<SpamStatics, Integer> {
    
}
