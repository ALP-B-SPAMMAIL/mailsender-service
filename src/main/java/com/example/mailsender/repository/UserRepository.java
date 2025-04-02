package com.example.mailsender.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailsender.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
}
