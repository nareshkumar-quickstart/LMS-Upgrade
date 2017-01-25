package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.softech.vu360.lms.model.RegulatoryApproval;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=com.softech.vu360.lms.LmsTestConfig.class)
public class RegulatoryApprovalRepositoryTest {

	@Inject
	private RegulatoryApprovalRepository regulatoryApprovalRepository;
	
	@Test
	public void regulatoryApprovalRepository_should_be_retrieved() {
		
		Long[] catIds = new Long[]{new Long(102)};
		
		List<RegulatoryApproval> findRegulatoryApproval = regulatoryApprovalRepository.findByRequirements(catIds);
		Assert.notNull(findRegulatoryApproval);
		
		System.out.println("..........");
	}
	
	@Test
	public void regulatoryApproval_should_be_deleted() {
		RegulatoryApproval objDelete = regulatoryApprovalRepository.findOne(new Long(102));
		regulatoryApprovalRepository.delete(objDelete);
		System.out.println("..........");
	}
	

}
