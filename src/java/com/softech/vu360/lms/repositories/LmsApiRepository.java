package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.LmsApi;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for {@code LmsApi}s.
 * 
 * @author muhammad.rehan
 * 
 */
public interface LmsApiRepository extends CrudRepository<LmsApi, Long> {
	@Query("SELECT la FROM  #{#entityName} la join fetch la.customer c WHERE c.id =:CID")
	LmsApi findByCustomerId(@Param("CID") Long customerId);
}
