package com.bwd.bwd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bwd.bwd.model.auth.UserAssociationAuth;

public interface UserAssociationAuthRepo extends JpaRepository<UserAssociationAuth, Long>{

}
