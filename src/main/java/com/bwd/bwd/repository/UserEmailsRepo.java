package com.bwd.bwd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bwd.bwd.model.jobsmith.UserEmails;



public interface UserEmailsRepo extends JpaRepository<UserEmails, Long> {
	
	UserEmails findByEmail(String email);
	
	List<UserEmails> findAll();
}