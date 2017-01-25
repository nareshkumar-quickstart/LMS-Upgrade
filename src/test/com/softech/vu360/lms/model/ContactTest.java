package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class ContactTest extends TestBaseDAO<Contact> {


	@Before
	public void setRequiredService() {
	}
	
//	 @Test
		public void Contact_should_save_with_Address() throws Exception {
			Contact contact = new Contact();
			contact.setFirstName("TEST_FIRSTNAME");
			contact.setMiddleName("Khan");
			contact.setLastName("TEST_LASTNAME");
			Regulator record = (Regulator)crudFindById(Regulator.class, new Long(2));
			contact.setRegulator(record);
			
			Address newAddress = new Address();
			newAddress.setStreetAddress("Test_Street");
			newAddress.setStreetAddress2("Test_Street2");
			newAddress.setCity("Karachi");
			newAddress.setState("Sindh");
			newAddress.setZipcode("78906");
			newAddress.setCountry("Pakistan");
			contact.setAddress(newAddress);

			Address newAddress2 = new Address();
			newAddress2.setStreetAddress("Test_StreetAddress");
			newAddress2.setStreetAddress2("Test_Street2");
			newAddress2.setCity("karachi");
			newAddress2.setState("Sindh");
			newAddress2.setZipcode("75850");
			newAddress2.setCountry("Pakistan");
			contact.setAddress2(newAddress2);
			save(contact);
			
			
		}
		
		@Test
		public void Contact_should_update(){
			
			 Contact contact=getById(new Long(5360), Contact.class);
			 System.out.print(contact.getFirstName());
			contact.setTitle("Test_Contact_Updated");
			contact.setAddress(null);
			update(contact);
		}
}
