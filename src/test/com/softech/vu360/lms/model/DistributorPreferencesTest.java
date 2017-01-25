package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author raja.ali 
 */
@Transactional
public class DistributorPreferencesTest extends TestBaseDAO<DistributorPreferences> {

	
	@Before
	public void setRequiredService() {
		

	}

	@Test
	public void distributorPreferences_test() throws Exception {
		System.out.println("#### Unit Test DistributorPreferences using JUnit ####");
		
		//** DistributorPreferences ****
		//DistributorPreferences distributorPreferences = distributorPreferences_should_save();
		DistributorPreferences objDistributorPreferences = getById(new Long("101"), DistributorPreferences.class);
		//DistributorPreferences objDistributorPreferences = getById(new Long("1"), DistributorPreferences.class);
		
		System.out.println("........Finish.........");
		
	}
	
	
	
	
	private DistributorPreferences distributorPreferences_should_save() {
		DistributorPreferences objDistributorPreferences = new DistributorPreferences();
		try{
			
			objDistributorPreferences = (DistributorPreferences) crudSave(DistributorPreferences.class, objDistributorPreferences);
			System.out.println("objDistributorPreferences.id::"+objDistributorPreferences.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return objDistributorPreferences;
	}
	

}
