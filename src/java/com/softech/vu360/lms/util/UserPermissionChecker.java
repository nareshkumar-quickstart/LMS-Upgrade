package com.softech.vu360.lms.util;

import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class UserPermissionChecker {

	public static String DISABLED_FEATURE_CODES = "DISABLED_FEATURE_CODES";
	public static String DISABLED_FEATURE_GROUPS = "DISABLED_FEATURE_GROUPS";
	private static Logger log = Logger.getLogger(UserPermissionChecker.class);
	
	@SuppressWarnings("unchecked")
	private static Set<String> getDisabledFeatureGroups (HttpSession session)
	{
		return (Set<String>) session.getAttribute(DISABLED_FEATURE_GROUPS);
	}
	
	@SuppressWarnings("unchecked")
	private static Set<String> getDisabledFeatureCodes (HttpSession session)
	{
		return (Set<String>) session.getAttribute(DISABLED_FEATURE_CODES);
	}

	public static boolean hasAccessToFeatureGroup(String featureGroup, com.softech.vu360.lms.vo.VU360User loggedInUser, HttpSession session) 
	{
		try
		{
			log.info("\t ---------- START - hasAccessToFeatureGroup : " + UserPermissionChecker.class.getName() + " ---------- ");
			log.info("featureGroup = " + featureGroup);
			boolean hasAccess = false;
			log.info("disabledFeatureGroups " + (getDisabledFeatureGroups(session) == null ? "is null" 
					: "size = " + getDisabledFeatureGroups(session).size()));
			if(getDisabledFeatureGroups(session) == null || !getDisabledFeatureGroups(session).contains(featureGroup))
			{
				log.info("logInAsManagerRole = " + loggedInUser.getLogInAsManagerRole());
				if(loggedInUser.getLogInAsManagerRole() != null)
					hasAccess = hasAccessToFeatureGroup(loggedInUser.getLogInAsManagerRole().getLmsPermissions(), featureGroup);
				else
				{
					for (com.softech.vu360.lms.vo.LMSRole role : loggedInUser.getLmsRoles()) 
					{
						log.info("role name = " + role.getRoleName());
						hasAccess = hasAccessToFeatureGroup(role.getLmsPermissions(), featureGroup);
						log.info("\t >>> Has " + (hasAccess ? "" : "Not ") + "Access");
						if(hasAccess)
							break;
					}
				}
			}
			log.info("hasAccess = " + hasAccess);
			return hasAccess;
		}
		catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		finally
		{
			log.info("\t ---------- END - hasAccessToFeatureGroup : " + UserPermissionChecker.class.getName() + " ---------- ");
		}
	}
	
	private static boolean hasAccessToFeatureGroup (List<com.softech.vu360.lms.vo.LMSRoleLMSFeature> permissions, String featureGroup)
	{
		boolean hasAccess = false;
		for (com.softech.vu360.lms.vo.LMSRoleLMSFeature permission : permissions) 
		{
			log.debug("\tFG = " + permission.getLmsFeature().getFeatureGroup() + " , enabled = " + permission.getEnabled());
			if (permission.getLmsFeature().getFeatureGroup().equals(featureGroup) && permission.getEnabled())
			{
				hasAccess = true;
				break;
			}
		}
		return hasAccess;
	}
	
	private static boolean hasAccessToFeatureCode (List<com.softech.vu360.lms.vo.LMSRoleLMSFeature> permissions, String featureCode)
	{
		boolean hasAccess = false;
		for (com.softech.vu360.lms.vo.LMSRoleLMSFeature permission : permissions) 
		{
			log.debug("\tFG : FC = " + permission.getLmsFeature().getFeatureGroup() + " : " + permission.getLmsFeature().getFeatureCode());
			if (permission.getLmsFeature().getFeatureCode().equals(featureCode) && permission.getEnabled()) 
			{
				hasAccess = true;
				break;
			}
		}	
		return hasAccess;
	}

	public static boolean hasAccessToFeature(String featureCode, com.softech.vu360.lms.vo.VU360User loggedInUser, HttpSession session) 
	{
		try
		{
			log.info("\t ---------- START - hasAccessToFeatureCode : " + UserPermissionChecker.class.getName() + " ---------- ");
			log.info("featureCode = " + featureCode);
			boolean hasAccess = false;
			if(getDisabledFeatureCodes(session) == null || !getDisabledFeatureCodes(session).contains(featureCode))
			{
				log.info("logInAsManagerRole = " + loggedInUser.getLogInAsManagerRole());
				if(loggedInUser.getLogInAsManagerRole() != null)
					hasAccess = hasAccessToFeatureCode(loggedInUser.getLogInAsManagerRole().getLmsPermissions(), featureCode);
				else
				{
					for (com.softech.vu360.lms.vo.LMSRole role : loggedInUser.getLmsRoles()) 
					{
						log.info("role name = " + role.getRoleName());
						hasAccess = hasAccessToFeatureCode(role.getLmsPermissions(), featureCode);
						if(hasAccess)
							break;
					}
				}
			}
			log.info("hasAccess = " + hasAccess);
			return hasAccess;
		}
		catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		finally
		{
			log.info("\t ---------- END - hasAccessToFeatureCode : " + UserPermissionChecker.class.getName() + " ---------- ");
		}
	}

}
