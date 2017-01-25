/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LearnerValidationAnswers;

/**
 * @author marium.saud
 *
 */
public interface LearnerValidationAnswersRepository extends CrudRepository<LearnerValidationAnswers, Long> {

	public List<LearnerValidationAnswers> findByLearnerId(long id);

}
