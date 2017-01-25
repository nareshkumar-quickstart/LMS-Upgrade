package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.SubscriptionKit;
import com.softech.vu360.lms.model.SubscriptionKitCourses;

public interface SubscriptionKitCoursesRepository extends CrudRepository<SubscriptionKitCourses, Long> {

	List<SubscriptionKitCourses> findBySubscriptionKitId(Long subscriptionKit);
	
	
}
