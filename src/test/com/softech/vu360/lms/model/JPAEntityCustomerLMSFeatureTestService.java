package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Test;

@Transactional
public class JPAEntityCustomerLMSFeatureTestService  extends TestBaseDAO<Object>{
	
	@Test
	public void testLMSFeature() throws Exception {
		
		CustomerLMSFeature cf = new CustomerLMSFeature();
		cf.setCustomer( (Customer) crudFindById(Customer.class, new Long(3L)));
		
		cf.setEnabled(false);
		cf.setLmsFeature( (LMSFeature) crudFindById( LMSFeature.class, new Long(3L)));
		cf.setLocked(false);
		cf = (CustomerLMSFeature) crudSave(CustomerLMSFeature.class, cf);

		
		CustomerLMSFeature cc = (CustomerLMSFeature) crudFindById(CustomerLMSFeature.class, cf.getId());
		update(cc);
		
	}
	
	
	
}