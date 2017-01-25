package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.VU360ReportExecutionSummary;

/**
 * Repository interface for {@code VU360ReportExecutionSummary}s.
 * 
 * @author muhammad.rehan
 * 
 */
public interface VU360ReportExecutionSummaryRepository extends CrudRepository<VU360ReportExecutionSummary, Long> {

	/////public List<VU360ReportExecutionSummary> findReportExecutionSummary(VU360Report report,VU360User user);
	List<VU360ReportExecutionSummary> findByReportIdAndUserIdOrderByExecutionDateDesc(Long reportId, Long userId);
	
}
