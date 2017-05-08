package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.Repository;

import com.softech.vu360.lms.model.BrandDomain;

/**
 * @author marium.saud
 * updated by Muhammad.Junaid
 *
 */
public interface BrandDomainRepository extends Repository<BrandDomain, Long> {
	//SELECT a FROM BrandDomain a WHERE a.domain like domain%
	public BrandDomain findFirstByDomainStartingWith(String domain);
}
