package com.softech.vu360.lms.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.SurveyFlag;
import com.softech.vu360.lms.model.SurveyOwner;
import com.softech.vu360.lms.model.SurveyResultAnswer;
import com.softech.vu360.lms.model.VU360User;

public interface SurveyFlagRepository extends CrudRepository<SurveyFlag, Long>	{

	SurveyFlag findOneByAnswerline(SurveyResultAnswer surveyAnswer);
	List<SurveyFlag> findByReviewedIsTrueAndFlagTemplateSurveyOwner(SurveyOwner surveyOwner);
	List<SurveyFlag> findBySentBackToLearnerIsTrueAndFlagTemplateSurveyOwner(SurveyOwner surveyOwner);
	List<SurveyFlag> findByReviewedIsFalseAndSentBackToLearnerIsFalseAndFlagTemplateSurveyOwner(SurveyOwner surveyOwner);
	List<SurveyFlag> findByFlagTemplateSurveyOwner(SurveyOwner surveyOwner);
	List<SurveyFlag> findByResponseProviderAndSentBackToLearnerIsTrue(VU360User user);
}
