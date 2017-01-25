package com.softech.vu360.lms.repositories;


import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LearnerPreferences;

import com.softech.vu360.lms.repositories.LearnerRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LearnerPreferencesRepositoryTest{

	@Inject
	private LearnerPreferencesRepository learnerPreferencesRepository;
	@Inject
	private LearnerRepository learnerrepository;

	@Test
	public void LearnerPreferences_should_save() {
		LearnerPreferences learnerPreferences = new LearnerPreferences();
		learnerPreferences.setLearner(learnerrepository.findOne(1L));
		try{
		learnerPreferencesRepository.save(learnerPreferences);
		}catch(Exception ex){
		System.out.println(ex);	
		}

		
	}

	
}
