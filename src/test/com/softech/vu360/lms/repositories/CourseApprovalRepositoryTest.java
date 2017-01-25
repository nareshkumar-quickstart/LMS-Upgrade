package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CourseApprovalRepositoryTest {

	@Inject
	private CourseApprovalRepository repoCourseApproval;
	
	
	//@Test
	public void courseApprovals_should_find_by_approvalName_approvalNumber_deleted_active() {
		
		String approvalName ="Edited Approval";
		String approvalNumber= "1dsdsd";
		Boolean isDeleted = true; 
		Boolean isActive = true;
		
		List<CourseApproval> lstContact = repoCourseApproval.findCourseApprovals(approvalName, approvalNumber, isDeleted, isActive);
		
		System.out.println("..........");
	}
	
	//@Test
	public void courseApproval_should_Find_By_courseConfigurationTemplate(){
		Long ccTemplateId = 303L;
		List<CourseApproval>	listCA = repoCourseApproval.findByTemplateIdAndDeletedFalse(ccTemplateId);
		System.out.println("..................");
	}
	 
	//@Test
	public void courseApproval_should_Find_By_id(){
		long ccTemplateId = 265;
		CourseApproval ca = repoCourseApproval.findByIdAndDeletedFalse(ccTemplateId);
		System.out.println("..................");
	}
	
	//@Test
	public void courseApprovals_should_Find_By_courseId(){
		long courseId = 3550;
		List<CourseApproval> cas = repoCourseApproval.findByCourseIdAndDeletedFalseAndActiveTrue(courseId);
		System.out.println("..................");
	}
	
	//@Test
	public void courseApprovals_should_Find_By_DateRange(){
		Date startDate= new Date();
		startDate.setDate(01);
		startDate.setMonth(10);
		startDate.setYear(07);
		
		Date endDate= new Date(System.currentTimeMillis());
		List<CourseApproval> cas = repoCourseApproval.findByCourseApprovalEffectivelyEndsDateBetweenAndDeletedFalseAndActiveTrue(startDate, endDate);
				
		System.out.println("..................");
	}
	
	//@Test
	public void courseApprovals_should_Find_By_course(){
		Course c = new Course();
		c.setId(3550L);
		List<CourseApproval> cas = repoCourseApproval.findByCourseAndDeletedFalseAndActiveTrue(c);
		System.out.println("..................");
	}
	
//	/@Test
	public void courseApprovals_isCourseAlreadyAssociatedWithRegulatorAuthority(){
		Course c = new Course();
		c.setId(3550L);
		Date startDate= new Date();
		startDate.setDate(01);
		startDate.setMonth(10);
		startDate.setYear(07);
		
		Date endDate= new Date(System.currentTimeMillis());
		boolean cas = repoCourseApproval.isCourseAlreadyAssociatedWithRegulatorAuthority(c.getId(), 5L,startDate,endDate,1L);
		System.out.println("..................");
	}
	
	
	//@Test
	public void courseApprovals_getNumberOfUnusedPurchaseCertificateNumbers(){
		int cas = repoCourseApproval.getNumberOfUnusedPurchaseCertificateNumbers(13805L);
		System.out.println("..................");
	}
	
	//@Test
	public void courseApprovals_isCertificateLinkedWithCourseApproval(){
		
		List<Long> l = new ArrayList<Long>();
		l.add(13805L);
		repoCourseApproval.isCertificateLinkedWithCourseApproval(l);
		System.out.println("..................");
	}
	
	//@Test
	public void courseApprovals_isAffidavitLinkedWithCourseApproval(){
		
		List<Long> l = new ArrayList<Long>();
		l.add(13805L);
		repoCourseApproval.isAffidavitLinkedWithCourseApproval(l);
		System.out.println("..................");
	}
	
	//@Test
	public void courseApprovals_getCourseApprovalByCourse(){
	
		List<Map<Object, Object>>  sfsd = repoCourseApproval.getCourseApprovalByCourse("14F93637DED84498BCBB8A336A1A1861", "13805");
		System.out.println("..................");
	}
	
	@Test
	public void CourseApproval_should_findByApprovedCourseNameLikeIgnoreCaseAndCourseApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(){
		String approvalName="%Test Course-3 25-Apr-2016%";
		String approvalNumber="%%";
		List<CourseApproval> cas = repoCourseApproval.findByApprovedCourseNameLikeIgnoreCaseAndCourseApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(approvalName, approvalNumber);
		if(cas!=null){
			System.out.println("List size is " +cas.size());
		}
		else{
			System.out.println("No record found");
		}
	}
}
