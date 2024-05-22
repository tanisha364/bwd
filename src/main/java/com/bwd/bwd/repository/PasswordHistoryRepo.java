package com.bwd.bwd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bwd.bwd.model.auth.PasswordHistory;


public interface PasswordHistoryRepo extends JpaRepository<PasswordHistory, Long> {

}
