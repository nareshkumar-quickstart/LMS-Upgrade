package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;


/**
 * 
 * @author haider.ali
 *
 */
public interface CustomFieldValueRepository extends CrudRepository<CustomFieldValue, Long> {
	
	public List<CustomFieldValue> findByCustomField(CustomField customField);
	public List<CustomFieldValue> findByCustomFieldIdIn(Long[] customFieldIds);
	public Long countByCustomField(CustomField customField);
	
}
