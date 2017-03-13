/**
 * 
 */
package com.softech.vu360.lms.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.VU360UserNew;


public interface VU360UserNewRepository extends CrudRepository<VU360UserNew, Long>{

	@Query("select p from VU360UserNew p "+
			" join Learner l on l.vu360User.id = p.id "+
			" where l.id = ?1 ")
	public VU360UserNew getUserByLearnerId(Long learnerId);
	
}
