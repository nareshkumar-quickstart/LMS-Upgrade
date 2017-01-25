package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyLearner;
import com.softech.vu360.lms.model.SurveyLearnerPK;
import com.softech.vu360.lms.model.SurveyOwner;


public interface SurveyLearnerRepository extends CrudRepository<SurveyLearner, SurveyLearnerPK>, SurveyLearnerRepositoryCustom{
	List<SurveyLearner> findByLearnerInAndSurveyIsLockedIsFalse(List<Learner> learners);
	List<SurveyLearner> findByLearnerAndSurvey(Learner learner, Survey survey);
	List<SurveyLearner> findByLearnerInAndSurveyIsLockedIsFalseAndSurveyNameLike(List<Learner> learners, String surveyName);
	List<SurveyLearner> findBySurveyOwnerAndSurveyIsInspectionIsTrue(SurveyOwner surveyOwner);
	List<SurveyLearner> findByLearnerInAndSurveyIn(List<Learner> learner, List<Survey> survey);
	
	
}
