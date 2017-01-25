package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.SuggestedTraining;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyOwner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class SuggestedTrainingRepositoryTest {

	@Inject
	private SuggestedTrainingRepository suggestedTrainingRepository;
	
	//@Test
	public void SuggestedTraining_should_findOne() {
		suggestedTrainingRepository.findOne(1L);
	}
	
	//@Test
	public void SuggestedTraining_should_save() {
		suggestedTrainingRepository.save(suggestedTrainingRepository.findOne(1L));
	}
		
	//@Test
	public void SuggestedTraining_shoudl_delete() {
		suggestedTrainingRepository.delete(suggestedTrainingRepository.findOne(1L));
	}
	
	//@Test
	public void findBySurveyId(){
		
		List<Course> courses = suggestedTrainingRepository.findBySurveyId(7574L).get(0).getCourses(); 
		for(Course obj : courses){
			System.out.println(obj.getId());
		}
		
		
	}
	
	@Test
	public void findBySurveyName(){
		long surveyOwnerId=9703,distributorId=103;
		String surveyName="Ahsun Taqveem Survey";//LMS9184 Reseller Survey";"Ahsun Taqveem Survey";
		String surveyStatus="";
		String isLockedStr=Survey.RETIRE_SURVEY.Yes.toString();
		String readOnlyStr=Survey.Editable.Yes.toString();
		SurveyOwner surveyOwner = new Customer();
		Customer cus = new Customer();
	    cus.setId(1L);
	    Boolean isLocked=null;
		Boolean readOnly=null;
		List<SuggestedTraining> lst =new ArrayList();
	     
	     
	     Distributor dis = new Distributor();
	     dis.setId(9703L);
	     cus.setDistributor(dis);
	     surveyOwner = cus;
		if(surveyOwner.getOwnerType().equalsIgnoreCase("CUSTOMER")){
			Customer customer=(Customer)surveyOwner;
			if (surveyStatus.isEmpty()) {
				if(!isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.All.toString())){
					if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.Yes.toString()))
						isLocked=true;	
						//whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(true));
					else if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.No.toString()))
						isLocked=false;
						//whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(false));
				}
				if(!readOnlyStr.equalsIgnoreCase(Survey.Editable.All.toString())){
					if(readOnlyStr.equalsIgnoreCase(Survey.Editable.Yes.toString()))
						readOnly=false;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(false));
					else if(readOnlyStr.equalsIgnoreCase(Survey.Editable.No.toString()))
						readOnly=true;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(true));
				}
				
				if(isLocked!=null && readOnly!=null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyIdOrDistributorIdAndIsLockedAndReadOnly(surveyOwner.getId(), customer.getDistributor().getId(), surveyName, isLocked, readOnly);
				else if(isLocked!=null && readOnly==null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyIdOrDistributorIdAndIsLocked(surveyOwner.getId(), customer.getDistributor().getId(), surveyName, isLocked);
				else if(isLocked==null && readOnly!=null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyIdOrDistributorIdAndReadOnly(surveyOwner.getId(), customer.getDistributor().getId(), surveyName, readOnly);
				else
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyIdOrDistributorId(surveyOwner.getId(), customer.getDistributor().getId(), surveyName);
				
			}

			else {
				if(!isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.All.toString())){
					if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.Yes.toString()))
						isLocked=true;
				//		whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(true));
					else if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.No.toString()))
						isLocked=false;
					//	whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(false));
				}
				if(!readOnlyStr.equalsIgnoreCase(Survey.Editable.All.toString())){
					if(readOnlyStr.equalsIgnoreCase(Survey.Editable.Yes.toString()))
						readOnly=false;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(false));
					else if(readOnlyStr.equalsIgnoreCase(Survey.Editable.No.toString()))
						readOnly=true;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(true));
				}
				
				if(isLocked!=null && readOnly!=null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorIdAndIsLockedAndReadOnly(cus.getId(), surveyStatus, cus.getDistributor().getId(), surveyName, isLocked, readOnly);
				else if(isLocked!=null && readOnly==null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorIdAndIsLocked(cus.getId(), surveyStatus, cus.getDistributor().getId(), surveyName, isLocked);
				else if(isLocked==null && readOnly!=null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorIdAndReadOnly(cus.getId(), surveyStatus, cus.getDistributor().getId(), surveyName, readOnly);
				else
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorId(cus.getId(), surveyStatus, cus.getDistributor().getId(), surveyName);
					
			}
		}else{
			if (surveyStatus.isEmpty()) {
				if(!isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.All.toString())){
					if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.Yes.toString()))
						isLocked=true;
				//		whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(true));
					else if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.No.toString()))
						isLocked=false;
					//	whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(false));
				}	
				if(!readOnlyStr.equalsIgnoreCase(Survey.Editable.All.toString())){
					if(readOnlyStr.equalsIgnoreCase(Survey.Editable.Yes.toString()))
						readOnly=true;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(true));
					else if(readOnlyStr.equalsIgnoreCase(Survey.Editable.No.toString()))
						readOnly=false;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(false));
				}		
				if(isLocked!=null && readOnly!=null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyIdAndOwnerTypeAndIsLockedAndReadOnly(cus.getId(),  surveyName,cus.getOwnerType(), isLocked, readOnly);
				else if(isLocked!=null && readOnly==null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyIdAndOwnerTypeAndIsLocked(cus.getId(), surveyName,cus.getOwnerType(),isLocked);
				else if(isLocked==null && readOnly!=null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyIdAndOwnerTypeAndReadOnly(cus.getId(), surveyName, cus.getOwnerType(),readOnly);
				else
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyIdAndOwnerType(cus.getId(), surveyName,cus.getOwnerType());
					
				
			}
	
			else {
				
				if(!isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.All.toString())){
					if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.Yes.toString()))
						isLocked=true;
				//		whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(true));
					else if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.No.toString()))
						isLocked=false;
					//	whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(false));
				}
				if(!readOnlyStr.equalsIgnoreCase(Survey.Editable.All.toString())){
					if(readOnlyStr.equalsIgnoreCase(Survey.Editable.Yes.toString()))
						readOnly=false;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(false));
					else if(readOnlyStr.equalsIgnoreCase(Survey.Editable.No.toString()))
						readOnly=true;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(true));
				}
				if(isLocked!=null && readOnly!=null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndIsLockedAndReadOnlyAndStatus(cus.getId(), surveyName,surveyStatus,cus.getOwnerType(), isLocked, readOnly);
				else if(isLocked!=null && readOnly==null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndIsLockedAndStatus(cus.getId(), surveyName,surveyStatus,cus.getOwnerType(), isLocked);
				else if(isLocked==null && readOnly!=null)
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndReadOnlyAndStatus(cus.getId(), surveyName,surveyStatus,cus.getOwnerType(), readOnly);
				else
					lst = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndStatus(cus.getId(), surveyName,surveyStatus,cus.getOwnerType());
					
				
			}
		}
		
		//List<SuggestedTraining> lst = suggestedTrainingRepository.findBySurveyNameAndSurveyIdOrDistributorIdAndIsLockedAndReadOnly(surveyOwner.getId(), distributorId, surveyName,  true, false);
		System.out.println("*************** "+lst.size());
		System.out.print("&&&&&&&&&&&&&&&&&&&"+lst.get(0).getId());
	}
}
