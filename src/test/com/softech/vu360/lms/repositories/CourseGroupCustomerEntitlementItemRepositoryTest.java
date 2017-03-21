package com.softech.vu360.lms.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.Distributor;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CourseGroupCustomerEntitlementItemRepositoryTest {

	
	@Inject 
	CourseGroupCustomerEntitlementItemRepository courseGroupCustomerEntitlementItemRepository;
	
	
	//@Test
	public void getCourseGroupIdsForDistributor(){
		
		 Distributor dis = new Distributor();
	     dis.setId(1L);
		
		List<Map<Object,Object>> lst  = courseGroupCustomerEntitlementItemRepository.getCourseGroupIdsForDistributor(dis);
		int index=0;
		Long[] courseGroupIDArray= new Long[lst.size()];
		for(Map<Object, Object> row : lst){
			courseGroupIDArray[index++]= Long.valueOf(String.valueOf(row.get("COURSEGROUP_ID")));
		}
		
		for(Map map : lst){
			System.out.print("***************** ID = ");
		}
		
	}
	
//	@Test
	public void findByCustomerIdAndStatus(){
		//String courseName="",courseGUID="10022",keywords="LCMS-2561";
		String courseName="",courseGUID="",keywords="";
		boolean isCourseName=false,isCourseGUID=false,isKeywords=false;
		List<CourseGroupCustomerEntitlementItem> lst = new ArrayList<CourseGroupCustomerEntitlementItem>();
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
			lst =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseTitle(10303L,"Published",courseName);
		}else if(!isCourseName && isCourseGUID && !isKeywords){
			lst =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseBussinessKey(10303L,"Published",courseGUID);
		}else if(!isCourseName && !isCourseGUID && isKeywords){
			lst =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseKeywords(10303L,"Published",keywords);
		}else if(isCourseName && isCourseGUID && !isKeywords){
			lst =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseTitleAndCourseBussinessKey(10303L,"Published",courseName,courseGUID);
		}else if(isCourseName && !isCourseGUID && isKeywords){
			lst =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseTitleAndCourseKeywords(10303L,"Published",courseName,keywords);
		}else if(!isCourseName && isCourseGUID && isKeywords){
			lst =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseBussinessKeyAndCourseKeywords(10303L,"Published",courseGUID,keywords);
		}else if(isCourseName && isCourseGUID && isKeywords){
			lst =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseTitleAndCourseBussinessKeyAndKeywords(10303L,"Published",courseName,courseGUID,keywords);
		}else{ 
		
			//lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatus(10303L,"Published");
			lst =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatus(30506L,"Published");
		
		}
		System.out.println("***************** SIZE= "+lst.size());
		//System.out.println("******************* ID = "+lst.get(0).getId());
	}
	

	@Transactional
	@Test
	public void save() {
		/*
		 * @Kaunain - Please use this method with caution, Improper use may
		 * result in data anomaly
		 */
		List<CourseGroupCustomerEntitlementItem> ceList = courseGroupCustomerEntitlementItemRepository.findByCustomerEntitlementIdAndCourseGroupIdIn(3559l, new long[]{72l});
		CourseGroupCustomerEntitlementItem ce = (CourseGroupCustomerEntitlementItem) ceList.get(0);
	
		CourseGroupCustomerEntitlementItem clone = new CourseGroupCustomerEntitlementItem();
		clone.setCourseGroup(ce.getCourseGroup());
		clone.setCourseGroupCustomerEntitlement(ce.getCourseGroupCustomerEntitlement());
		
		List<CourseGroupCustomerEntitlementItem> cloneList = new ArrayList<CourseGroupCustomerEntitlementItem>();
		cloneList.add(clone);
		
		courseGroupCustomerEntitlementItemRepository.save(clone);
		
	}

	// @Transactional
	// @Test
	public void delete() {
		/*
		 * @Kaunain - Please use this method with caution, improper use may
		 * result in anomaly
		 */
		List<CourseGroupCustomerEntitlementItem> objList = new ArrayList<CourseGroupCustomerEntitlementItem>();
		//objList.add(courseGroupCustomerEntitlementItemRepository.findOne(1l));
		//objList.add(courseGroupCustomerEntitlementItemRepository.findOne(2l));
		//objList.add(courseGroupCustomerEntitlementItemRepository.findOne(3l));
		courseGroupCustomerEntitlementItemRepository.delete(objList);

	}

	//@Test
	public void findByCustomerEntitlementIdAndCourseGroupIdIn() {
		long ids[] = { 72l, 73l };
		List<CourseGroupCustomerEntitlementItem> objList = courseGroupCustomerEntitlementItemRepository
				.findByCustomerEntitlementIdAndCourseGroupIdIn(3559l, ids);
		if (objList != null && objList.size() > 0) {
			for (CourseGroupCustomerEntitlementItem ce : objList) {
				System.out.println(ce.getCourseGroup().getId()+"...."+ce.getCourseGroupCustomerEntitlement().getId());
			}
		}
	}

//	@Test
	public void findByCustomerEntitlementId() {
		List<CourseGroupCustomerEntitlementItem> objList = courseGroupCustomerEntitlementItemRepository
				.findByCustomerEntitlementId(3559l);
		if (objList != null && objList.size() > 0) {
			for (CourseGroupCustomerEntitlementItem ce : objList) {
				System.out.println(ce.getCourseGroup().getId());
			}
		}
	}
	
	//Added By Marium Saud
//	@Test
	public void CourseGroupCustomerEntitlementItem_should_getCourseGroupCustomerEntitlementItemByCourseId(){
		List<CourseGroupCustomerEntitlementItem> objList = courseGroupCustomerEntitlementItemRepository.findByCourseGroupCoursesIdAndCustomerEntitlementCustomerId(970L, 474L);
		if (objList != null && objList.size() > 0) {
			for (CourseGroupCustomerEntitlementItem ce : objList) {
				System.out.println(ce.getCourseGroup().getId());
			}
		}
	}
	
//	@Test
	public void CourseGroupCustomerEntitlementItem_should_findCourseGroupEntitlementItems(){
		List<CourseGroupCustomerEntitlementItem> objList = courseGroupCustomerEntitlementItemRepository.findByCustomerEntitlementIdAndCourseGroupCoursesId(2048L,970L);
		if (objList != null && objList.size() > 0) {
			for (CourseGroupCustomerEntitlementItem ce : objList) {
				System.out.println(ce.getCourseGroup().getId());
			}
		}
	}
	
	//@Test
	public void CourseGroupCustomerEntitlementItem_should_getActiveCourseGroupContractsFor(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		try {
			date=sdf.parse("10/23/2009");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<CourseGroupCustomerEntitlementItem> objList=courseGroupCustomerEntitlementItemRepository.getActiveCourseGroupContractsFor(53L,date, 102729L);
		if (objList != null && objList.size() > 0) {
			for (CourseGroupCustomerEntitlementItem ce : objList) {
				System.out.println(ce.getCourseGroup().getId());
			}
		}
	}
}
	

