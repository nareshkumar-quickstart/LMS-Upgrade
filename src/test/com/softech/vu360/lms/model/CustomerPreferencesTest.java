package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

/**
 * 
 * @author muhammad.rehan
 */

@Transactional
public class CustomerPreferencesTest extends TestBaseDAO<CustomerPreferences> {

	//@Test
	public void CustomerPreferences_should_find(){
		CustomerPreferences de =getById(new Long(1), CustomerPreferences.class);
		System.out.println(de.getBandwidth());
		System.out.println(de.getVolume());
	}
	
	
	//@Test
	public void CustomerPreferences_should_save() throws Exception {
		Customer d =(Customer)crudFindById(Customer.class, new Long(3));
		CustomerPreferences cp = new CustomerPreferences();
		cp.setAudioEnabled(true);
		cp.setAudioLocked(true);
		cp.setBandwidth("1l");
		cp.setBlankSearchEnabled(true);
		cp.setCaptioningEnabled(true);
		cp.setCaptioningLocked(true);
		cp.setCourseCompletionCertificateEmailEnabled(true);
		cp.setCustomer(d);
		cp.setEnableEnrollmentEmailsForNewCustomers(true);
		cp.setEnableSelfEnrollmentEmailsForNewCustomers(true);
		cp.setEnrollmentEmailLocked(true);
		cp.setRegistrationEmailLocked(true);
		cp.setVedioEnabled(true);
		cp.setVideoLocked(true);
		cp.setVolume(1);
		cp.setVolumeLocked(true);
		cp=(CustomerPreferences)crudSave(CustomerPreferences.class, cp);
	}
	
	//@Test
	public void CustomerPreferences_should_update() throws Exception {
		CustomerPreferences cp = (CustomerPreferences)crudFindById(CustomerPreferences.class, new Long(3050200));
		cp.setAudioEnabled(false);
		cp.setAudioLocked(false);
		cp.setBandwidth("1l11111");
		cp=(CustomerPreferences)crudSave(CustomerPreferences.class, cp);
	}
}
