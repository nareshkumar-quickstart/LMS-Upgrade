package com.softech.vu360.lms.service;

import java.util.List;

import com.softech.vu360.lms.model.HomeWorkAssignmentAsset;
import com.softech.vu360.lms.model.LMSProductCourseType;
import com.softech.vu360.lms.model.LMSProductPurchase;
import com.softech.vu360.lms.model.LMSProducts;

public interface LMSProductPurchaseService {
	public LMSProductPurchase savePurchaseProduct(LMSProductPurchase lmsProductPurchase);
	public HomeWorkAssignmentAsset saveHomeWorkAssignmentAsset(HomeWorkAssignmentAsset homeWorkAssignmentAsset);
	public List<HomeWorkAssignmentAsset> getHomeWorkAssignmentAsset(long courseid);
	public void deleteHomeWorkAssignmentAsset(long homeWorkAssignmentAssetid);
	public LMSProducts getlmsProduct(long id);
	public List<LMSProductCourseType> getCustomerCourseTypes(long customerid);
	public LMSProducts findLMSProductsPurchasedByCustomer(long productId,long customerId);
}
