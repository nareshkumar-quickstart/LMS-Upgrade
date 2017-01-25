package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CommissionParticipation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CommissionParticipationTest {
	@Inject
	private CommissionParticipationRepository commissionParticipationRepository;

//	@Test
	public void findByCommissionId() {
		List<CommissionParticipation> aList= commissionParticipationRepository.findByCommissionId(3155L);
		System.out.println(aList);
	}
	
	//@Test
	public void findById() {
		CommissionParticipation entity= commissionParticipationRepository.findOne(3407L);
		System.out.println(entity.getKey());
	}

	@Test
	public void findByIdIn() {
		Long[] ids = new Long[2];
		ids[0]=3407L;
		ids[1]=3408L;
		List<CommissionParticipation> aList= commissionParticipationRepository.findByIdIn(ids);
		System.out.println(aList);
	}
}
