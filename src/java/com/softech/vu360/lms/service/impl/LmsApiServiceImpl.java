/**
 * 
 */
package com.softech.vu360.lms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.softech.vu360.lms.model.LmsApi;
import com.softech.vu360.lms.repositories.LmsApiRepository;
import com.softech.vu360.lms.service.LmsApiService;

/**
 * @author basit.ahmed
 *
 */
public class LmsApiServiceImpl implements LmsApiService {
	
	@Autowired
	private LmsApiRepository lmsApiRepository = null;


	@Override
	public LmsApi findLmsApiByCustomerId(long customerId) throws Exception {
		LmsApi lmsApi = lmsApiRepository.findByCustomerId(customerId);
		return lmsApi;
	}

}
