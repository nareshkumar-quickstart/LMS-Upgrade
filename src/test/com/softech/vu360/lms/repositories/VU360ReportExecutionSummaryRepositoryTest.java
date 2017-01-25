package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.model.VU360ReportExecutionSummary;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.VU360ReportExecutionSummaryRepository;
import com.softech.vu360.lms.repositories.VU360ReportRepository;
import com.softech.vu360.lms.repositories.VU360UserRepository;

public class VU360ReportExecutionSummaryRepositoryTest extends SpringJUnitConfigAbstractTest {

	@Inject
	private VU360ReportExecutionSummaryRepository reportExeRepository;
	
	@Inject
	private VU360ReportRepository reportRepository;
	
	@Inject
	private VU360UserRepository userRepository;
	
	//@Test
	public void findByReportIdAndUserId() {
		List<VU360ReportExecutionSummary> r = reportExeRepository.findByReportIdAndUserIdOrderByExecutionDateDesc(3650L, 3L);
		System.out.println(r.get(0).getCsvLocation());
		System.out.println(r.get(0).getReport().getDescription());
	}
	
	
	@Test
	public void save() {
		VU360ReportExecutionSummary summ = new VU360ReportExecutionSummary();
		summ.setCsvLocation("12345");
		summ.setExecutionDate(new Date());
		summ.setHtmlLocation("67890");
		
		VU360User vu360User = userRepository.findOne(211185L);
		VU360Report report = reportRepository.findOne(new Long(5930));
		
		summ.setReport(report);
		summ.setUser(vu360User);
	    
		reportExeRepository.save(summ);
	}
}
