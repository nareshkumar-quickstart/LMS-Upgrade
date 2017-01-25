package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CustomField;

public interface CustomFieldRepository extends CrudRepository<CustomField, Long>, CustomFieldRepositoryCustom {
	
	public List<CustomField> findByIdIn(List<Long> list);
	
}
