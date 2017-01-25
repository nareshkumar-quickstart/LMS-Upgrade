package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.SubscriptionCourse;

/**
 * 
 * @author ramiz.uddin
 *
 */

public class SubscriptionCourseRepositoryTest extends
		SpringJUnitConfigAbstractTest {

	@Inject
	private SubscriptionCourseRepository subscriptionCourseRepository;

	// @Test
	public void findBySubscriptionSubscriptionCode() {

		List<SubscriptionCourse> subsList = subscriptionCourseRepository
				.findBySubscriptionSubscriptionCode("002");
		if (subsList != null && subsList.size() > 0) {
			for (SubscriptionCourse course : subsList) {
				System.out.println(course.getCourse().getName());
			}
		}
	}

	// @Test
	public void findBySubscriptionId() {
		List<SubscriptionCourse> subsList = subscriptionCourseRepository
				.findBySubscriptionId(2861l);
		if (subsList != null && subsList.size() > 0) {
			for (SubscriptionCourse course : subsList) {
				System.out.println(course.getCourse().getName());
			}
		}
	}

	// @Test
	public void findBySubscriptionCustomerEntitlementId() {
		List<SubscriptionCourse> subsList = subscriptionCourseRepository
				.findBySubscriptionCustomerEntitlementId(23150l);
		if (subsList != null && subsList.size() > 0) {
			for (SubscriptionCourse course : subsList) {
				System.out.println(course.getCourse().getName());
			}
		}
	}

	//@Test
	public void getUserSubscriptionCourses() {
		List<Object[]> subList = subscriptionCourseRepository
				.getUserSubscriptionCourses(1, 1, 1, "Einstein");
		if (subList != null && subList.size() > 0) {
			for (Object obj : subList) {

			}
		}

	}

	//@Test
	public void getUserSubscriptionCoursesWithoutCourseParam() {
		Map<Object,Object> subList = subscriptionCourseRepository
				.getUserSubscriptionCourses(1, 1, 1);
		if (subList != null && subList.size() > 0) {
			for (Object obj : subList.values()) {
				
			}
		}

	}
	
	//@Test
	public void getUserSubscriptions(){
		Map<Object,Object> subList = subscriptionCourseRepository
				.getUserSubscriptions(1);
		if (subList != null && subList.size() > 0) {
			for (Object obj : subList.values()) {

			}
		}
	}
	
	//@Test
	public void saveSubscriptionCoursesProc(){
		/*Warning! - Please be careful with this method
		 * may result in anomally*/
		boolean result = subscriptionCourseRepository.saveSubscriptionCourses(4911L, 73L);
		System.out.println(result);
		
	}
	
}
