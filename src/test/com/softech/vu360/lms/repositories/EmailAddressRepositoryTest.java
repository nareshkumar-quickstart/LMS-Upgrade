package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.EmailAddress;

public class EmailAddressRepositoryTest extends SpringJUnitConfigAbstractTest {

	@Inject
	private EmailAddressRepository emailAddressRepository;
	
	@Test
	public void findByEmailLikeAndIdIn() {
		try {
			List<EmailAddress> emailAddresses = emailAddressRepository.getEmailAddressUnderAlertRecipient(9010L, "sara@gmail.com");
			System.out.println(emailAddresses);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
