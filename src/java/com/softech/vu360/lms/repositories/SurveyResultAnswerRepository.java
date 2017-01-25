package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.SurveyResultAnswer;

public interface SurveyResultAnswerRepository extends CrudRepository<SurveyResultAnswer, Long> {

	SurveyResultAnswer findById(Long Id);
	SurveyResultAnswer findFirstByQuestionId(Long Id);
	List<SurveyResultAnswer> findByQuestionId(Long Id);
	SurveyResultAnswer findByQuestionIdAndSurveyQuestionBankIdAndSurveySectionIdAndSurveyResultSurveyeeId(
		Long surveyQuestionId, Long surveyQuestionBrankId, Long surveySectionId, Long vu360userId);
	SurveyResultAnswer findByQuestionIdAndSurveyResultId(long questionId, long surveyResultId);
	SurveyResultAnswer findByQuestionIdAndSurveyResultSurveyeeId(Long questionId, Long userId);
}
