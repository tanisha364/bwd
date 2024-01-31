package com.bwd.bwd.repository.jobsmith;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bwd.bwd.model.jobsmith.JobsmithCapabilities;
import com.bwd.bwd.model.jobsmith.JobsmithReportCapability;

@Repository
public interface JobsmithReportCapabilityRepo   extends JpaRepository<JobsmithReportCapability, Long> {
	String query = "SELECT cap.Cap_CapabilityId, cap.Cap_Capability, jcap.jobsmith_categoryid as categoryid, Cap_Description FROM capability_tbl cap "
			+ "INNER JOIN jobsmith_capability_tbl jcap "
			+ " ON cap.Cap_CapabilityId = jcap.capabilityid"
			+ "AND jcap.jobsmith_categoryid = ?1 AND  jcap.archived = 0";
	@Query(nativeQuery = true, value = query)	
	List<JobsmithCapabilities> findByRkaid(int catid);
	
//	List<JobsmithCapabilities> findAll();	
}