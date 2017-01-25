package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.DistributorPreferences;


/**
 * 
 * @author haider.ali
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class DistributorPreferencesRepositoryTest {

	@Inject
	private DistributorPreferencesRepository distributorPreferencesRepository;
	
	//@Test
	public void save(){
		DistributorPreferences s =  distributorPreferencesRepository.findOne(1L);
		s.setId(null);
		DistributorPreferences d = distributorPreferencesRepository.save(s);
		Assert.notNull(d);
	}
	
	
	@Test
	public void getDistributorPreferencesById(){
		
		DistributorPreferences s =  distributorPreferencesRepository.findOne(1L);
		Assert.notNull(s);
	}
	
	

	
}
