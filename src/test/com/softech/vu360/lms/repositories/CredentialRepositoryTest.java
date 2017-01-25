package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Credential;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CredentialRepositoryTest {

	@Inject
	private CredentialRepository credentialRepository;
	
	//@Test
	public void credential_should_save() {
		Credential saveCredential = credentialRepository.findOne(new Long(2105));
		saveCredential.setId(null);
		
		Credential savedCredential = credentialRepository.save(saveCredential);
		
		System.out.println("..........");
	}
	
	//@Test
	public void credential_should_find() {
		Credential saveCredential = credentialRepository.findOne(new Long(2105));
		
		System.out.println("..........");
	}
	
	//@Test
	public void credential_should_deleteAll() {
		List<Credential> objCredentials = new ArrayList<Credential>();
		credentialRepository.delete(objCredentials);
		
		System.out.println("..........");
	}

	
	//@Test
	public void courses_should_find_by_contentOwners_And_courseTitle_And_courseStatus_And_retired_And_courseId() {
		
		Collection<Long> ids= null;
		ids.add(1L);
		ids.add(53L);
		
		String officialLicenseName = "License-1";
		String shortLicenseName = "L1";
		Boolean active = false;

		
		
		List<Credential> lstCredential = credentialRepository.findCredentialByContentOwner(ids, officialLicenseName, shortLicenseName, active);
		
		System.out.println("..........");
	}
	
	@Test
	public void credentials_should_find_by_contentOwners_And_officialLicenseName_And_credentialShortName() {
		
		Long[] cos = new Long[]{1L, 53L};
		
		String officialLicenseName = "License-1";
		String shortLicenseName = "L1";
		Boolean active = false;

		
		
		List<Credential> lstCredential = credentialRepository.findByCredentialNameAndCredentialIds(Arrays.asList(cos), officialLicenseName, shortLicenseName);
		
		System.out.println("..........");
	}
}
