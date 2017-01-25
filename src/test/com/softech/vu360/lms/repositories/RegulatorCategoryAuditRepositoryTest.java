package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.RegulatorCategoryAudit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class RegulatorCategoryAuditRepositoryTest {

	@Inject
	RegulatorCategoryAuditRepository regulatorCategoryAuditRepository;
	
	@Test
	public void regulatorCategoryAudit_should_save() {
		RegulatorCategoryAudit saveRegulatorCategoryAudit = regulatorCategoryAuditRepository.findOne(new Long(2105));
		saveRegulatorCategoryAudit.setId(null);
		
		RegulatorCategoryAudit savedRegulatorCategoryAudit = regulatorCategoryAuditRepository.save(saveRegulatorCategoryAudit);
		
		System.out.println("..........");
	}
	
	//@Test
	public void regulatorCategoryAudit_should_find() {
		RegulatorCategoryAudit saveRegulatorCategoryAudit = regulatorCategoryAuditRepository.findOne(new Long(2105));
		
		System.out.println("..........");
	}
	
	//@Test
	public void regulatorCategoryAudit_should_deleteAll(List<RegulatorCategoryAudit> regulatorCategorys) {
		regulatorCategoryAuditRepository.delete(regulatorCategorys);
		
		System.out.println("..........");
	}

}
