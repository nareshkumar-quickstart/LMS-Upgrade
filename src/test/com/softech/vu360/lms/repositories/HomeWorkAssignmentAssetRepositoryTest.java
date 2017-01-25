package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.HomeWorkAssignmentAsset;

public class HomeWorkAssignmentAssetRepositoryTest  extends SpringJUnitConfigAbstractTest {
	@Autowired
	private HomeWorkAssignmentAssetRepository homeWorkAssignmentAssetRepository;
	
	@Test
	public void findByHomeWorkAssignmentCourseId() {
		List<HomeWorkAssignmentAsset> lc = homeWorkAssignmentAssetRepository.findByHomeWorkAssignmentCourseId(112541L);
		System.out.println(lc.get(0).getId());
	}
	
	//@Test
	//@Transactional
	public void deleteById() {
		homeWorkAssignmentAssetRepository.deleteById(452L);
		
	}
}
