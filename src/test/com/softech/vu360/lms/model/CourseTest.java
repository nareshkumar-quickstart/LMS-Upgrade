package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class CourseTest extends TestBaseDAO<Course> {


	@Before
	public void setRequiredService() {

	}

//	@Test
	public void Course_should_save() throws Exception {
	
		Course course=new ScormCourse();
		
		course.setCourseTitle("Test_Course_HomeworkAssignmentCourse");
		course.setCourseId("TestCourse_1");
		course.setKeywords("Test");
		course.setCredithour("4");;
		course.setVersion("Test_Version_1");
		course.setBussinesskey("Test_Business_Key");
		course.setBusinessUnitName("Test_BusinessName");
		course.setBusinessUnitId(3);
		course.setCourseStatus(Course.PUBLISHED);

		ContentOwner contentOwner = (ContentOwner)crudFindById(ContentOwner.class, new Long(1));
		course.setContentOwner(contentOwner);

		Language language = new Language();
		try {
		    language.setId(1l);
		    language.setLanguage("en");
		    language.setCountry("US");
		    language.setVariant("En-US");
		} catch (Exception pe) {
		    pe.printStackTrace();
		}

		course.setLanguage(language);
		course.setDescription("Test_Course_Description");

		
		if(course instanceof HomeworkAssignmentCourse){
			((HomeworkAssignmentCourse) course).setAssignmentDueDate(new Date());
			((HomeworkAssignmentCourse) course).setMasteryScore(56.00);
			((HomeworkAssignmentCourse) course).setGradingMethod("Test_GradingMethod");
		}
		else if(course instanceof InstructorConnectCourse){
			((InstructorConnectCourse) course).setInstructorType("Test_InstructorType");
		    ((InstructorConnectCourse) course).setMeetingId("Test_Meeting");
		    ((InstructorConnectCourse) course).setMeetingPasscode("Test_123");
		    ((InstructorConnectCourse) course).setEmailAddress("test@gmai.com");
		}
		else if(course instanceof WebLinkCourse){
			((WebLinkCourse) course).setLink("Test_WebLinkCourse");
		}
		
		try{
			course=save(course);
		}
		catch(Exception ex){
			System.out.println(course.getId());
		}
	}
	
//	@Test
	public void Course_should_update_with_new_SCO(){
		
		ScormCourse course=(ScormCourse)getById(new Long(118714), Course.class);
		course.setCourseTitle("Test_Course_ScormCourse");
		//Setting SCO for Scorm
		ArrayList<SCO> scosList = new ArrayList<SCO>();
		SCO scoObj = new SCO();
		scoObj.setLaunchURI("Test_URI");
		scoObj.setLastUpdatedUsername("Test_User");
		scoObj.setCreatedUsername("Test_User");
		scoObj.setCreatedDate(new Date());
		scoObj.setLastUpdatedDate(new Date());
		scoObj.setDefaultSequenceOrder(1);
		scoObj.setCourse(course);
		scosList.add(scoObj);
		course.setScos(scosList);
		save(course);
	}

//	@Test
	public void CourseCustomerEntitlementItem_should_save(){
		
		CourseCustomerEntitlementItem item=new CourseCustomerEntitlementItem();
		Course course=getById(new Long(4195),Course.class);
		CourseGroup courseGrp=(CourseGroup)crudFindById(CourseGroup.class, new Long(73));
		CustomerEntitlement entitlement=(CustomerEntitlement)crudFindById(CustomerEntitlement.class, new Long(167954));
		item.setCourse(course);
		item.setCourseGroup(courseGrp);
		item.setCustomerEntitlement(entitlement);
		try{
			item=(CourseCustomerEntitlementItem)crudSave(CourseCustomerEntitlementItem.class, item);
		}
		catch(Exception ex){
			System.out.println(item.getId());
		}
	}
	
//	@Test
	public void CourseGroup_should_save(){
		
		CourseGroup courseGrp=new CourseGroup();
		courseGrp.setDescription("Test_CourseGroup");
		courseGrp.setKeywords("Test_Keyword");
		courseGrp.setName("Test_CourseGrp_Name");
		courseGrp.setParentCourseGroup(null);
		ContentOwner owner=(ContentOwner)crudFindById(ContentOwner.class, new Long(1));
		courseGrp.setContentOwner(owner);
		try{
			courseGrp=(CourseGroup)crudSave(CourseGroup.class, courseGrp);
		}
		catch(Exception ex){
			System.out.println(courseGrp.getId());
		}
	}
	
//	@Test
	public void CourseGroup_should_update(){
		
		CourseGroup courseGrp=(CourseGroup)crudFindById(CourseGroup.class, new Long(10858));
		List<Course> list=new ArrayList<Course>();
		Course course=(Course)crudFindById(Course.class, new Long(4195));
		list.add(course);
		courseGrp.setCourses(list);
		crudSave(CourseGroup.class, courseGrp);
	}
	
	@Test
	public void CourseGroupCustomerEntitlementItem_should_save(){
		
		CourseGroupCustomerEntitlementItem item=new CourseGroupCustomerEntitlementItem();
		CourseGroup courseGrp=(CourseGroup)crudFindById(CourseGroup.class, new Long(9105));
		CourseGroupCustomerEntitlement entitlement=(CourseGroupCustomerEntitlement)crudFindById(CourseGroupCustomerEntitlement.class, new Long(193605));
		item.setCourseGroup(courseGrp);
		item.setCourseGroupCustomerEntitlement(entitlement);
		try{
			item=(CourseGroupCustomerEntitlementItem)crudSave(CourseGroupCustomerEntitlementItem.class, item);
		}
		catch(Exception ex){
			System.out.println(item.getCourseGroup().getId());
		}
	}
	
}
