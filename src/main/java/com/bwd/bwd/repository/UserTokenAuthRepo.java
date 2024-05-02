package com.bwd.bwd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bwd.bwd.model.auth.UesrTokenAuth;

public interface UserTokenAuthRepo extends JpaRepository<UesrTokenAuth, Long> {

}
