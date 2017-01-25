/**
 * 
 */
package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.BrandDomain;

/**
 * @author marium.saud
 *
 */
public interface BrandDomainRepositoryCustom {
	
	public BrandDomain findBrandByDomain(String domain);

}
