package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Contact;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class ContactRepositoryTest {

	@Inject
	private ContactRepository repoContact;
	
	//@Test
	public void contact_should_save() {
		Contact saveContact = repoContact.findOne(new Long(2105));
		saveContact.setId(null);
		
		Contact savedContact = repoContact.save(saveContact);
		
		System.out.println("..........");
	}
	
	//@Test
	public void contact_should_find() {
		Contact saveContact = repoContact.findOne(new Long(2));
		
		System.out.println("..........");
	}
	
	//@Test
	public void contact_should_deleteAll() {
		List<Contact> objContacts = new ArrayList<Contact>();
		
		objContacts.add(repoContact.findOne(new Long(267950)));
		objContacts.add(repoContact.findOne(new Long(5360)));
		
		repoContact.delete(objContacts);
		
		System.out.println("..........");
	}

	//@Test
	public void contacts_should_find_by_firstName_lastName_emailAddress_phone_regulator() {
		String firstName = "";
		String lastName = "";
		String emailAddress = "";
		String phone = "";
		Long regulatorId = 2L;
		
		List<Contact> lstContact = repoContact.findContactByRegulator(firstName, lastName, emailAddress, phone, regulatorId);
		
		System.out.println("..........");
	}
	
	@Test
	public void contacts_should_find_by_regulator() {

		Long regulatorId = 2L;
		
		List<Contact> lstContact = repoContact.findByRegulatorId(regulatorId);
		
		System.out.println("..........");
	}
}
