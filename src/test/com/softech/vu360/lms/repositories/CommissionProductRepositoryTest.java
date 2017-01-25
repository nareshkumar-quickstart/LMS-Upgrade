package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CommissionProduct;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CommissionProductRepositoryTest {

	@Inject
	private CommissionProductRepository commissionProductRepository;
	
	@Test
	public void findByCommissionProductCategoryIdIn() {
		Long[] ids = new Long[3];
		ids[0]=1L;
		ids[1]=1L;
		ids[2]=1L;
		List<CommissionProduct> aList = commissionProductRepository.findByCommissionProductCategoryIdIn(ids);
		if(aList!=null)
			System.out.println(aList.size());
	}

}
