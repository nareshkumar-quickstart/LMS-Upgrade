package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CreditReportingField;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CreditReportingFieldRepositoryTest {

	@Inject
	private CreditReportingFieldRepository repoCreditReportingFieldRepository;
	
	
	//@Test
	public void creditReportingField_should_find_by_fieldLabel_fieldType_active() {
		
		String fieldLabel ="Repoting";
		String fieldType= "SINGLELINETEXTCREDITREPORTINGFIELD";
		List<CreditReportingField> lstContact = repoCreditReportingFieldRepository.findByFieldLabelLikeIgnoreCaseAndFieldTypeLikeIgnoreCaseAndActiveTrue('%'+fieldLabel+'%', '%'+fieldType+'%');
		
		System.out.println("..........");
	}
	
	@Test
	public void creditReportingField_should_find_by_fieldLabel_contentOwner_active() {
		
		String fieldLabel ="Repoting";
		Long co = 1L;
		List<CreditReportingField> lstContact = repoCreditReportingFieldRepository.findByFieldLabelAndContentOwnerIdAndActiveTrue(fieldLabel, co);
		
		System.out.println("..........");
	}
}
