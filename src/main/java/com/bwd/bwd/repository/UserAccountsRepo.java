package com.bwd.bwd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bwd.bwd.model.UserAccounts;

@Repository
public interface UserAccountsRepo extends JpaRepository<UserAccounts, Long>
{
	List<UserAccounts> findByEmail(String email);	
//	UserAccountsAuth findById();
	
    List<UserAccounts> findAll();	
}