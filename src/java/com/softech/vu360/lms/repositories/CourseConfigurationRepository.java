package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CourseConfiguration;

public interface CourseConfigurationRepository extends CrudRepository<CourseConfiguration, Long> {
	List<CourseConfiguration> findByCourseConfigTemplateId(Long templateId);
}
