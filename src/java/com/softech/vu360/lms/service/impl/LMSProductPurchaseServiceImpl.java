package com.softech.vu360.lms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.model.HomeWorkAssignmentAsset;
import com.softech.vu360.lms.model.LMSProductCourseType;
import com.softech.vu360.lms.model.LMSProductPurchase;
import com.softech.vu360.lms.model.LMSProducts;
import com.softech.vu360.lms.repositories.HomeWorkAssignmentAssetRepository;
import com.softech.vu360.lms.repositories.LMSProductCourseTypeRepository;
import com.softech.vu360.lms.repositories.LMSProductPurchaseRepository;
import com.softech.vu360.lms.repositories.LMSProductsRepository;
import com.softech.vu360.lms.service.LMSProductPurchaseService;

public class LMSProductPurchaseServiceImpl implements LMSProductPurchaseService{
	
	@Autowired
	LMSProductsRepository lMSProductsRepository;
	
	@Autowired
	LMSProductPurchaseRepository lMSProductPurchaseRepository;
	
	@Autowired
	HomeWorkAssignmentAssetRepository homeWorkAssignmentAssetRepository;
	
	@Autowired
	LMSProductCourseTypeRepository lMSProductCourseTypeRepository;
	 
	@Override
	public LMSProductPurchase savePurchaseProduct(LMSProductPurchase lmsProductPurchase) {
		return lMSProductPurchaseRepository.save(lmsProductPurchase);
	}
	
	@Override
	public void deleteHomeWorkAssignmentAsset(long homeWorkAssignmentAssetid) {
		homeWorkAssignmentAssetRepository.deleteById(homeWorkAssignmentAssetid);
	}

	@Override
	public LMSProducts getlmsProduct(long id) {
		return lMSProductsRepository.findOne(id);
	}

	@Override
	public List<LMSProductCourseType> getCustomerCourseTypes(long customerid) {
		return lMSProductCourseTypeRepository.findByLmsProductIdByCustomerId(customerid);
	}
	
	@Override
	public LMSProducts findLMSProductsPurchasedByCustomer(long productId, long customerId) {
		LMSProductPurchase objPP = lMSProductPurchaseRepository.findTop1ByCustomerIdAndLmsProductId(productId, customerId) ;
		if(objPP!=null)
			return objPP.getLmsProduct();
		else
			return null;
	}
	
	@Override
	public HomeWorkAssignmentAsset saveHomeWorkAssignmentAsset(HomeWorkAssignmentAsset homeWorkAssignmentAsset){
		return homeWorkAssignmentAssetRepository.save(homeWorkAssignmentAsset);
	}
		
	@Override
	public List<HomeWorkAssignmentAsset> getHomeWorkAssignmentAsset(long courseid){
		return homeWorkAssignmentAssetRepository.findByHomeWorkAssignmentCourseId(courseid);
	}

}
