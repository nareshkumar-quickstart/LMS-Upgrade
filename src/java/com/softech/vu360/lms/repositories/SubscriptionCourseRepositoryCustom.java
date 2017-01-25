package com.softech.vu360.lms.repositories;

public interface SubscriptionCourseRepositoryCustom {
	boolean saveSubscriptionCourses(
			long subscriptionId,
			long subscriptionKitid);
}
