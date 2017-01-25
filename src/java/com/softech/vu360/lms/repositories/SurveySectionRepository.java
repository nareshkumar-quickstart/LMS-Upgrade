/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.SurveySection;

/**
 * @author muhammad.junaid
 *
 */
public interface SurveySectionRepository extends CrudRepository<SurveySection, Long> {
	List<SurveySection> findBySurveyId(Long surveyId);
	List<SurveySection> findBySurveyIdAndParentIsNull(Long surveyId);
}
