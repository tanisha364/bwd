package com.bwd.bwd.repository.jobsmith;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bwd.bwd.model.jobsmith.JobsmithReport;

public interface JobsmithReportRepo    extends JpaRepository<JobsmithReport, Long> {

}