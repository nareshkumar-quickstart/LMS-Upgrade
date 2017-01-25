package com.softech.vu360.lms.repositories;


import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;





import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.LearnerPreferences;
import com.softech.vu360.lms.repositories.LearnerRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CreditReportingFieldValueChoiceRepositoryTest{

	@Inject
	private CreditReportingFieldValueChoiceRepository creditRepoFieldValueChoiceRepository;

	@Test
	public void CreditReportingFieldValueChoice_should_findByCreditReportingFieldDisplayOrderAsc() {
		List<CreditReportingFieldValueChoice> choices=creditRepoFieldValueChoiceRepository.findByCreditReportingFieldIdOrderByDisplayOrderAsc(355L);
		System.out.println("CreditReportingFieldValueChoice list size :" +((Collection<CreditReportingFieldValueChoice>)choices).size());
	}

	//@Test
	public void CreditReportingFieldValueChoice_should_findByCreditReportingField() {
		List<CreditReportingFieldValueChoice> choices=creditRepoFieldValueChoiceRepository.findByCreditReportingFieldId(355L);
		System.out.println("CreditReportingFieldValueChoice list size :" +((Collection<CreditReportingFieldValueChoice>)choices).size());
	}
}
