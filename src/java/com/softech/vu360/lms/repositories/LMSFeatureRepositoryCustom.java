package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.LMSFeature;

/**
 * 
 * @author raja.ali
 * @create date 18-Oct-2016
 *  
 */

public interface LMSFeatureRepositoryCustom {

	List<LMSFeature> findAllActiveLMSFeaturesByUser(Long loggedInUserId, Long customerId, String roleType);
}
