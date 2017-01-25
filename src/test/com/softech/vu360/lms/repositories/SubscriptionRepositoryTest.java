package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


import org.junit.Test;

import com.softech.vu360.lms.model.Subscription;
import com.softech.vu360.lms.model.SubscriptionKitCourses;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.SubscriptionService;
import com.softech.vu360.lms.vo.SubscriptionVO;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;

public class SubscriptionRepositoryTest extends SpringJUnitConfigAbstractTest {

	@Inject
	SubscriptionRepository subscriptionRepository;

	@Inject
	VU360UserRepository vU360UserRepository;
	
	@Inject
	SubscriptionKitCoursesRepository subscriptionKitCoursesRepository;
	
	@Inject
	SubscriptionService subscriptionService;
	// @Test
	public void findOne() {
		Subscription subscription = subscriptionRepository.findOne(2l);
		if (subscription != null) {
			System.out.println(subscription.getSubscriptionName());
		}
	}

	// @Test
	public void findAllBySubscriptionCode() {
		List<Subscription> subList = subscriptionRepository
				.findAllBySubscriptionCode("002");
		if (subList != null && subList.size() > 0) {
			for (Subscription subscription : subList) {
				System.out.println(subscription.getSubscriptionName());
			}
		}
	}

	//@Test
	public void findBySubscriptionCode() {
		Subscription subscription = subscriptionRepository
				.findBySubscriptionCode("003");
		if (subscription != null) {
			System.out.println(subscription.getSubscriptionName());
		}
	}

	// @Test
	public void save() {
		/* Warning! please use with caution or data anomaly will occur */
		Subscription subscription = subscriptionRepository.findOne(33l);
		Subscription clone = new Subscription();
		clone.setCustomerEntitlement(subscription.getCustomerEntitlement());
		clone.setCustomerType(subscription.getCustomerType());
		clone.setSubscriptionCourses(subscription.getSubscriptionCourses());
		clone.setSubscriptionKit(subscription.getSubscriptionKit());
		clone.setSubscriptionQty(subscription.getSubscriptionQty());
		clone.setVu360User(subscription.getVu360User());
		clone.setSubscriptionCode(subscription.getSubscriptionCode());
		subscriptionRepository.save(clone);
	}

	//@Test
	public void searchSubscriptions() {
		List<Subscription> subscriptionList = subscriptionRepository
				.searchSubscriptions("First Kit", 60455l);//CEID223005
		if(subscriptionList!=null && subscriptionList.size()>0){
			for(Subscription subs:subscriptionList){
				System.out.println(subs.getSubscriptionCode());
			}
		}
	}
	
	//@Test
	public void findSubscriptionsByEntitlementId(){
		List<Subscription> subscriptionList = subscriptionRepository
				.findSubscriptionsByEntitlementId(221556l);
		if(subscriptionList!=null && subscriptionList.size()>0){
			for(Subscription subs:subscriptionList){
				System.out.println(subs.getSubscriptionCode());
			}
		}
	}
	
	//@Test
	public void searchsubscriptioncourse(){
		Subscription subscription = subscriptionRepository
				.searchsubscriptioncourse(2861l, "Dev Testing Course KK");
		if (subscription != null) {
			System.out.println(subscription.getSubscriptionName());
		}
		
		
	}

	//@Test
	public void Subscription_should_find_By_CourseNames(){
		Long learnerId=1L;
		Long userId=207579L;
		Long subscriptionId=2861L;
		String coursesearch="rehan";
		
		List<Map<Object, Object>> subscription = subscriptionRepository.findByIdByLearnerIdByUserIdByCourseName( learnerId,  userId,  subscriptionId, coursesearch);

		for(Map<Object, Object> map : subscription){
			System.out.println(map.get("COURSE_ID").toString());
			System.out.println(map.get("COURSENAME").toString());
			System.out.println(map.get("SUBSCRIPTION_NAME").toString());
		}
	}
	
	//@Test
	public void Subscription_should_find_By_UserId(){
		Long userId=207579L;
		List<Map<Object, Object>> subscription = subscriptionRepository.findByUserId(userId);

		for(Map<Object, Object> map : subscription){
			System.out.println(map.get("ID").toString());
			System.out.println(map.get("SUBSCRIPTION_NAME").toString());
		}
		
	}
	
	//@Test
	public void Subscription_should_update(){
		
		Subscription selectedSubscription = subscriptionRepository.findOne(1L);
		List<VU360User> userList = new ArrayList<VU360User>();
		VU360User user = vU360UserRepository.findOne(210481L);
		
		userList.add(user);
    	selectedSubscription.getVu360User().addAll(userList);
    	selectedSubscription = subscriptionService.assignToUsers(selectedSubscription);
	}
	
	@Test
	public void SubscriptionKitCourses_should_find(){
		
		List<SubscriptionKitCourses> subkitCourses = subscriptionKitCoursesRepository.findBySubscriptionKitId(2L);
		for(SubscriptionKitCourses subs:subkitCourses){
			System.out.println(subs.getCourseGUID() + " - - " +subs.getCourseGroupGUID());
		}
	}
	
	
}
