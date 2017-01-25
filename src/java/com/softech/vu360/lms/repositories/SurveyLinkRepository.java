package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.SurveyLink;

public interface SurveyLinkRepository extends CrudRepository<SurveyLink, Long> {

	List<SurveyLink> findBySurveyId(long surveyId);

}
