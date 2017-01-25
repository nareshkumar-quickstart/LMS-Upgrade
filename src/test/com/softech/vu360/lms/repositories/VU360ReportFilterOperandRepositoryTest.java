package com.softech.vu360.lms.repositories;

import java.util.List;
import javax.inject.Inject;
import org.junit.Test;
import com.softech.vu360.lms.model.VU360ReportFilterOperand;
import com.softech.vu360.lms.repositories.VU360ReportFilterOperandRepository;

public class VU360ReportFilterOperandRepositoryTest extends SpringJUnitConfigAbstractTest{

	@Inject
	private VU360ReportFilterOperandRepository reportFilterOpeRepository;
	
	@Test
	public void findByReportIdAndUserId() {
		List<VU360ReportFilterOperand> r = reportFilterOpeRepository.findAllByOrderByDisplayOrderAsc();
		System.out.println(r.get(0).getDataType());
		System.out.println(r.get(0).getDisplayOrder());
	}
}
