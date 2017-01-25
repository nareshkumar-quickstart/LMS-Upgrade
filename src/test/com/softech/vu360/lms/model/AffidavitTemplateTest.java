package com.softech.vu360.lms.model;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class AffidavitTemplateTest extends TestBaseDAO<AffidavitTemplate> {


	@Before
	public void setRequiredService() {
	}
	
//	@Test
	public void AffidavitTemplate_should_save(){
		
		AffidavitTemplate affidavitTemplate = new AffidavitTemplate();
		VU360User vu360User = (VU360User)crudFindById(VU360User.class, new Long(3));
		affidavitTemplate.setCreatedVU360User(vu360User);
		affidavitTemplate.setCreatedDate(new Date());
		affidavitTemplate.setStatus(true);
		affidavitTemplate.setTemplateGUID("Test_Template_GUID");
		affidavitTemplate.setTemplateName("Test_Template_Name");
		try{
			affidavitTemplate=save(affidavitTemplate);	
		}
		catch(Exception ex){
			System.out.println(affidavitTemplate.getId());
		}
		
	}
	
	@Test
	public void AffidavitTemplate_should_update(){
		AffidavitTemplate template=(AffidavitTemplate)crudFindById(AffidavitTemplate.class, new Long(1053));
		template.setTemplateName("Test_Template_Name_Updated");
		update(template);
	}
	
}
