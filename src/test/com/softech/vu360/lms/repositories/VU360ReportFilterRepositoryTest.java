package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;
import org.junit.Test;
import com.softech.vu360.lms.model.VU360ReportFilter;
import com.softech.vu360.lms.repositories.VU360ReportFilterRepository;

public class VU360ReportFilterRepositoryTest extends SpringJUnitConfigAbstractTest{

	@Inject
	private VU360ReportFilterRepository reportRepository;
	
	@Test
	public void findByReportId() {
		List<VU360ReportFilter> r = reportRepository.findByReportId(327L);
		System.out.println(r.get(0).getReport().getTitle());
		System.out.println(r.get(0).getOperand().getDataType());
	}
}
