package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CourseConfigurationTemplate;

public interface CourseConfigurationTemplateRepository extends CrudRepository<CourseConfigurationTemplate, Long>, CourseConfigurationTemplateRepositoryCustom {
	
}
