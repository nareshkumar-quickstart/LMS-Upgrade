package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CredentialCategoryRequirement;

public interface CredentialCategoryRequirementRepository extends CrudRepository<CredentialCategoryRequirement, Long> {
	List<CredentialCategoryRequirement> findByCredentialCategoryId(Long credentialCategoryId);
	List<CredentialCategoryRequirement> findByCredentialCategoryCredentialId(Long credentialId);
	
}
