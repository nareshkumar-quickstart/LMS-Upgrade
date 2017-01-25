package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.ProviderApproval;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class ProviderApprovalRepositoryTest {

	@Inject
	private ProviderApprovalRepository providerApprovalRepository;
	
	//@Test
	public void providerApprovals_should_find_by_approvalName_approvalNumber_deleted_active() {
		
		String approvedProviderName ="Approvals approval 1";
		String providerApprovalNumber= "3";
		Boolean isActive = true;
		
		List<ProviderApproval> lstProviderApproval = providerApprovalRepository.findProviderApprovals(approvedProviderName, providerApprovalNumber, isActive);
		
		System.out.println("..........");
	}

	//@Test
	public void providerApproval_should_find_by_id() {
		
		
		ProviderApproval providerApproval = providerApprovalRepository.findOne(2L);
		
		System.out.println("..........");
	}
	
	@Test
	public void ProviderApproval_should_findByApprovedProviderNameLikeIgnoreCaseAndProviderApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(){
		String approvalName="%Test Provider1 26-Apr-2016%";
		String approvalNumber="%%";
		List<ProviderApproval> cas = providerApprovalRepository.findByApprovedProviderNameLikeIgnoreCaseAndProviderApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(approvalName, approvalNumber);
		if(cas!=null){
			System.out.println("List size is " +cas.size());
		}
		else{
			System.out.println("No record found");
		}
	}
}
