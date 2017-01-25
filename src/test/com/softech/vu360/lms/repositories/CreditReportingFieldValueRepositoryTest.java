package com.softech.vu360.lms.repositories;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class CreditReportingFieldValueRepositoryTest{
	
	//public LearnerService learnerService;
	
	@Autowired
	ApplicationContext context;

	@Inject
	private CreditReportingFieldValueRepository creditRepoFieldValueRepository;

	@Before
	public void CustomerRepositoryInit(){
		//learnerService = (LearnerService) context.getBean("learnerService");
    }
	
	//@Test
	public void CreditReportingFieldValue_should_findById() {
		CreditReportingFieldValue value=creditRepoFieldValueRepository.findOne(203L);
		System.out.println("CreditReportingFieldValue value is :" +value.getValue());
	}
	
	//@Test
	public void CreditReportingFieldValue_should_findByLearenrProfile_Learner_ID() {
		List<CreditReportingFieldValue> valueList=creditRepoFieldValueRepository.findByLearnerprofile_Learner_Id(3L);
		System.out.println("CreditReportingFieldValue value is :" +valueList.size());
	}
	
	//@Transactional
	//@Test
	public void CreditReportingFieldValue_should_save() {
		CreditReportingFieldValue value=creditRepoFieldValueRepository.findOne(27157L);
		System.out.println("CreditReportingFieldValue value is :" +value.getValue());
		CreditReportingFieldValue new_value=new CreditReportingFieldValue();
		
		List<CreditReportingFieldValueChoice> valueChoicesList=value.getCreditReportingValueChoices();
		List<CreditReportingFieldValueChoice> new_valueChoicesList=new ArrayList<CreditReportingFieldValueChoice>();
		CreditReportingFieldValueChoice choice=new CreditReportingFieldValueChoice();
		choice.setCreditReportingField(valueChoicesList.get(0).getCreditReportingField());
		choice.setLabel("Test_SpringData");
		choice.setValue("Test_SpringData_Value");
		choice.setDisplayOrder(2);
		new_valueChoicesList.add(choice);
		
		new_value.setLearnerprofile(value.getLearnerprofile());
		new_value.setReportingCustomField(value.getReportingCustomField());
		new_value.setValue(value.getValue());
		new_value.setCreditReportingValueChoices(new_valueChoicesList);
		
//		/learnerService.saveCreditReportingfieldValue(new_value);
	}

//	@Test
	public void creditReportingFieldValue_should_findBy_creditReportingFieldId() {
		List<CreditReportingFieldValue> valueList=creditRepoFieldValueRepository.findByReportingCustomFieldId(52L);
		System.out.println("CreditReportingFieldValue value is :" +valueList.size());
	}
	
	//@Test
	public void creditReportingFieldValue_should_findBy_creditReportingFieldId_And_learnerprofileId() {
		Long creditReportingFieldId=52L;
		Long learnerprofileId=855L;
		List<CreditReportingFieldValue> creditReportingFieldValue=creditRepoFieldValueRepository.findByReportingCustomFieldIdAndLearnerprofileId(creditReportingFieldId, learnerprofileId);
		System.out.println("CreditReportingFieldValue ");
	}
	
	//@Test
	public void creditReportingFieldValue_should_findBy_learnerprofileId() {
		Long learnerprofileId=855L;
		List<CreditReportingFieldValue> creditReportingFieldValue=creditRepoFieldValueRepository.findByLearnerprofileId(learnerprofileId);
		System.out.println("CreditReportingFieldValue ");
	}
	
	@Test
	@Transactional
	public void creditReportingFieldValue_should_save_Null_As_Object_For_Value_Columns(){
		CreditReportingFieldValue crf_Value=new CreditReportingFieldValue();
		CreditReportingFieldValue value=creditRepoFieldValueRepository.findOne(27157L);
	
		crf_Value.setLearnerprofile(value.getLearnerprofile());
		crf_Value.setReportingCustomField(value.getReportingCustomField());
		creditRepoFieldValueRepository.save(crf_Value);
		
	}
}
