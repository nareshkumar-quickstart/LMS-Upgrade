/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.LearnerValidationAnswers;

/**
 * @author marium.saud
 *
 */
public interface LearnerValidationAnswersRepository extends CrudRepository<LearnerValidationAnswers, Long> {
	
	
	public List<LearnerValidationAnswers> findByLearnerId(long id);
	
	@Query(value = "Select a.* from learnervalidationanswers a, VALIDATIONQUESTION q where a.LEARNER_ID = :learnerId and q.id = a.question_id and q.COURSECONFIGURATION_ID is NULL", nativeQuery = true)
	public List<LearnerValidationAnswers> getLearnerValidationQuestions(@Param("learnerId") Long learnerId);
	
	@Query(value= "select a.* from learnervalidationanswers a, VALIDATIONQUESTION q where a.LEARNER_ID = :learnerId and q.id = a.question_id and q.COURSECONFIGURATION_ID = :courseconfigurationId", nativeQuery = true)	
	public List<LearnerValidationAnswers> getLearnerUniqueValidationQuestionsAnswers(@Param("learnerId") Long learnerId, @Param("courseconfigurationId") Long courseconfigurationId);
	
	public  LearnerValidationAnswers findByQuestionId(long questionId);

}
