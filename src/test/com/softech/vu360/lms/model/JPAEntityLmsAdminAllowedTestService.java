package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Test;

@Transactional
public class JPAEntityLmsAdminAllowedTestService  extends TestBaseDAO<Object>{

	
	@Test
	public void testLMSAdminAllowedEntityUpdate() throws Exception {
		
		LMSAdministratorAllowedDistributor b = new LMSAdministratorAllowedDistributor();
		b.setAllowedDistributorId(454L);
		b.setDistributorGroupId(3104L);
		b.setLmsAdministratorId(53L);
		b = (LMSAdministratorAllowedDistributor) crudSave(LMSAdministratorAllowedDistributor.class, b);
		
		LMSAdministratorAllowedDistributor c = (LMSAdministratorAllowedDistributor) crudFindById(LMSAdministratorAllowedDistributor.class, b.getId());
		c.setAllowedDistributorId(855L);
		update(c);
		
	}
}
