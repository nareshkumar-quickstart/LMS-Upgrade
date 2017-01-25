package com.softech.vu360.lms.repositories;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CourseCustomerEntitlementItemRepositoryTest {

	
	@Inject 
	CourseCustomerEntitlementItemRepository courseCustomerEntitlementItemRepository;
	@Inject
	private CustomerRepository customerRepository;
	
	
	//@Test
	public void getActiveCourseContractsFor(){
		
		List<CourseCustomerEntitlementItem> lst = courseCustomerEntitlementItemRepository.getActiveCourseContractsFor(10303L,new Date(),new Date(),true,4195L);
		
		for(CourseCustomerEntitlementItem obj : lst){
			System.out.println(obj.getId());
		}
		
		
	}
	
	//@Test
	public void findByCustomerEntitlementCustomerIdAndCourseCourseStatus(){
		String courseName="",courseGUID="10022",keywords="LCMS-2561";
		boolean isCourseName=false,isCourseGUID=false,isKeywords=false;
		List<CourseCustomerEntitlementItem> lst = new ArrayList<CourseCustomerEntitlementItem>();
		if(StringUtils.isNotEmpty(courseName))
		{
			isCourseName=true;
			
		}
		
		if(StringUtils.isNotEmpty(courseGUID))
		{
			isCourseGUID=true;
			
		}
		
		if(StringUtils.isNotEmpty(keywords))
		{
			isKeywords=true;
			
		}
		
		if(isCourseName && !isCourseGUID && !isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWith(10303L,"Published",courseName);
		}else if(!isCourseName && isCourseGUID && !isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseBussinesskeyStartingWith(10303L,"Published",courseGUID);
		}else if(!isCourseName && !isCourseGUID && isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseKeywordsStartingWith(10303L,"Published",keywords);
		}else if(isCourseName && isCourseGUID && !isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWithAndCourseBussinesskeyStartingWith(10303L,"Published",courseName,courseGUID);
		}else if(isCourseName && !isCourseGUID && isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWithAndCourseKeywordsStartingWith(10303L,"Published",courseName,keywords);
		}else if(!isCourseName && isCourseGUID && isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseBussinessKeyStartingWithAndCourseKeywordsStartingWith(10303L,"Published",courseGUID,keywords);
		}else if(isCourseName && isCourseGUID && isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWithAndCourseBussinesskeyStartingWithAndCourseKeywordsStartingWith(10303L,"Published",courseName,courseGUID,keywords);
		}else{ 
		
			//lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatus(10303L,"Published");
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWithAndCourseBussinesskeyStartingWithAndCourseKeywordsStartingWith(10303L,"Published",courseName,courseGUID,keywords);
		
		}
		System.out.println("***************** SIZE= "+lst.size());
		System.out.println("******************* ID = "+lst.get(0).getId());
	}
	
	// @Test
		public void findOnetest() {
			CourseCustomerEntitlementItem ccei = courseCustomerEntitlementItemRepository
					.findOne(52l);
			System.out.println(ccei.getCourse().getId());
		}

		// @Test
		public void findByCustomerEntitlementIdAndCourseIdIN() {

			Long[] courseId = { 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l, 10l, 11l, 12l,
					50l, 60l, 70l, 80l, 90l, 100l };
			List<CourseCustomerEntitlementItem> cList = courseCustomerEntitlementItemRepository
					.findByCustomerEntitlementIdAndCourseIdIn(51l, courseId);
			if (cList != null && cList.size() > 0) {
				for (CourseCustomerEntitlementItem ce : cList) {
					System.out.println(ce.getId());
				}
			}
		}

		// @Test
		public void findByCustomerEntitlementId() {
			List<CourseCustomerEntitlementItem> cList = courseCustomerEntitlementItemRepository
					.findByCustomerEntitlementId(52l);
			if (cList != null && cList.size() > 0) {
				for (CourseCustomerEntitlementItem ce : cList) {
					System.out.println(ce.getId());
				}
			}
		}

		// @Test
		public void findByCustomerEntitlementIdAndCourseRetiredFalse() {
			List<CourseCustomerEntitlementItem> cList = courseCustomerEntitlementItemRepository
					.findByCustomerEntitlementIdAndCourseRetiredFalse(52l);// ID
																			// with
																			// retired
																			// true
																			// -
																			// 156853
			if (cList != null && cList.size() > 0) {
				for (CourseCustomerEntitlementItem ce : cList) {
					System.out.println(ce.getId());
				}
			}
		}

		// @Test
		public void findByCustomerEntitlementIdAndCourseId() {
			// ID with retired true - 156853
			List<CourseCustomerEntitlementItem> cList = courseCustomerEntitlementItemRepository
					.findByCustomerEntitlementIdAndCourseId(3556l, 1675l);
			
			if (cList != null && cList.size() > 0) {
				for (CourseCustomerEntitlementItem ce : cList) {
					System.out.println(ce.getId());
				}
			}
		}
		
		//@Test
		public void save(){
			CourseCustomerEntitlementItem ccei = courseCustomerEntitlementItemRepository
					.findOne(55l);
			CourseCustomerEntitlementItem cceiClone=new CourseCustomerEntitlementItem();
			cceiClone.setCourseGroup(ccei.getCourseGroup());
			cceiClone.setCourse(ccei.getCourse());
			cceiClone.setCustomerEntitlement(ccei.getCustomerEntitlement());
			courseCustomerEntitlementItemRepository.save(cceiClone);
		}
		
		//@Test
		public void delete(){
			CourseCustomerEntitlementItem ccei = courseCustomerEntitlementItemRepository
					.findOne(224735l);
			courseCustomerEntitlementItemRepository.delete(ccei);
		}
		
		//@Test
		public void getCoursesByNameAndCourseType() {

			Customer customer = customerRepository.findOne(1l);
			List<CourseCustomerEntitlementItem> cList = courseCustomerEntitlementItemRepository
					.getCoursesByNameAndCourseType(customer, "course", "course");
			if (cList != null && cList.size() > 0) {
				for (CourseCustomerEntitlementItem ce : cList) {
					System.out.println(ce.getId());
				}
			}

		}
	
	@Test
	public void findByCustomerEntitlement_CustomerAndCourseId(){
		Customer customer = new Customer();
		customer.setId(447L);
		System.out.print("*********** Name = "+courseCustomerEntitlementItemRepository.findByCustomerEntitlement_CustomerAndCourseId(customer,1659L).get(0).getCustomerEntitlement().getCustomer().getName());
	}
}
