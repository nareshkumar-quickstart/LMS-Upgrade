package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.CourseConfigurationTemplate;

public interface CourseConfigurationTemplateRepositoryCustom {
	
	List<CourseConfigurationTemplate> findByNameAndLastUpdateDate(String name, Date lastUpdatedDate);
	List<CourseConfigurationTemplate> findByNameAndLastUpdateDateAndContentOwnerId(String name, Date lastUpdatedDate, Long contentOwnerId);
	public Boolean findviewAssessmentResults(Long learnerEnrollmentId);
}
