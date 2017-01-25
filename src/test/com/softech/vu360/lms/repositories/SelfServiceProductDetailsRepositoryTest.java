package com.softech.vu360.lms.repositories;

import java.util.Date;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.SelfServiceProductDetails;

public class SelfServiceProductDetailsRepositoryTest extends
		SpringJUnitConfigAbstractTest {

	@Inject
	private SelfServiceProductDetailsRepository selfServiceProductDetailsRepository;

	//@Test
	public void save() {

		SelfServiceProductDetails prodDetails = selfServiceProductDetailsRepository
				.findOne(856l);
		SelfServiceProductDetails clone = new SelfServiceProductDetails();
		clone.setActualproductId(prodDetails.getActualproductId());
		clone.setActualProductType(prodDetails.getActualProductType());
		clone.setEffectiveFrom(prodDetails.getEffectiveFrom());
		clone.setEffectiveTo(prodDetails.getEffectiveTo());
		clone.setExernalOrginizationId(prodDetails.getExernalOrginizationId());
		clone.setProductCategory(prodDetails.getProductCategory());
		clone.setProductCode(prodDetails.getProductCode());
		clone.setProductType(prodDetails.getProductType());
		clone.setPurchaseDate(prodDetails.getPurchaseDate());
		clone.setUsername(prodDetails.getUsername() + "_clone");
		selfServiceProductDetailsRepository.save(clone);
	}
	
	//@Test
	public void createFreemiumAccount(){
		
		selfServiceProductDetailsRepository.createFreemiumAccount
				("Test", "Test2", "remove@remove.com", "remove", "remove", 
						"remove", new Date(), new Date(), 10, 	10);
		
	}
	
	//@Test
	public void insertCourseLicenseByFreemium() {
		selfServiceProductDetailsRepository.insertAuthorRightsByFreemium("RemoveThis2", 8);
		//System.out.println(bool);
	}
	
	@Test
	public void insertAuthorRightsByFreemium() {
		selfServiceProductDetailsRepository.insertCourseLicenseByFreemium("Remove3");
		//System.out.println(i);
	}
	
	//@Test
	public void insertCoursePublishRightsByFreemium() {
		selfServiceProductDetailsRepository.insertCoursePublishRightsByFreemium("Remove4", 9);
	}
	
	//@Test
	public void insertSceneTemplateByFreemium() {
		selfServiceProductDetailsRepository.insertSceneTemplateByFreemium("Remove 5", "Remove 6", new Date(), new Date());
	}
	
	//@Test
	public void insertSceneTemplateByFreemiumV2() {
		selfServiceProductDetailsRepository.insertSceneTemplateByFreemiumV2("RemoveUName", "TestTemplate", new Date(), new Date());
	}

}
