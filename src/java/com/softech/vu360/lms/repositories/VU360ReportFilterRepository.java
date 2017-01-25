package com.softech.vu360.lms.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.VU360ReportFilter;

/**
 * Repository interface for {@code VU360ReportFilter}s.
 * 
 * @author muhammad.rehan
 * 
 */
public interface VU360ReportFilterRepository extends CrudRepository<VU360ReportFilter, Long> {

	//public List<VU360ReportFilter> getVU360ReportFilter(VU360Report report) 
	List<VU360ReportFilter> findByReportId(Long reportId);
}
