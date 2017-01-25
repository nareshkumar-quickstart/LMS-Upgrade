package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Test;

@Transactional
public class JPAEntityLicenseIndustryTestService  extends TestBaseDAO<Object>{

	
	@Test
	public void testLicenceIndustryEntityUpdate() throws Exception {
		
		LicenseIndustry b = new LicenseIndustry();
		b.setName("ddd");
		b.setShortName("dd33d");
		b = (LicenseIndustry) crudSave(LicenseIndustry.class, b);
		
		LicenseIndustry c = (LicenseIndustry) crudFindById(LicenseIndustry.class, b.getId());
		c.setName("tttt");
		update(c);
		
	}
}
