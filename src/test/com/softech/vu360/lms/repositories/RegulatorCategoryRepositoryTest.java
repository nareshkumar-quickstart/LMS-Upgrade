package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.RegulatorCategory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class RegulatorCategoryRepositoryTest {

	@Inject
	private RegulatorCategoryRepository regulatorCategoryRepository;
	
	//@Test
	public void regulatorCategory_should_save() {
		RegulatorCategory saveRegulatorCategory = regulatorCategoryRepository.findOne(new Long(2105));
		saveRegulatorCategory.setId(null);
		
		RegulatorCategory savedRegulatorCategory = regulatorCategoryRepository.save(saveRegulatorCategory);
		
		System.out.println("..........");
	}
	
	//@Test
	public void regulatorCategory_should_find() {
		RegulatorCategory saveRegulatorCategory = regulatorCategoryRepository.findOne(new Long(2105));
		
		System.out.println("..........");
	}
	
	//@Test
	public void regulatorCategory_should_deleteAll(List<RegulatorCategory> regulatorCategorys) {
		regulatorCategoryRepository.delete(regulatorCategorys);
		
		System.out.println("..........");
	}

	//@Test
	public void regulatorCategory_should_saveAll(List<RegulatorCategory> regulatorCategorys) {
		regulatorCategoryRepository.save(regulatorCategorys);
		
		System.out.println("..........");
	}

	//@Test
	public void regulatorCategory_should_find_by_regulatorId() {
		List<RegulatorCategory> rc = regulatorCategoryRepository.findByRegulator(2L);		
		System.out.println("..........");
	}
	
	//@Test
	public void regulatorCategory_should_find_by_categoryType_displayName_regulatorId() {
		String categoryType="Post License";
		String displayName="vxv";
		Long regulatorId=2L;
		
		List<RegulatorCategory> rc = regulatorCategoryRepository.findByCriteria(null, displayName, null);
		System.out.println("..........");
	}
	
	//@Test
	public void regulatorCategory_should_findByCategoryTypeCategoryNameRegulatorId() {
		String categoryType="";//"Post License";
		String displayName="";//"vxv";
		Long regulatorId=2L;
		
		List<RegulatorCategory> rc = regulatorCategoryRepository.findByCategoryTypeCategoryNameRegulatorId(categoryType, displayName, regulatorId);
		System.out.println("..........");
	}
	
	@Test
	public void regulatorCategory_isRegulatorCategoryTypeAlreadyAssociatedWithRegulator() {
		String categoryType="Post License";
		String displayName="";//"vxv";
		Long regulatorId=2L;
		
		boolean rc = regulatorCategoryRepository.isRegulatorCategoryTypeAlreadyAssociatedWithRegulator(2L,categoryType, regulatorId);
		System.out.println("..........");
	}
	
	@Test
	public void regulatorCategory_should_findAll_OrderByDisplayName_Asc(){
		List<RegulatorCategory> rc = regulatorCategoryRepository.findAllByOrderByDisplayNameAsc();
		System.out.println("..........");
	}
	
	
}
