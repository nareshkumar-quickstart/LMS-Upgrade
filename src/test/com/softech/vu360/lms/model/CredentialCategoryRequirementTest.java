package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author raja.ali 
 */
@Transactional
public class CredentialCategoryRequirementTest extends TestBaseDAO<CredentialCategoryRequirement> {

	
	@Before
	public void setRequiredService() {
		

	}

	@Test
	public void testCredentialCategoryRequirement() throws Exception {
		System.out.println("#### Unit Test Survey using JUnit ####");
		
		//** CredentialCategoryRequirement ****
//		credentialCategoryRequirement_should_save();
//		CredentialCategoryRequirement objCredentialCategoryRequirement = getById(new Long("2907"), CredentialCategoryRequirement.class);
		
	}
	
	
	
	
	private CredentialCategoryRequirement credentialCategoryRequirement_should_save() {
		CredentialCategoryRequirement objCredentialCategoryRequirement = new CredentialCategoryRequirement();
		try{
			
			objCredentialCategoryRequirement = (CredentialCategoryRequirement) crudSave(CredentialCategoryRequirement.class, objCredentialCategoryRequirement);
			System.out.println("objCredentialCategoryRequirement.id::"+objCredentialCategoryRequirement.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return objCredentialCategoryRequirement;
	}
	

}
