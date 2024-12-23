package com.bwd.bwd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bwd.bwd.model.auth.UserInvite;

public interface UserInviteRepo extends JpaRepository<UserInvite, Long>{

}
