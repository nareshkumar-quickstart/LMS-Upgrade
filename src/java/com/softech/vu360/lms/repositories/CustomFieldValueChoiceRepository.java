package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValueChoice;


/**
 * 
 * @author haider.ali
 *
 */
public interface CustomFieldValueChoiceRepository extends CrudRepository<CustomFieldValueChoice, Long> {
	
	public List<CustomFieldValueChoice> findByCustomFieldOrderByDisplayOrderAsc(CustomField customField);
}
