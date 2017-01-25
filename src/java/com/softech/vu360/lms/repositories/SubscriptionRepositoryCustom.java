package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

public interface SubscriptionRepositoryCustom {
	public List<Map<Object, Object>> findByIdByLearnerIdByUserIdByCourseName(Long learnerId, Long userId, Long subscriptionId,String coursesearch);
	public List<Map<Object, Object>> findByUserId(Long userId);
}
