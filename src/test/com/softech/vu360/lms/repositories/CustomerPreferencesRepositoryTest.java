package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CustomerPreferences;
import com.softech.vu360.lms.repositories.CustomerPreferencesRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CustomerPreferencesRepositoryTest {

	@Inject
	private CustomerPreferencesRepository customerPreferencesRepository;
	
	@Test
	public void save() {
		CustomerPreferences customerPreferences = customerPreferencesRepository.findOne(new Long(120));
		customerPreferences.setId(null);
		CustomerPreferences cp = customerPreferencesRepository.save(customerPreferences);
		System.out.println("saved.........");
	}
	
}
