/**
 * 
 */
package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import com.softech.vu360.lms.model.BusinessObjectSequence;

/**
 * @author marium.saud
 *
 */
public interface BusinessObjectSequenceRepository extends CrudRepository<BusinessObjectSequence, Long>,BusinessObjectSequenceRepositoryCustom {
	
	

}
