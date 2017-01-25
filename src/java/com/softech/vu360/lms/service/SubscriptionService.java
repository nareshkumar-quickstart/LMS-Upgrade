package com.softech.vu360.lms.service;

import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.EnrollmentSubscriptionView;
import com.softech.vu360.lms.model.Subscription;
import com.softech.vu360.lms.model.SubscriptionCourse;
import com.softech.vu360.lms.model.SubscriptionKit;
import com.softech.vu360.lms.model.SubscriptionKitCourses;

import com.softech.vu360.lms.vo.SubscriptionVO;

import com.softech.vu360.lms.model.VU360User;


/**
 * Date: 2015-June-20
 * @author Raja Wajahat Ali
 * @modified by 	
 */
public interface SubscriptionService {
	public Subscription findSubscriptionById(Long id);
	public Subscription loadSubscriptionById(Long id);
	public Subscription saveSubscription(Subscription subscription);
	public Subscription updateSubscription(Subscription subscription);
	public List<Subscription> getSubscriptionsBySubscriptionCode(String subscriptioncode);
	public List<Subscription> getSubscriptionsByEntitlementId(long entitlementId);
	public List<SubscriptionCourse> findSubscriptionCoursesBySubscriptionId(Long id);
	public List<SubscriptionCourse> findSubscriptionCoursesBySubscriptionCode(String subscriptionCode);
	//public List<SubscriptionCourse> saveSubscriptionCourses(List<SubscriptionCourse> listSubscriptionCourse);
	public List<Subscription> searchSubscriptions(String subscriptionName, long customerid);
	public boolean hasseatsavailable(int learnercount,long entitlementId,long subscriptionId);
	public Subscription getSubscriptionBySubscriptionCode(String subscriptioncode);
	public SubscriptionKit getSubscriptionKitByGUID(String kitCode);
	public List<SubscriptionKitCourses> getAllSubscriptionKitCoursesByKit(long kitId);
	public boolean saveSubscriptionCourses(long subscriptionId, long subscriptionKitid);
	public Subscription searchsubscriptioncourse(Long id, String courseName);
	public List<SubscriptionVO> getUserSubscriptionCourses(long learnerId, long userId,long subscriptionId,String coursesearch);
	public List<SubscriptionVO> getUserSubscriptions(long userId);
    public Subscription assignToUsers(Subscription updatedSubscription);
    public Subscription updateStatus(Subscription subscription);
}