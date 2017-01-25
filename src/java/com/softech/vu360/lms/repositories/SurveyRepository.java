package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyOwner;
import com.softech.vu360.lms.repositories.impl.SurveyRepositoryCustom;

public interface SurveyRepository extends CrudRepository<Survey, Long>,SurveyRepositoryCustom {

	Survey findFirstByName(String name);
	List<Survey> findByIdIn(List<Long> Ids);
	void deleteByQuestionsSurveyAnswers(List<SurveyAnswerItem> surveyAnswerItem);
	List<Survey> findByOwnerAndIsLockedFalse(SurveyOwner owner);
	List<Survey> findByCoursesIdInAndIsLockedIsFalseAndStatusAndOwnerIdOrOwnerIdAndNameLike(
			Long[] courseIdArray, String published, Long customerId, Long distributorId, String surveyNameIsLike);
	List<Survey> findByCoursesIdInAndIsLockedIsFalseAndStatusAndOwnerIdOrOwnerId(
			Long[] courseIdArray, String published, Long customerId, Long distributorId);
	Survey findByOwnerId(Long ownerId);
	List<Survey> findByIsLockedIsFalse();
	Survey findByOwner(SurveyOwner owner);
}
