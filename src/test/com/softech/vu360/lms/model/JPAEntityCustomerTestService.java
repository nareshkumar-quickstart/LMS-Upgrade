package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Test;

import com.softech.vu360.util.GUIDGeneratorUtil;

@Transactional
public class JPAEntityCustomerTestService  extends TestBaseDAO<Object>{
	
	@Test
	public void testCustomer() throws Exception {
		
		Customer customer = new Customer();
		Brand brand = null;
		String guid=GUIDGeneratorUtil.generateGUID();
		customer.setCustomerGUID(guid);
		customer.setName("Haide");
		customer.setFirstName("haider");
		customer.setLastName("haider");
		customer.setWebsiteUrl("ali");
		customer.setEmail("haider@asdf.com");
		customer.setPhoneNumber("43434");
		customer.setPhoneExtn("asdfa");
		customer.setCustomerType("dasd");
		customer.setActive(true);
		customer.setLmsApiEnabledTF(false);
		
		brand= (Brand) crudFindById(Brand.class, 2L);
		customer.setBrand(brand);
		customer.setBrandName(brand==null?"":brand.getBrandKey());				
		
		Address address1 = new Address();
		address1.setStreetAddress("asd");
		address1.setStreetAddress2("asd");
		address1.setCity("asd");
		address1.setCountry("asd");
		address1.setState("asd");
		address1.setZipcode("asd");
		customer.setBillingAddress(address1);

		Address address2 = new Address();
		address2.setStreetAddress("asd");
		address2.setStreetAddress2("asd");
		address2.setCity("asd");
		address2.setCountry("asd");
		address2.setState("asd");
		address2.setZipcode("asd");

		customer.setShippingAddress(address2);

		CustomerPreferences customerPreferences = new CustomerPreferences();
		customerPreferences.setAudioEnabled(false);
		customerPreferences.setAudioLocked(false);
		customerPreferences.setVolumeLocked(false);
		customerPreferences.setCaptioningEnabled(false);
		customerPreferences.setCaptioningLocked(false);
		customerPreferences.setVolume(1);
		customerPreferences.setBandwidth(CustomerPreferences.BANDWIDTH_LOW);

		customerPreferences.setBandwidthLocked(false);
		customerPreferences.setVedioEnabled(false);
		customerPreferences.setVideoLocked(false);
		customerPreferences.setEnableRegistrationEmailsForNewCustomers(false);
		customerPreferences.setRegistrationEmailLocked(false);
		customerPreferences.setEnableEnrollmentEmailsForNewCustomers(false);
		customerPreferences.setEnrollmentEmailLocked(false);
		customerPreferences.setCourseCompletionCertificateEmailEnabled(false);
		customerPreferences.setCourseCompletionCertificateEmailLocked(false);

		customer.setCustomerPreferences(customerPreferences);
		customerPreferences.setCustomer(customer);

		// make no assumptions on how we got this one
		Distributor dist= (Distributor) crudFindById(Distributor.class, 2L);
		customer.setDistributor(dist);
		
		customer.setCustomerType("b2c");
		customer = (Customer) crudSave(Customer.class, customer);

		
		Customer cc = (Customer) crudFindById(Customer.class, customer.getId());
		cc.setActive(false);
		cc.getBillingAddress().setCountry("pppp");
		update(cc);
		
	}
	
	
	
}