package com.bwd.bwd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bwd.bwd.model.auth.UserAccountsAuth;

@Repository
public interface UserAccountsAuthRepo  extends JpaRepository<UserAccountsAuth, Long>
{
	List<UserAccountsAuth> findByEmail(String email);	
	
	UserAccountsAuth getReferenceById(Long useraccountid);
	
	UserAccountsAuth getReferenceByUserid(String userid);
	
    List<UserAccountsAuth> findAll();	
}