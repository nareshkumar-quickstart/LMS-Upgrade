package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.SubscriptionKit;

public interface SubscriptionKitRepository extends
		CrudRepository<SubscriptionKit, Long> {

	public SubscriptionKit findByGuid(String guid);	
}
