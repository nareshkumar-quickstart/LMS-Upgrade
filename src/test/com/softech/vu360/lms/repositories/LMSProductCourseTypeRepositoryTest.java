package com.softech.vu360.lms.repositories;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.LMSProductCourseType;
import com.softech.vu360.lms.repositories.LMSProductCourseTypeRepository;

public class LMSProductCourseTypeRepositoryTest extends SpringJUnitConfigAbstractTest {
	
	@Autowired
	private LMSProductCourseTypeRepository lMSProductCourseTypeRepository;
	
	@Test
	public void LMSProductCourseTypeRepository() {
		List<LMSProductCourseType> lc = lMSProductCourseTypeRepository.findByLmsProductIdByCustomerId(1L);
		System.out.println(lc.get(0).getCourseType());
	}
	
	
}
