package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Subscription;
import com.softech.vu360.lms.model.SubscriptionCourse;
import com.softech.vu360.lms.model.SubscriptionKit;
import com.softech.vu360.lms.model.SubscriptionKitCourses;
import com.softech.vu360.lms.repositories.CustomerEntitlementRepository;
import com.softech.vu360.lms.repositories.SubscriptionCourseRepository;
import com.softech.vu360.lms.repositories.SubscriptionKitCoursesRepository;
import com.softech.vu360.lms.repositories.SubscriptionKitRepository;
import com.softech.vu360.lms.repositories.SubscriptionRepository;
import com.softech.vu360.lms.service.SubscriptionService;
import com.softech.vu360.lms.vo.SubscriptionVO;

/**
 * Date: 2015-June-20
 * @author Raja Wajahat Ali
 * @modified by 	
 */
public class SubscriptionServiceImpl implements SubscriptionService{

	private static final Logger log=Logger.getLogger(SubscriptionServiceImpl.class);
	//private SubscriptionDAO subscriptionDAO = null;
	//private EntitlementDAO entitlementDAO = null;
	
	@Inject
	private SubscriptionRepository subscriptionRepository;
	
	@Inject
	private SubscriptionCourseRepository subscriptionCourseRepository;
	
	@Inject
	private SubscriptionKitRepository subscriptionKitRepository;
	
	@Inject
	SubscriptionKitCoursesRepository subscriptionKitCoursesRepository;
	
	@Inject
	CustomerEntitlementRepository customerEntitlementRepository;
	
	@Override
	public Subscription saveSubscription(Subscription subscription) {
		return subscriptionRepository.save(subscription);
	}

	@Override
	public Subscription updateSubscription(Subscription subscription) {
		return subscriptionRepository.save(subscription);
	}
	
	@Override
	public List<Subscription> getSubscriptionsBySubscriptionCode(
			String subscriptioncode) {
		return subscriptionRepository.findAllBySubscriptionCode(subscriptioncode);
		
	}
	@Override
	public Subscription getSubscriptionBySubscriptionCode(
			String subscriptioncode) {
		return subscriptionRepository.findBySubscriptionCode(subscriptioncode);
		
	}

	@Override
	public List<Subscription> getSubscriptionsByEntitlementId(long entitlementId) {
		return subscriptionRepository.findSubscriptionsByEntitlementId(entitlementId);
	}
	
	@Override
	public SubscriptionKit getSubscriptionKitByGUID(String kitCode){
		return subscriptionKitRepository.findByGuid(kitCode);
	}
	/*
	@Override
	public List<SubscriptionKitCourses> getAllSubscriptionKitCoursesByKit(long kitId){
		return 	subscriptionDAO.getAllSubscriptionKitCoursesByKit( kitId);
	}*/
	
	@Override
	public List<Subscription> searchSubscriptions(String subscriptionName, long customerId) {

        List<Subscription> lstSubscription = subscriptionRepository.searchSubscriptions(subscriptionName,customerId);
       
        return lstSubscription;
    }

	/*
	@Override
	public List<SubscriptionCourse> saveSubscriptionCourses(
			List<SubscriptionCourse> listSubscriptionCourse) {
		List<SubscriptionCourse> listSubscriptionCourseReturn = new ArrayList<SubscriptionCourse>();
		
		if(listSubscriptionCourse!=null && listSubscriptionCourse.size()>0){
			for (SubscriptionCourse subscriptionCourse : listSubscriptionCourse) {
				subscriptionCourse = subscriptionDAO.saveSubscriptionCourse(subscriptionCourse);
				listSubscriptionCourseReturn.add(subscriptionCourse);
			}
		}
		return listSubscriptionCourseReturn;
	}
	 */
	@Override
	public List<SubscriptionCourse> findSubscriptionCoursesBySubscriptionId(
			Long id) {
		return subscriptionCourseRepository.findBySubscriptionId(id);
	}

	@Override
	public List<SubscriptionCourse> findSubscriptionCoursesBySubscriptionCode(
			String subscriptionCode) {
		return subscriptionCourseRepository.findBySubscriptionSubscriptionCode(subscriptionCode);
	}


