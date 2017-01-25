package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.InstructorApproval;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class InstructorApprovalRepositoryTest {

	@Inject
	private InstructorApprovalRepository instructorApprovalRepository;
	
	
	//@Test
	public void instructorApprovals_should_find_by_approvalName_approvalNumber_deleted_active() {
		
		String approvalName ="Zeeshan11111";
		String approvalNumber= "3251";
		Boolean isActive = true;
		
		List<InstructorApproval> lstInstructorApproval = instructorApprovalRepository.findInstructorApprovals(approvalName, approvalNumber, isActive);
		
		System.out.println("..........");
	}

	//@Test
	public void instructorApprovals_should_find_by_regulatorCategoryIds() {
		
		String approvalName ="Zeeshan11111";
		String approvalNumber= "3251";
		Boolean isActive = true;
		
		Long[] regulatorCategoryIds= new Long[]{106L,5L,305L};
		try{
		List<InstructorApproval> lstInstructorApproval = instructorApprovalRepository.findByRegulatorCategoryIdIn(Arrays.asList(regulatorCategoryIds));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("..........");
	}
	
	@Test
	public void InstructorApproval_should_findByApprovedInstructorNameLikeIgnoreCaseAndInstructorApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(){
		String approvalName="%Test Instructor1 26-Apr-2016%";
		String approvalNumber="%%";
		List<InstructorApproval> cas = instructorApprovalRepository.findByApprovedInstructorNameLikeIgnoreCaseAndInstructorApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(approvalName, approvalNumber);
		if(cas!=null){
			System.out.println("List size is " +cas.size());
		}
		else{
			System.out.println("No record found");
		}
	}

}
