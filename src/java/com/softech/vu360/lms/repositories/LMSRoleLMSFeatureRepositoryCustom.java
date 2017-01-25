package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.LMSRoleLMSFeature;

/**
 * 
 * @author raja.ali
 * @create date 15-July-2016
 *  
 */

public interface LMSRoleLMSFeatureRepositoryCustom {

	List<LMSRoleLMSFeature> findLMSRoleLMSFeatureByUser(Long loggedInUserId, Long customerId, String roleType);
	public boolean isAllowedLearnerDashboard(Long userId);
}
