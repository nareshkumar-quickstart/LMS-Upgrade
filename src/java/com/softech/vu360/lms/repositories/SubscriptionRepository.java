package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Subscription;

public interface SubscriptionRepository extends	SubscriptionRepositoryCustom, CrudRepository<Subscription, Long> {

	List<Subscription> findAllBySubscriptionCode(String subscriptionCode);

	Subscription findBySubscriptionCode(String subscriptionCode);

	@Query("Select p from Subscription p Where p.customerEntitlement.customer.id = :customerId AND p.subscriptionName LIKE %:subscriptionName% and p.subscriptionStatus ='ACTIVE' ORDER BY p.id ASC")
	public List<Subscription> searchSubscriptions(
			@Param("subscriptionName") String subscriptionName,
			@Param("customerId") long customerId);

	@Query("Select p from Subscription p Where p.customerEntitlement.id = :id AND p.subscriptionStatus='ACTIVE'")
	public List<Subscription> findSubscriptionsByEntitlementId(@Param("id") long id);

	@Query("Select p from Subscription p JOIN p.subscriptionCourses c WHERE p.id = :id AND c.course.courseTitle LIKE %:courseName%")
	public Subscription searchsubscriptioncourse(@Param("id") Long id,
			@Param("courseName") String courseName);

}
