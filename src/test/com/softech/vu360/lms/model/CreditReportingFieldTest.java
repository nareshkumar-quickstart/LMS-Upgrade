package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author marium.saud
 * The test case include CRUD operation for 
 * CreditReportingField
 * CreditReportingFieldValue
 * CreditReportingFieldValueChoice
 */
@Transactional
public class CreditReportingFieldTest extends
		TestBaseDAO<CreditReportingField> {
	

	@Before
	public void setRequiredService() {
		
	}

//	@Test
	public void CreditReportingField_should_save() throws Exception {

		CreditReportingField crf=new SingleLineTextCreditReportingFiled();
		crf.setActive(true);
		crf.setCustomFieldDescription("Test_CustomField_Description");
		crf.setFieldEncrypted(true);
		crf.setFieldRequired(true);
		crf.setFieldLabel("Test_FieldLabel");
		ContentOwner contentOwner=(ContentOwner)crudFindById(ContentOwner.class, new Long(1));
		crf.setContentOwner(contentOwner);
		crf.setHostName(null);
		
		try {

			crf = save(crf);
		} catch (Exception ex) {
			System.out.println(crf.getId());
		}

	}
	
//	@Test
	public void CreditReportingField_should_update(){
		
		CreditReportingField crf_Update=getById(new Long(9658), CreditReportingField.class);
		crf_Update.setCustomFieldDescription("Test_CustomField_Description_Updated");
		crf_Update.setFieldLabel("Test_FieldLabel_Updated");

		try{
			crf_Update=update(crf_Update);
		}
		catch(Exception ex){
			System.out.println(crf_Update.getFieldLabel());
		}
	}
	
//	@Test
	public void CreditReportingFieldValueChoice_should_save(){
		
		CreditReportingFieldValueChoice crfValueChoice=new CreditReportingFieldValueChoice();
		CreditReportingField crf=(CreditReportingField)crudFindById(CreditReportingField.class, new Long(9658));
		crfValueChoice.setCreditReportingField(crf);
		crfValueChoice.setDisplayOrder(1);
		crfValueChoice.setLabel("Test_Label");
		crfValueChoice.setValue("Test_Value");
		try{
			crfValueChoice=(CreditReportingFieldValueChoice)crudSave(CreditReportingFieldValueChoice.class, crfValueChoice);
		}
		catch(Exception ex){
			System.out.println(crfValueChoice.getId());
		}
	}

//	@Test
	public void CreditReportingFieldValue_should_save(){
		
		CreditReportingFieldValue crfValue=new CreditReportingFieldValue();
		//==========Setting CreditReportingField
		CreditReportingField crf=(CreditReportingField)crudFindById(CreditReportingField.class, new Long(9658));
		crfValue.setReportingCustomField(crf);
		//==========Setting LearnerProfile
		LearnerProfile profile=(LearnerProfile)crudFindById(LearnerProfile.class, new Long(3));
		crfValue.setLearnerprofile(profile);
		//==========Setting CreditReportingFieldValueChoice
		List<CreditReportingFieldValueChoice> choiceList=new ArrayList<CreditReportingFieldValueChoice>();
		CreditReportingFieldValueChoice choice=(CreditReportingFieldValueChoice)crudFindById(CreditReportingFieldValueChoice.class, new Long(2280));
		choiceList.add(choice);
		crfValue.setCreditReportingValueChoices(choiceList);
		
		crfValue.setValue(new Date());
		
		try{
			crfValue=(CreditReportingFieldValue)crudSave(CreditReportingFieldValue.class, crfValue);
		}
		catch(Exception ex){
			System.out.println(crfValue.getId());
		}
	}

	@Test
	public void CreditReportingFieldValue_should_update(){
		
		CreditReportingFieldValue crfValue_Update=(CreditReportingFieldValue)crudFindById(CreditReportingFieldValue.class, new Long(27157));
		System.out.println("value " + crfValue_Update.getValue());
		crfValue_Update.setValue2("111-11-1111");
		try{
			crfValue_Update=(CreditReportingFieldValue)crudSave(CreditReportingFieldValue.class, crfValue_Update);
		}
		catch(Exception ex){
			System.out.println(crfValue_Update.getValue2());
		}
	}

}
