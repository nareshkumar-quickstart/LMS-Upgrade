package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CommissionProductCategory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CommissionProductCategoryRepositoryTest {

	@Inject
	private CommissionProductCategoryRepository commissionProductCategoryRepository;
	
	//@Test
	public void findByCommissionId() {
		List<CommissionProductCategory> aList = commissionProductCategoryRepository.findByCommissionId(1304L);
		if(aList!=null)
			System.out.println(aList.size());
	}

	//@Test
	public void findById() {
		CommissionProductCategory entity= commissionProductCategoryRepository.findOne(1168L);
		System.out.println(entity.getName());
	}

	//@Test
	public void findByParentCommissionProductCategoryIn() {
		Long[] ids = new Long[3];
		ids[0]=1168L;
		ids[1]=1169L;
		ids[2]=1170L;
		List<CommissionProductCategory> aList = commissionProductCategoryRepository.findByParentIdIn(Arrays.asList(ids));
		if(aList!=null)
			System.out.println(aList.size());
	}

	@Test
	public void findByProductCategoryCodeAndCommissionId() {
		CommissionProductCategory entity= commissionProductCategoryRepository.findByProductCategoryCodeAndCommissionId(
				"881288786780635413944238027441", 1304L);
		System.out.println(entity.getName());
	}
}
