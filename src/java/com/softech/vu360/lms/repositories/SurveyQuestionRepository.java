package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.SurveyQuestion;

public interface SurveyQuestionRepository extends CrudRepository<SurveyQuestion, Long>{
	
	public void deleteByIdIn(List<Long> ids);
	
	List<SurveyQuestion> findBySurveyId(long surveyId);
	
}
