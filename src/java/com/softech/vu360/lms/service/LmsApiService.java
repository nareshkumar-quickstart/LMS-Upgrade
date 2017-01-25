/**
 * 
 */
package com.softech.vu360.lms.service;

import com.softech.vu360.lms.model.LmsApi;

/**
 * @author basit.ahmed
 *
 */
public interface LmsApiService {
	
	public LmsApi findLmsApiByCustomerId(long customerId) throws Exception;
	
}
