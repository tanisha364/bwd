package com.bwd.bwd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bwd.bwd.model.auth.RegistrationActivity;
import com.bwd.bwd.model.auth.UserAccountsAuth;

@Repository
public interface RegistrationActivityRepo extends JpaRepository<RegistrationActivity, Long>{

}
