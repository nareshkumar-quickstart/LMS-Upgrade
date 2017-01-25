package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.PredictSurveyQuestionMapping;

public interface PredictSurveyQuestionMappingRepository extends CrudRepository<PredictSurveyQuestionMapping, Long> {

	PredictSurveyQuestionMapping findByLmsSurveyIdAndLmsQuestionId(Long surveyId, Long questionId);
	List<PredictSurveyQuestionMapping> findBySurveySectionIdAndQuestionIdAndLmsSurveyIdOrderByIdDesc(String sectionId, Long questionId, Long surveyId);
	PredictSurveyQuestionMapping findByLmsSurveyIdAndLmsSectionIdAndLmsFrameworkIdAndLmsQuestionId(Long surveyId, String sectionId, Long frameworkId, Long questionId);
	PredictSurveyQuestionMapping findFirstBySurveyIdAndLmsSurveyIdAndSurveySectionIdOrderByClonesAsc(Long predictSurveyId, Long lmsSurveyId, String predictSectionId);
	PredictSurveyQuestionMapping findByLmsSectionId(Long lmsSectionId);
	PredictSurveyQuestionMapping findByLmsFrameworkId(Long lmsFrameworkId);
	List<PredictSurveyQuestionMapping> findByLmsSurveyId(Long lmsSurveyId);
	List<PredictSurveyQuestionMapping> findBySurveySectionIdLikeAndQuestionIdAndLmsSurveyId(String sectionId,
			Long questionId, Long surveyId);
}
