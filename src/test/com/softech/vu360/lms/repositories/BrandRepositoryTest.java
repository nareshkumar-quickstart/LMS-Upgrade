package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.repositories.BrandRepository;
import com.softech.vu360.lms.repositories.LearnerRepository;

public class BrandRepositoryTest extends SpringJUnitConfigAbstractTest{

	@Inject
	private BrandRepository brandRepository;
	
	//@PersistenceContext
	//protected EntityManager entityManager;
	
	
	//@Test
	public void brand_should_getByName() {
		Brand brand = brandRepository.findByBrandNameIgnoreCase("accreditation");
		System.out.println("Brand : ID = "+brand.getId()+" Name = "+brand.getBrandName());
	}
	
	//@Test
	public void brand_should_getById() {
		Brand brand = brandRepository.findOne(16L);
		System.out.println("Brand : ID = "+brand.getId()+" Name = "+brand.getBrandName());
	}
	
	//@Test
	public void brand_should_getAllBrand() {
		Iterable<Brand> brandList = brandRepository.findAll();
		System.out.println("BrandList contains = "+((Collection<Brand>)brandList).size());
	}
	
	//@Test
	public void brand_should_deleteByName() {
		brandRepository.deleteByBrandNameIgnoreCase("1nov");
		Brand deleted=brandRepository.findOne(183L);
		if(deleted==null){
			System.out.println("Brand deleted");
		}
	}
	
	@Test
	public void brand_should_save(){

		brandRepository.save("Test_JPA_BrandName_1", "Test_JPA_BrandKey_2");
	}
	
	
}
