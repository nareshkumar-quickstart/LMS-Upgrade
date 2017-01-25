package com.softech.vu360.lms.service.lmsapi;

import com.softech.vu360.lms.model.lmsapi.LmsApiDistributor;

public interface LmsApiDistributorService {

	public LmsApiDistributor findLmsApiByDistributorId(long distributorId) throws Exception;
	public LmsApiDistributor findApiKey(String key) throws Exception;
	public LmsApiDistributor addLmsApiDistributor(LmsApiDistributor lmsApiDistributor) throws Exception;
	
}
