package com.softech.vu360.lms.model;

import java.sql.Date;

import javax.transaction.Transactional;

import org.junit.Test;

@Transactional
public class JPAEntityCredentialTestService  extends TestBaseDAO<Object>{

	
	@Test
	public void testCredentialEntityUpdate() throws Exception {
		
		Credential c = new Credential();
		c.setActive(true);
		c.setContentOwner((ContentOwner) crudFindById(ContentOwner.class, new Long(53)));
		c.setCredentialType("d");
		c.setDescription("ds");
		c.setHardDeadlineDay("ddd");
		c.setHardDeadlineYear("s");
		c.setHardDeadlineMonth("dd");
		c.setInformationLastVerifiedDate(new Date(System.currentTimeMillis()));
		c.setJurisdiction("d");
		c.setOfficialLicenseName("sssssss");
		c.setOtherJurisdiction("daaa");
		c.setPreRequisite("asdasd");
		c.setRenewalDeadlineType("dsas");
		c.setRenewalFrequency("aaa");
		c.setShortLicenseName("dada");
		c.setStaggeredTo("daaasd");
		c.setTotalNumberOfLicense(4);
		c.setVerifiedBy("sss");
		c.setStaggeredBy("aaa");
		
		
		c = (Credential) crudSave(Credential.class, c);
		
		
		Credential dc = (Credential) crudFindById(Credential.class, c.getId());
		update(dc);
		
	}
}