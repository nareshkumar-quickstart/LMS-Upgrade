package com.softech.vu360.lms.service.impl.lmsapi;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.lmsapi.LmsApiDistributor;
import com.softech.vu360.lms.repositories.LmsApiDistributorRepository;
import com.softech.vu360.lms.service.lmsapi.LmsApiDistributorService;


public class LmsApiDistributorServiceImpl implements LmsApiDistributorService {
	
	private static final Logger log = Logger.getLogger(LmsApiDistributorServiceImpl.class.getName());
	
	@Inject
	private LmsApiDistributorRepository lmsApiDistributorRepository;

	@Override
	public LmsApiDistributor findLmsApiByDistributorId(long distributorId) throws Exception {
		LmsApiDistributor lmsApiDistributor = lmsApiDistributorRepository.findLmsApiByDistributorId(distributorId);
		return lmsApiDistributor;
	}

	@Override
	public LmsApiDistributor findApiKey(String key) throws Exception {
		LmsApiDistributor lmsApiDistributor = lmsApiDistributorRepository.findByApiKey(key);
		return lmsApiDistributor;
	}

	@Override
	public LmsApiDistributor addLmsApiDistributor(LmsApiDistributor lmsApiDistributor) throws Exception {
		LmsApiDistributor newLmsApiDistributor = lmsApiDistributorRepository.save(lmsApiDistributor);
		return newLmsApiDistributor;
	}

}
