package com.softech.vu360.lms.webservice.client;

import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfiguration;

public abstract class LCMSClientWS implements ClientAbstractWS{

	public abstract boolean uploadAsset(String fileName, byte[] bytes);
	public abstract long saveAsset(Asset asset, boolean fileUploaded, long loggedInUserID);
	public abstract int updateAssetStatus(long[] assetIds, boolean active, long loggedInUserID);
	public abstract boolean invokeCourseConfigurationUpdated(CourseConfiguration couseCourseConfiguration);
	public abstract boolean invokeCourseApprovalUpdated(CourseApproval courseApproval);
	public abstract boolean invalidateBrandCache(String brandName, String language);
	public abstract byte[] downloadAssetStatus(long assetId);
	
	
	/*
	public static LCMSClientWS getDefaultClient(){
		return new LCMSClientWSImpl();
	}
	*/

}
