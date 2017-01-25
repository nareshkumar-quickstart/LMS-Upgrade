package com.softech.vu360.lms.model;


import javax.transaction.Transactional;

import org.junit.Test;

@Transactional
public class JPAEntityDistributorLMSFeatureTestService  extends TestBaseDAO<Object>{
	
	@Test
	public void testLMSFeature() throws Exception {
		
		DistributorLMSFeature cf = new DistributorLMSFeature();
		cf.setDistributor( (Distributor) crudFindById(Distributor.class, new Long(1)));
		
		cf.setEnabled(false);
		cf.setLmsFeature( (LMSFeature) crudFindById( LMSFeature.class, new Long(198)));
		cf.setLocked(false);
		cf = (DistributorLMSFeature) crudSave(DistributorLMSFeature.class, cf);

		
		DistributorLMSFeature cc = (DistributorLMSFeature) crudFindById(DistributorLMSFeature.class, cf.getId());
		update(cc);
		
	}
	
	
	
}