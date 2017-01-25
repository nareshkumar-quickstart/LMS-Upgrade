package com.softech.vu360.lms.service;

import java.util.Hashtable;

import com.softech.vu360.lms.model.SelfServiceProductDetails;

/**
 * @author rehan.rana
 * 
 */
public interface SelfServiceService {

	public SelfServiceProductDetails saveSelfServiceProductDetails(SelfServiceProductDetails productDetail);
	public boolean createFreemiumAccount(Hashtable<String, Object> accountdetail);
	public boolean processSelfServiceOrder(Hashtable<String, Object> accountdetail);
	public boolean insertCourseLicenseByFreemium(Hashtable<String, Object> accountdetail);
	public boolean insertSceneTemplateByFreemium(Hashtable<String, Object> accountdetail);
	// LMS-15514 - Updating DistributorCode in Distributor table. This function call when SF create Store on their end. Store Id is same as DistributorCode in LMS.
	public boolean storeCreation(Hashtable<String, Object> accountdetail);
	boolean insertAuthorRightsByFreemium(Hashtable<String, Object> accountdetail);	
	boolean insertCourseRightsByFreemium(Hashtable<String, Object> accountdetail);
	boolean insertCourseRightsByFreemiumV2(Hashtable<String, Object> accountdetail);
	public boolean processLMSProductPurchase(Hashtable<String, Object> dataBundle, long lmsProductCode);
}
