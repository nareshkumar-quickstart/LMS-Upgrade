package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.util.CustomFieldEntityType;

public interface CustomFieldRepositoryCustom {
	public List<CustomField> findCustomFieldByCustomFieldEntityTypeAndFieldTypeAndName(
			CustomFieldEntityType customFieldEntityType, String fieldType, String name);

	
	public void deleteCustomFields(Long[] s);
	
	public boolean isUniqueCustomFieldName(String entityType, String name);
}
