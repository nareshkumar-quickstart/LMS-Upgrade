package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.ValidationQuestion;

/**
 * 
 * @author marium.saud
 *
 */
public interface ValidationQuestionRepository extends CrudRepository<ValidationQuestion, Long>{
	
	public List<ValidationQuestion> findByCourseConfigurationId(long id);
	
	public void deleteByIdIn(List<Long> lsValidationQuestionIds);

}
