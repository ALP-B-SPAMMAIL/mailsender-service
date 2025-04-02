package com.example.mailsender.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.mailsender.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByEmailIsNotNull();
}
