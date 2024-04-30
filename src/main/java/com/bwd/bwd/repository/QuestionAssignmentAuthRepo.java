package com.bwd.bwd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bwd.bwd.model.auth.QuestionAssignmentAuth;
import com.bwd.bwd.model.auth.UserAssociationAuth;

public interface QuestionAssignmentAuthRepo extends JpaRepository<QuestionAssignmentAuth, Long>{

}
