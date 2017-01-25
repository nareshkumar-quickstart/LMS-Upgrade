package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.SurveyFlagTemplate;

public interface SurveyFlagTemplateRepository extends CrudRepository<SurveyFlagTemplate, Long>	{

	List<SurveyFlagTemplate> findBySurveyId(Long surveyId);
	List<SurveyFlagTemplate> findByFlagNameIgnoreCaseLikeAndSurveyId(String flagName, Long surveryId);
	void deleteByIdIn(Long[] ids);

}
