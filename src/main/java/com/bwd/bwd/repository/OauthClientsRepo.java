package com.bwd.bwd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bwd.bwd.model.auth.OauthClients;

@Repository
public interface OauthClientsRepo extends JpaRepository<OauthClients, Long> {
	
	@Query(nativeQuery = true, value = "SELECT Count(*) FROM `oauth_clients` WHERE `client_id` = ?1 AND client_secret = ?2")
	public Long isRecordExist(String client_id, String client_secret); 
	
}