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
 * Test case has been done for following entities:
 * Provider
 * ProviderAudit
 */
@Transactional
public class ProviderTest extends TestBaseDAO<Provider> {


	@Before
	public void setRequiredService() {
	}

	//@Test
	public void Provider_should_save_with_Address_CustomField_CustomFieldValue() throws Exception {

		Provider provider = new Provider();
		provider.setName("Test_Name");
		provider.setContactName("Test_ContactName");
		
		Address address=new Address();
		provider.setAddress(address);
		
		//Get Regulatory Analyst from User and set content owner
		ContentOwner contentowner=(ContentOwner)crudFindById(ContentOwner.class, new Long(1));
		provider.setContentOwner(contentowner);
	
		//Setting customfield of type 'SingleLineTextCustomFiled'
		List<CustomField> customFieldList=new ArrayList<CustomField>();
		SingleLineTextCustomFiled customField_1=new SingleLineTextCustomFiled();
		customField_1.setFieldLabel("Recommendation");
		customFieldList.add(customField_1);
		provider.setCustomfields(customFieldList);
		
		//Setting customfieldvalue
		List<CustomFieldValue> fieldValue=new ArrayList<CustomFieldValue>();
		CustomFieldValue customFieldValue_1=new CustomFieldValue();
		customFieldValue_1.setValue("Yes");
		customFieldValue_1.setCustomField(customField_1);
		fieldValue.add(customFieldValue_1);
		provider.setCustomfieldValues(fieldValue);
		
		save(provider);

	}

	//@Test
	public void Provider_should_update() throws Exception {

		Provider updateProvider = new Provider();
		updateProvider = getById(new Long(3055), Provider.class);
		updateProvider.setName("Test_Name_Updated");
		updateProvider.getCustomfields().remove(0);
		updateProvider.getCustomfieldValues().remove(0);
		
		update(updateProvider);
		

	}
	@Test
	public void ProviderAudit_should_save() throws Exception{
		
		ProviderAudit provideraudit = new ProviderAudit();
		
		Provider provider = new Provider();
		provider = getById(new Long(3055), Provider.class);
        
		provideraudit.setAddressId(provider.getAddress().getId());
		provideraudit.setRegulatorId(null);
		provideraudit.setContentownerId(provider.getContentOwner().getId());
		provideraudit.setContactName(provider.getContactName());
		provideraudit.setName(provider.getName());
		provideraudit.setPhone(provider.getPhone());
		provideraudit.setWebsite(provider.getWebsite());
		provideraudit.setEmailAddress(provider.getEmailAddress());
		provideraudit.setFax(provider.getFax());
		provideraudit.setActive(provider.isActive());
		provideraudit.setProviderId(provider.getId());
			provideraudit.setOperation("Add");
			provideraudit.setCreatedOn(new Date());
	    crudSave(ProviderAudit.class, provideraudit);
}
}
