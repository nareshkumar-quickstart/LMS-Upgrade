package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CredentialCategory;

public interface CredentialCategoryRepository extends CrudRepository<CredentialCategory, Long> {
	
	List<CredentialCategory> findByCategoryTypeAndName(String categoryType, String name);
	List<CredentialCategory> findByCredentialId(Long id);
}
