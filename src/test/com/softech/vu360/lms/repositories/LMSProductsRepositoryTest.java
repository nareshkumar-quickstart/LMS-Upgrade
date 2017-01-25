package com.softech.vu360.lms.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.LMSProducts;
import com.softech.vu360.lms.repositories.LMSProductsRepository;

public class LMSProductsRepositoryTest  extends SpringJUnitConfigAbstractTest {
	@Autowired
	private LMSProductsRepository lMSProductsRepository;
	
	@Test
	public void LMSProductCourseTypeRepository() {
		LMSProducts lc = lMSProductsRepository.findOne(1L);
		System.out.println(lc.getProductName());
	}
}
