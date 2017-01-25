package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Test;

/**
 * 
 * @author haider.ali
 *
 */
@Transactional
public class JPAEntityCredentialCategoryTestService  extends TestBaseDAO<Object>{
	
	@Test
	public void testCredentialCategoryIneset() throws Exception {
		
		CredentialCategory b = new CredentialCategory();
		b = (CredentialCategory) crudFindById(CredentialCategory.class, new Long(1112L));
		
		
		CredentialCategory d = new CredentialCategory();
		d.setName("JUnit Test Category");
		d.setCategoryType(CredentialCategory.CONTINUING_EDUCATION);
		d.setHours((float) 1.5);
		d.setDescription("JUnit Test Category");
		d.setCredential( (Credential) crudFindById(Credential.class, new Long(2L)) );
		d=(CredentialCategory) crudSave(CredentialCategory.class, d) ;
		
		CredentialCategory c = (CredentialCategory) crudFindById(CredentialCategory.class, d.getId());
		c.setDescription("ddddddddddddddd");
		update(c);
		
	}
	
	
	
}