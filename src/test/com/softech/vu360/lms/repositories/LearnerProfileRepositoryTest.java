package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.repositories.LearnerRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LearnerProfileRepositoryTest{

	@Inject
	private LearnerProfileRepository learnerProfileRepository;
	@Inject
	private TimeZoneRepository timeZoneRepository;
	@Inject
	private LearnerRepository learnerrepository;

	
	//@Test
	public void LearnerProfile_should_save() {
		LearnerProfile newLearnerProfile = new LearnerProfile();

		Address newAddress = new Address();
		newAddress.setStreetAddress("Test_Street");
		newAddress.setStreetAddress2("Test_Street2");
		newAddress.setCity("Karachi");
		newAddress.setState("Sindh");
		newAddress.setZipcode("78906");
		newAddress.setCountry("Pakistan");
		newLearnerProfile.setLearnerAddress(newAddress);

		Address newAddress2 = new Address();
		newAddress2.setStreetAddress("Test_StreetAddress");
		newAddress2.setStreetAddress2("Test_Street2");
		newAddress2.setCity("karachi");
		newAddress2.setState("Sindh");
		newAddress2.setZipcode("75850");
		newAddress2.setCountry("Pakistan");
		newLearnerProfile.setLearnerAddress2(newAddress2);

		newLearnerProfile.setMobilePhone("03002312342342");
		
		TimeZone timeZone=timeZoneRepository.findOne(12);
		newLearnerProfile.setTimeZone(timeZone);
		
		Learner learner=learnerrepository.findOne(1L);
		newLearnerProfile.setLearner(learner);
		
		newLearnerProfile = learnerProfileRepository.save(newLearnerProfile);
		System.out.println("LearnerProfile : ID = "+newLearnerProfile.getId());
	}
	
	@Test
	public void LearnerProfile_should_getByIdAndUpdate() {
		LearnerProfile learenrProfile=learnerProfileRepository.findOne(3L);
		TimeZone timeZone=timeZoneRepository.findOne(15);
		learenrProfile.setTimeZone(timeZone);
		learenrProfile=learnerProfileRepository.save(learenrProfile);
		System.out.println("LearnerProfile : Updated Time Zone = "+learenrProfile.getTimeZone().getCode());
	}
	
}
