package com.softech.vu360.lms.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.VU360ReportFilterOperand;

/**
 * Repository interface for {@code VU360ReportFilterOperand}s.
 * 
 * @author muhammad.rehan
 * 
 */
public interface VU360ReportFilterOperandRepository extends CrudRepository<VU360ReportFilterOperand, Long> {

	/////public List<VU360ReportFilterOperand> getReportFilterOperands();
	List<VU360ReportFilterOperand> findAllByOrderByDisplayOrderAsc();
}