	@Override
	public Subscription findSubscriptionById(Long id) {
		return subscriptionRepository.findOne(id);
	}
	
	@Override
	public boolean hasseatsavailable(int learnercount,long entitlementId,long subscriptionId) {
		int availableSeats=0;
		CustomerEntitlement entitlement=customerEntitlementRepository.findOne(entitlementId);
		availableSeats=entitlement.getMaxNumberSeats()-entitlement.getNumberSeatsUsed();
		if(availableSeats==0){
			return false;
		}
		else
		{
			return true;
		}
		//return subscriptionDAO.hasseatsavailable(learnercount, entitlementId, subscriptionId);
	}

	public boolean saveSubscriptionCourses(long subscriptionId, long subscriptionKitid){
		return subscriptionCourseRepository.saveSubscriptionCourses(subscriptionId, subscriptionKitid);
	}

	// ask saleem
	@Override
	public List<SubscriptionKitCourses> getAllSubscriptionKitCoursesByKit(long kitId) {
		return subscriptionKitCoursesRepository.findBySubscriptionKitId(kitId);
	}

	//
	@Override
	public Subscription loadSubscriptionById(Long id) {
		return subscriptionRepository.findOne(id);
	}

	@Override
	
	public Subscription searchsubscriptioncourse(Long id, String courseName) {
		return subscriptionRepository.searchsubscriptioncourse(id, courseName);
	}

	@Override
	public List<SubscriptionVO> getUserSubscriptionCourses(long learnerId,long userId, long subscriptionId, String coursesearch) {
		List<Map<Object, Object>> mp_subscriptionCourses=subscriptionRepository.findByIdByLearnerIdByUserIdByCourseName(learnerId, userId,subscriptionId,coursesearch);
		List<SubscriptionVO> lstSubscriptionVO = new ArrayList<SubscriptionVO>();

		if(mp_subscriptionCourses!=null && !mp_subscriptionCourses.isEmpty() ){
			
			for(Map<Object, Object> map : mp_subscriptionCourses){
				SubscriptionVO subscriptionCourseselVO = new SubscriptionVO();
				subscriptionCourseselVO.setSubscriptionCourseId(map.get("COURSE_ID").toString());
				subscriptionCourseselVO.setSubscriptionCourseName(map.get("COURSENAME").toString());
				subscriptionCourseselVO.setSubscriptionName(map.get("SUBSCRIPTION_NAME").toString());
				subscriptionCourseselVO.setSubscriptionId(map.get("ID").toString());
				subscriptionCourseselVO.setCourseType(map.get("COURSETYPE").toString());
				if(map.get("ENROLLMENTID")!= null)
				subscriptionCourseselVO.setEnrollmentId(map.get("ENROLLMENTID").toString());
				lstSubscriptionVO.add(subscriptionCourseselVO);
			}
         }	
		
		return lstSubscriptionVO;
	}

	@Override
	public List<SubscriptionVO> getUserSubscriptions(long userId) {
		List<Map<Object, Object>> mp_subscriptionCourses=subscriptionRepository.findByUserId(userId);
		List<SubscriptionVO> lstSubscriptionVO = new ArrayList<SubscriptionVO>();

		if(mp_subscriptionCourses!=null && !mp_subscriptionCourses.isEmpty() ){
			
			for(Map<Object, Object> map : mp_subscriptionCourses){
				SubscriptionVO subscriptionCourseselVO = new SubscriptionVO();
				
				subscriptionCourseselVO.setSubscriptionName(map.get("SUBSCRIPTION_NAME").toString());
				subscriptionCourseselVO.setSubscriptionId(map.get("ID").toString());
				lstSubscriptionVO.add(subscriptionCourseselVO);
			}
         }	
		
		return lstSubscriptionVO;
	}

	@Override
	public Subscription assignToUsers(Subscription updatedSubscription) {
		return subscriptionRepository.save(updatedSubscription);
	}

	@Override
	public Subscription updateStatus(Subscription subscription) {
		return subscriptionRepository.save(subscription);
	}
	
}
