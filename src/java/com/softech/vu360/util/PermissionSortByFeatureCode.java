package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.LMSRoleLMSFeature;


public class PermissionSortByFeatureCode implements Comparator<LMSRoleLMSFeature> {

	@Override
	public int compare(LMSRoleLMSFeature arg0, LMSRoleLMSFeature arg1) {
		// TODO Auto-generated method stub
		//return arg0.get.compareTo(arg1.getName());
		//if(arg0.getLmsFeature().getLmsMode().compareTo(arg1.getLmsFeature().getLmsMode())!=0)
			//return arg0.getLmsFeature().getLmsMode().compareTo(arg1.getLmsFeature().getLmsMode());
		if(arg0.getLmsFeature().getFeatureGroup().compareTo(arg1.getLmsFeature().getFeatureGroup())!=0)
			return arg0.getLmsFeature().getFeatureGroup().compareTo(arg1.getLmsFeature().getFeatureGroup());
		if(arg0.getLmsFeature().getFeatureName().compareTo(arg1.getLmsFeature().getFeatureName())!=0)
			return arg0.getLmsFeature().getFeatureName().compareTo(arg1.getLmsFeature().getFeatureName());
		return 0;
	}

}
