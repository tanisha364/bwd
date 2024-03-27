package com.bwd.bwd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bwd.bwd.model.auth.UserEmailsAuth;

public interface UserEmailAuthRepo  extends JpaRepository<UserEmailsAuth, Long> {
	
	UserEmailsAuth findByEmail(String email);
	
	List<UserEmailsAuth> findAll();
}