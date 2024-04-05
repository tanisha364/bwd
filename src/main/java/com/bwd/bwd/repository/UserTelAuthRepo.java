package com.bwd.bwd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bwd.bwd.model.auth.UserTelsAuth;

public interface UserTelAuthRepo extends JpaRepository<UserTelsAuth, Long> { 

}
