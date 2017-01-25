package com.softech.vu360.lms.service.impl;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.BrandDomain;
import com.softech.vu360.lms.repositories.BrandDomainRepository;
import com.softech.vu360.lms.service.BrandDomainService;



public class BrandDomainServiceImpl implements BrandDomainService 
{
	private static final Logger log = Logger.getLogger(BrandDomainServiceImpl.class.getName());
	
	@Inject
	private BrandDomainRepository brandDomainRepository;

	@Override
	public BrandDomain findBrandByDomain(String domain) {
			return brandDomainRepository.findBrandByDomain(domain);
	}

}
