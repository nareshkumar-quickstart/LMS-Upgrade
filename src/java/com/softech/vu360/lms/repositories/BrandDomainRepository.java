/**
 * 
 */
package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.BrandDomain;

/**
 * @author marium.saud
 *
 */
public interface BrandDomainRepository extends CrudRepository<BrandDomain, Long> , BrandDomainRepositoryCustom{
	
}
