package com.softech.vu360.lms.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.ILMSFeaturePermission;
import com.softech.vu360.lms.model.LMSFeature;

/**
 * Comparator class to sort {@link ILMSFeaturePermission} collections by 
 * {@link LMSFeature#getFeatureGroup()} and 
 * {@link LMSFeature#getFeatureName()}
 * @author sm.humayun
 * @since 4.13 {LMS-8108}
 */
public class LMSFeaturePermissionComparator implements Comparator<ILMSFeaturePermission> {

	/**
	 * Compare {@link ILMSFeaturePermission} by
	 * {@link LMSFeature#getFeatureGroup()} and
	 * {@link LMSFeature#getFeatureName()}
	 * @see {@link Comparator#compare(Object, Object)}
	 */
	@Override
	public int compare(ILMSFeaturePermission arg0, ILMSFeaturePermission arg1){
		int ret = arg0.getLmsFeature().getDisplayOrder().compareTo(arg1.getLmsFeature().getDisplayOrder());
		if(ret == 0)
			ret = arg0.getLmsFeature().getFeatureName().compareTo(arg1.getLmsFeature().getFeatureName());
		if(ret == 0)
			ret = -1; //in case of tree set zero value will over ride the root node
		return ret;
	}

}
