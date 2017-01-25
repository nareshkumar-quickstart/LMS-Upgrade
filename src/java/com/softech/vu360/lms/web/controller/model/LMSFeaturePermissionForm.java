package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.ILMSFeaturePermission;
import com.softech.vu360.lms.util.LMSFeaturePermissionComparator;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * Helper class to extract {@link List} of group level
 * {@link ILMSFeaturePermission} permissions and {@link List} of 
 * {@link ILMSFeaturePermission} against a group level feature
 * @author sm.humayun
 * @since 4.13 {LMS-8108}
 */
public class LMSFeaturePermissionForm  implements ILMSBaseInterface
{
	private static final long serialVersionUID = 1L;
	/**
	 * Feature by feature groups
	 */
	private Map<String, List<ILMSFeaturePermission>> featuresByFeatureGroups;
	
	/**
	 * Sorted feature groups
	 */
	private Set<String> sortedFeatureGroups;

	/**
	 * Logger
	 */
	private static final Logger log = Logger.getLogger(LMSFeaturePermissionForm.class.getName());

	/**
	 * Instantiate with given {@link List} of {@link ILMSFeaturePermission}
	 * @param lmsFeaturePermissions
	 */
	public LMSFeaturePermissionForm (
			List<? extends ILMSFeaturePermission> lmsFeaturePermissions)
	{
		this.processFeaturePermissions(lmsFeaturePermissions);
	}

	/**
	 * Sort permissions by feature groups
	 */
	private void processFeaturePermissions (List<? extends ILMSFeaturePermission> lmsFeaturePermissions)
	{
		try
		{
			log.debug(" ---------- processFeaturePermissions() : START --------------- ");
			featuresByFeatureGroups = new HashMap<String, List<ILMSFeaturePermission>>();
			Set<ILMSFeaturePermission> set = new TreeSet<ILMSFeaturePermission>(new LMSFeaturePermissionComparator());
			this.sortedFeatureGroups = new LinkedHashSet<String>();
			for(ILMSFeaturePermission permission : lmsFeaturePermissions)
			{
				log.debug("permission code - group - order = " + permission.getLmsFeature().getFeatureCode() + " - " 
						+ permission.getLmsFeature().getFeatureGroup() + " - " + permission.getLmsFeature().getDisplayOrder());
				if(featuresByFeatureGroups.get(permission.getLmsFeature().getFeatureGroup()) == null)
				{
					featuresByFeatureGroups.put(permission.getLmsFeature().getFeatureGroup(), new ArrayList<ILMSFeaturePermission>());
					log.debug("featuresByFeatureGroups.size = " + featuresByFeatureGroups.size());
					log.debug("adding permission to set = " + set.add(permission));
					log.debug("set.size = " + set.size());
				}
				featuresByFeatureGroups.get(permission.getLmsFeature().getFeatureGroup()).add(permission);
				log.debug("features in feature group = " + featuresByFeatureGroups.get(permission.getLmsFeature().getFeatureGroup()).size());
			}
			Iterator<ILMSFeaturePermission> iter = set.iterator();
			while(iter.hasNext())
			{
				this.sortedFeatureGroups.add(iter.next().getLmsFeature().getFeatureGroup());
			}
		}
		finally
		{
			log.debug(" ---------- processFeaturePermissions() : END --------------- ");
		}
	}
	
	/**
	 * Return {@link List} of group level {@link ILMSFeaturePermission}
	 * @return {@link List} of group level {@link ILMSFeaturePermission}
	 */
	public Set<String> getFeatureGroups()
	{
		return this.sortedFeatureGroups;
	}
	
	/**
	 * Return {@link List} of {@link ILMSFeaturePermission} against given 
	 * <code>featureGroup</code>
	 * @param featureGroup
	 * @return {@link List} of {@link ILMSFeaturePermission} against given 
	 * <code>featureGroup</code>
	 */
	public List<ILMSFeaturePermission> getFeatures (String featureGroup)
	{
		return this.featuresByFeatureGroups.get(featureGroup);
	}

}
