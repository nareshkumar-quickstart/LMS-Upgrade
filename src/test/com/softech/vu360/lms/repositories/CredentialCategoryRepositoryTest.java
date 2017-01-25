package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CredentialCategory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CredentialCategoryRepositoryTest {

	@Inject
	private CredentialCategoryRepository credentialCategoryRepository;
	
	//@Test
	public void courses_should_find_by_contentOwners_And_courseTitle_And_courseStatus_And_retired_And_courseId() {
		
		String categoryType = "Pre-License";
		String name = "Pre-License";
		
		List<CredentialCategory> lstCredentialCategory = credentialCategoryRepository.findByCategoryTypeAndName(categoryType, name);
				
		
		System.out.println("..........");
	}
	
	@Test
	public void credentialCategory_should_find_by_credentialId() {
		
		Long id = 1L;
		
		List<CredentialCategory> lstCredentialCategory = credentialCategoryRepository.findByCredentialId(id);
		
		System.out.println("..........");
	}

}
