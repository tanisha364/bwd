package com.bwd.bwd.repository.jobsmith;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bwd.bwd.model.jobsmith.Reports;

@Repository
public interface ReportsRepo  extends JpaRepository<Reports, Long> {
	List<Reports> findAll();
    
	String query = "SELECT * "
	+ " FROM reports_tbl rept"
	+ " INNER JOIN jobsmith_premade_reports_tbl jpre"
	+ " ON rept.rep_report_id = jpre.jobsmith_premade_reportid AND jpre.jobsmith_categoryid=:categoryId AND jpre.archived = 0";
@Query(nativeQuery = true, value = query)	
List<Reports> findByCategoryType(int categoryId);
}
