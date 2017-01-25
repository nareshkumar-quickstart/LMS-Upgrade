package com.softech.vu360.lms.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import com.softech.vu360.util.GUIDGeneratorUtil;

/**
 * 
 * @author marium.saud
 * This Unit Test contains insert/update/delete operations for VU360User.Following entities CRUD are also included
 * Learner
 * Instructor
 * 
 */
@Transactional
public class VU360UserTest extends TestBaseDAO<VU360User> {


	//@Test
	public void Learner_should_save_with_VU360User_Address_LearnerProfile() throws Exception{
		
		Learner learner=new Learner();
		
		Customer customer=(Customer)crudFindById(Customer.class, new Long(103));
		
		//==========Setting User
		VU360User newUser = new VU360User();

		newUser.setFirstName("Marium");
		// UI has not provided me the middlename
		newUser.setLastName("Saud");
		newUser.setEmailAddress("marium.saud@360training.com");
		newUser.setPassword("12345");
		newUser.setUsername("marium.saud");

		newUser.setAccountNonExpired(true);
		newUser.setAcceptedEULA(new Boolean(false));
		newUser.setNewUser(new Boolean(true));
		newUser.setAccountNonLocked(true);
		newUser.setChangePasswordOnLogin(false);
		newUser.setCredentialsNonExpired(true);
		newUser.setEnabled(true);
		newUser.setVissibleOnReport(true);

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		newUser.setExpirationDate(null);

		String guid = GUIDGeneratorUtil.generateGUID();
		newUser.setUserGUID(guid);
		Calendar calender = Calendar.getInstance();
		Date createdDate = calender.getTime();
		newUser.setCreatedDate(createdDate);

//		LMSRole role=vU360UserDAO.getDefaultRole(customer);
//		newUser.addLmsRole(role);

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

		//Setting user in learner
		learner.setVu360User(newUser);
		//Setting learnerProfile in learner
		learner.setLearnerProfile(newLearnerProfile);
		//Setting customer in learner
		learner.setCustomer(customer);
		//Setting learner to LearnerProfile
		newLearnerProfile.setLearner(learner);
		//Setting learner to user
		newUser.setLearner(learner);
		
		crudSave(Learner.class, learner);

	}
	
//	@Test
	public void Learner_should_update() throws Exception{
		
		Learner updateObj=(Learner)crudFindById(Learner.class, new Long(1057045));
		LearnerPreferences learnerPreference=new LearnerPreferences();
		learnerPreference.setBandwidth("high");
		learnerPreference.setLearner(updateObj);
		learnerPreference.setAudioEnabled(true);
		updateObj.setPreference(learnerPreference);
		crudSave(Learner.class, updateObj);
	}
	
	
}
