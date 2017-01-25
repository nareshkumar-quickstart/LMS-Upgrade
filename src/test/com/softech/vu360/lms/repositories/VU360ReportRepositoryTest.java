package com.softech.vu360.lms.repositories;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.model.VU360User;


public class VU360ReportRepositoryTest extends SpringJUnitConfigAbstractTest {
	
	@Autowired
	private VU360ReportRepository reportRepository;
	
	@Autowired
	private VU360UserRepository userRepository;
	
	//@Test
	public void findById() {
		VU360Report r2 = reportRepository.findOne(253L);
		System.out.println(r2);
	}
	
	//@Test
	public void findBySystemOwnedOrderByTitleAsc() {
		List <VU360Report> r = reportRepository.findBySystemOwnedOrderByTitleAsc(true);
		System.out.println(r);
	}
	
	//@Test
	public void findByModeAndSystemOwnedOrderByTitleAsc() {
		List <VU360Report> r = reportRepository.findByModeAndSystemOwnedOrderByTitleAsc("MANAGER",true);
		System.out.println(r);
	}
	
	
	
	@Test
	public void findByOwnerId() {
		List <VU360Report> r = reportRepository.findByOwnerId(144L);
		System.out.println(r);
	}
	
	//@Test
	public void findByOwnerIdAndMode() {
		List <VU360Report> r = reportRepository.findByOwnerIdAndMode(144L, "MANAGER");
		System.out.println(r);
	}
	
	//@Test
	public void save() {
		VU360Report report = new VU360Report();
		VU360Report report2 = reportRepository.findOne(new Long(5348));
		VU360User vu360User = userRepository.findOne(211185L);
		report.setCategory("Registration");
		report.setDataSource("DataSource");
		report.setDerivedFrom(report2);
		report.setDescription("description");
		//report.setExecutionSummaries(executionSummaries);
		report.setFavorite(true);
		//report.setFields(fields);
		//report.setFilters(filters);
		report.setMode("MANAGER");
		report.setOriginalSystemReport(report2);
		report.setOwner(vu360User);
		//report.setParameter(parameter);
		report.setSqlTemplateUri("vm/reportsql/mgr_PrfLearnerByCourse.vm");
		report.setSystemOwned(true);
		report.setTitle("Rehan JAP RTeport");
		reportRepository.save(report);
	}
		
	
}
