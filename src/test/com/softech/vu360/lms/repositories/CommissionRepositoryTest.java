package com.softech.vu360.lms.repositories;

import java.util.HashSet;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Commission;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CommissionRepositoryTest {
	@Inject
	private CommissionRepository commissionRepository;
	
	//@Test
	public void findOne() {
		Commission entity = commissionRepository.findOne(805L);
		if(entity!=null)
			System.out.println(entity.getName());
	}

	@Test
	public void getCommissions() {
		HashSet<Commission> aSet = commissionRepository.getCommissions(11506L);
		if(aSet!=null)
			System.out.println(aSet.size());
	}
	
}
