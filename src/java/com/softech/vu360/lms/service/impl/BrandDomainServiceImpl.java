package com.softech.vu360.lms.service.impl;

import javax.inject.Inject;

import com.softech.vu360.lms.model.BrandDomain;
import com.softech.vu360.lms.repositories.BrandDomainRepository;
import com.softech.vu360.lms.service.BrandDomainService;



public class BrandDomainServiceImpl implements BrandDomainService 
{
	@Inject
	private BrandDomainRepository brandDomainRepository;

	@Override
	public BrandDomain findBrandByDomain(String domain) {
			return brandDomainRepository.findFirstByDomainStartingWith(domain);
	}

}
