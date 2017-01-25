package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.model.VU360User;

public interface SurveyResultRepository extends CrudRepository<SurveyResult, Long>	{

	SurveyResult findOneBySurveyeeAndCourseAndSurvey(VU360User surveyee, Course course, Survey survey);
	SurveyResult findOneBySurveyeeAndSurvey(VU360User surveyee, Survey survey);
	SurveyResult findOneBySurveyeeAndCourseIsNullAndSurvey(VU360User surveyee, Survey survey);
	SurveyResult findOneBySurveyeeAndCourseIsNullAndSurveyAndSurveyNameIsLike(VU360User surveyee, Survey survey, String surveyName);
	List<SurveyResult> findBySurveyId(long surveyId);
	List<SurveyResult> findBySurveyee(VU360User vu360user);
	@Query(value="select * from surveyresult sr, learningsession ls, learnerenrollment le where le.id = :learnerEnrollmentId and ls.enrollment_id = le.id and sr.learningsession_id = ls.id", nativeQuery = true)
	List<SurveyResult> findByLearningSessionLearnerEnrollmentId(Long learnerEnrollmentId);
	SurveyResult findById(Long id);
	Integer countBySurveyId(Long surveyId);
	@Query(value="SELECT COUNT(DISTINCT(t0.id)) FROM SurveyResultAnswer t6, SURVEYQUESTION t5, SurveyResultAnswer t4, SURVEYSECTION t3, SURVEY t2, SurveyResult t1, VU360USER t0 WHERE ((((t2.id = ?1) AND (t3.id = ?2)) AND (t5.id = ?3)) AND ((((((t2.ID = t1.Survey_ID) AND (t4.SurveyResult_ID = t1.ID)) AND (t3.ID = t4.surveySectionId)) AND (t6.SurveyResult_ID = t1.ID)) AND (t5.ID = t6.SURVEYQUESTION_ID)) AND (t0.ID = t1.VU360User_ID)))", nativeQuery = true)
	Integer countByAnsweredQuestion(Long surveyId, Long sectionId, Long questionId);
	@Query(value="SELECT COUNT(t0.id) FROM SURVEYSECTION t4, SurveyResult t3, SURVEY t2, SurveyResultAnswer t1, SURVEYQUESTION t0 WHERE ((((t2.id = ?1) AND (t4.id = ?2)) AND (t0.id = ?3)) AND ((((t3.ID = t1.SurveyResult_ID) AND (t2.ID = t3.Survey_ID)) AND (t4.ID = t1.surveySectionId)) AND (t0.ID = t1.SURVEYQUESTION_ID)))", nativeQuery = true)
	Integer countByQuestionsAnswered(Long surveyId, Long sectionId, Long questionId);
	@Query(value="SELECT COUNT(t0.id) FROM SURVEYANSWER_SURVEYRESULTANSWER t6, SURVEYQUESTION t5, SURVEYSECTION t4, SurveyResult t3, SURVEY t2, SurveyResultAnswer t1, SURVEYANSWER t0 WHERE (((((t2.id = ?1) AND (t4.id = ?2)) AND (t5.id = ?3)) AND (t0.id = ?4)) AND (((((t3.ID = t1.SurveyResult_ID) AND (t2.ID = t3.Survey_ID)) AND (t4.ID = t1.surveySectionId)) AND (t5.ID = t1.SURVEYQUESTION_ID)) AND ((t6.SURVEYRESULTANSWER_ID = t1.ID) AND (t0.ID = t6.SURVEYANSWER_ID))))", nativeQuery = true)
	Integer countByAnswer(Long surveyId, Long sectionId, Long questionId, Long answerId);
	SurveyResult findOneBySurveyeeAndCourseAndSurveyAndSurveyNameLike(VU360User loggedInUser, Course course, Survey survey, String search);
}
