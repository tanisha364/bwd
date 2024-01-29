package com.bwd.bwd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bwd.bwd.model.Category;
import com.bwd.bwd.model.jobsmith.Reports;

public interface ReportRepo  extends JpaRepository<Reports, Long> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM `jobsmith_category_tbl` WHERE `jobsmith_category_type` = ?1 AND archived = 0")
	List<Reports> findByCategoryType(int categoryType);	

}
