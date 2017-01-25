/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.LearnerGroup;

/**
 * @author marium.saud
 *
 */
public interface LearnerGroupRepositoryCustom {
	
	List<LearnerGroup> findByLearnerIdOrderByLearnerGroupNameAsc(Long learnerId);

}
