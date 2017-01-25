package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author marium.saud
 * The test case has been done for following entites
 * Distributor
 * DistributorPreferences
 */
@Transactional
public class DistributorTest extends
		TestBaseDAO<Distributor> {

	@Before
	public void setRequiredService() {

	}
	
//	@Test
	public void Distributor_should_save_with_DistributorPreferences_Address(){
		
		Distributor distributor = new Distributor();

		distributor.setName("Test_Distributor");
		distributor.setDistributorCode("Test_Distributor_1");
		distributor.setFirstName("360Training");
		distributor.setLastName("Company");
		distributor.setWebsiteUrl("www.360training.com");
		distributor.setDistributorEmail("test@gmail.com");
		distributor.setOfficePhone("12324234");
		distributor.setOfficePhoneExtn("1234");
		distributor.setActive(true);				
		Brand brand = (Brand)crudFindById(Brand.class, new Long(1));
		distributor.setBrand(brand);
		distributor.setBrandName("Test_Brand");
		distributor.setSelfReporting(true); //added from regulatory branch
		distributor.setCorporateAuthorVar(true);
		distributor.setCallLoggingEnabled(true);
		distributor.setMarkedPrivate(false);
		distributor.setType("Test_Type");
		distributor.setLmsApiEnabledTF(true);

		Address address1 = new Address();
		address1.setStreetAddress("Test_StreetAdress_1");
		address1.setStreetAddress2("Test_StreetAdress_2");
		address1.setCity("Karachi");
		address1.setCountry("Pakistan");
		address1.setState("Sindh");
		address1.setZipcode("75600");
		distributor.setDistributorAddress(address1);

		Address address2 = new Address();
		address2.setStreetAddress("Test_StreetAdress_3");
		address2.setStreetAddress2("Test_StreetAdress_4");
		address2.setCity("Lahore");
		address2.setCountry("Pakistan");
		address2.setState("Punjab");
		address2.setZipcode("45767");
		distributor.setDistributorAddress2(address2);

		DistributorPreferences distributorPreferences = new DistributorPreferences();
		distributorPreferences.setAudioEnabled(true);
		distributorPreferences.setAudioLocked(false);
		distributorPreferences.setVolume(5);
		distributorPreferences.setVolumeLocked(false);
		distributorPreferences.setCaptioningEnabled(true);
		distributorPreferences.setCaptioningLocked(false);
		distributorPreferences.setBandwidth(DistributorPreferences.BANDWIDTH_LOW);
		distributorPreferences.setBandwidthLocked(false);
		distributorPreferences.setVedioEnabled(true);
		distributorPreferences.setVideoLocked(false);
		distributor.setDistributorPreferences(distributorPreferences);
		distributorPreferences.setDistributor(distributor);

		try{
			persist(distributor);	
		}
		catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	@Test
	public void Distributor_should_update(){
		
		Distributor distributor=getById(new Long(30506), Distributor.class);
		ContentOwner owner=(ContentOwner)crudFindById(ContentOwner.class, new Long(1));
		distributor.setContentOwner(owner);
		Customer customer=(Customer)crudFindById(Customer.class, new Long(1));
		distributor.setMyCustomer(customer);
		CustomField field=(CustomField)crudFindById(CustomField.class, new Long(2));
		distributor.getCustomFields().add(field);
		update(distributor);
	}


}
