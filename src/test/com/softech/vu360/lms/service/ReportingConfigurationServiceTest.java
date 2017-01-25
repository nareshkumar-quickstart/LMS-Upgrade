package com.softech.vu360.lms.service;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.VU360UserRepository;
import com.softech.vu360.lms.service.ReportingConfigurationService;

public class ReportingConfigurationServiceTest extends SpringJUnitConfigAbstractTest {

	@Inject
	private ReportingConfigurationService reportingConfigurationService;
	
	@Inject
	private VU360UserRepository userRepo;
	
	//@Test
	public void getSystemReports() {
		List<VU360Report> rpt = reportingConfigurationService.getSystemReports("MANAGER");
		System.out.println(rpt.get(0).getCategory());
	}

	@Test
	public void getOwnedReports() {
		VU360User user = userRepo.findOne(3L);
		List<VU360Report> rpt = reportingConfigurationService.getOwnedReports(user);
		System.out.println(rpt.get(0).getCategory());
	}
}
