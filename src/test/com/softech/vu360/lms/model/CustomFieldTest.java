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
 *  The test case include CRUD operation for 
 *  CustomField
 *  CustomFieldValue
 *  CustomFieldValueChoice
 */
@Transactional
public class CustomFieldTest extends
		TestBaseDAO<CustomField> {
	

	@Before
	public void setRequiredService() {
		
	}

//	@Test
	public void CustomField_should_save() throws Exception {

		CustomField cf=new SingleSelectCustomField();
		cf.setActive(true);
		cf.setCustomFieldDescription("Test_CustomField_Description_CF");
		cf.setFieldEncrypted(true);
		cf.setFieldRequired(true);
		cf.setFieldLabel("Test_FieldLabel_CF");
		//=============Setting CustomFieldContext0
		CustomFieldContext context=new CustomFieldContext();
		ContentOwner contentOwner=(ContentOwner)crudFindById(ContentOwner.class, new Long(1));
		context.setContentOwner(contentOwner);
		cf.setCustomFieldContext(context);
		
		
		try {

			cf = save(cf);
		} catch (Exception ex) {
			System.out.println(cf.getId());
		}

	}
	
//	@Test
	public void CustomField_should_update(){
		
		CustomField cf_Update=getById(new Long(15662), CustomField.class);
		cf_Update.setCustomFieldDescription("Test_CustomField_Description_CF_Updated");
		cf_Update.setFieldLabel("Test_FieldLabel_CF_Updated");

		try{
			update(cf_Update);
		}
		catch(Exception ex){
			System.out.println(ex);
		}
	}
	
//	@Test
	public void CustomFieldValueChoice_should_save(){
		
		CustomFieldValueChoice cfValueChoice=new CustomFieldValueChoice();
		//===========Setting CustomField
		CustomField field=new SingleSelectCustomField();
		field.setActive(true);
		field.setCustomFieldDescription("Test_CustomField_Description_CF_1");
		field.setFieldEncrypted(true);
		field.setFieldRequired(true);
		field.setFieldLabel("Test_FieldLabel_CF_1");
		cfValueChoice.setCustomField(field);
		
		cfValueChoice.setDisplayOrder(1);
		cfValueChoice.setLabel("Test_Label");
		cfValueChoice.setValue("Test_Value");
		try{
			cfValueChoice=(CustomFieldValueChoice)crudSave(CustomFieldValueChoice.class, cfValueChoice);
		}
		catch(Exception ex){
			System.out.println(cfValueChoice.getId());
		}
	}

//	@Test
	public void CustomFieldValue_should_save(){
		
		CustomFieldValue cfValue=new CustomFieldValue();
		//==========Setting CreditReportingField
		CustomField cf=(CustomField)crudFindById(CustomField.class, new Long(15663));
		cfValue.setCustomField(cf);
		//==========Setting CustomFieldValueChoice
		List<CustomFieldValueChoice> choiceList=new ArrayList<CustomFieldValueChoice>();
		CustomFieldValueChoice choice=(CustomFieldValueChoice)crudFindById(CustomFieldValueChoice.class, new Long(4704));
		choiceList.add(choice);
		cfValue.setValueItems(choiceList);
		
		cfValue.setValue(new Date());
		
		try{
			cfValue=(CustomFieldValue)crudSave(CustomFieldValue.class, cfValue);
		}
		catch(Exception ex){
			System.out.println(cfValue.getId());
		}
	}

	@Test
	public void CustomFieldValue_should_update(){
		
		CustomFieldValue cfValue_Update=(CustomFieldValue)crudFindById(CustomFieldValue.class, new Long(82329));
		System.out.println("value " + cfValue_Update.getValue());
		cfValue_Update.setValue(123);
		try{
			cfValue_Update=(CustomFieldValue)crudSave(CustomFieldValue.class, cfValue_Update);
		}
		catch(Exception ex){
			System.out.println(ex);
		}
	}

}
