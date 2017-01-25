package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CredentialCategoryRequirementRepositoryTest {

	@Inject
	private CredentialCategoryRequirementRepository repoCredentialCategoryRequirement;
	
	@Test
	public void courseApprovals_should_Find_By_credentialId(){
		Long credentialId = 3L;
		List<CredentialCategoryRequirement> cas = repoCredentialCategoryRequirement.findByCredentialCategoryCredentialId(credentialId);
		System.out.println("..................");
	}
	

}
