package com.softech.vu360.lms.util;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;

public class UserPermissionChecker {

	public static final String DISABLED_FEATURE_CODES = "DISABLED_FEATURE_CODES";
	public static final String DISABLED_FEATURE_GROUPS = "DISABLED_FEATURE_GROUPS";
	private static final Logger log = Logger.getLogger(UserPermissionChecker.class);
	
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
		Authentication authenticatedUser;
		VU360UserAuthenticationDetails authenticatedUserDetails;
		VU360UserMode userMode;
		
		boolean hasAccess = false;
		
		try
		{
			log.info("\t ---------- START - hasAccessToFeatureGroup : " + UserPermissionChecker.class.getName() + " ---------- ");
			log.info("featureGroup = " + featureGroup);
			
			authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
			authenticatedUserDetails = (VU360UserAuthenticationDetails)authenticatedUser.getDetails();
			userMode = authenticatedUserDetails.getCurrentMode();
			
			if(userMode == null) {
				userMode = (VU360UserMode)session.getAttribute("clusteredCurrentMode");
			}
			
			log.info("disabledFeatureGroups " + (getDisabledFeatureGroups(session) == null ? "is null" 
					: "size = " + getDisabledFeatureGroups(session).size()));
			/*
			 * User having multiple roles, like Administrator, Manager & Learner
			 * and those roles have a feature group with the same name. For instance, 
			 * 'Tools' feature group exists in both Administrator and Manager. If
			 * all features of Tools disabled on Administrator-level, 'Tools' 
			 * remain accessible from Manager mode.
			 * 
			 * Due to which feature groups are now kept along with role type. 
			 * For example, if a user has Administrator, Manager & Learner 
			 * roles (modes), when the user logs in all its disabled feature groups
			 * are being kept in session and to distinguish which feature group
			 * belongs to which role type each feature group is now being concatenate 
			 * with relevant role. 
			 * 
			 * If you debug and inspect session.getAttribute(DISABLED_FEATURE_CODES)
			 * for disabled feature groups, you will find feature group concatenated
			 * with role type, for example ROLE_LMSADMINISTRATOR:Tools, 
			 * ROLE_TRAININGADMINISTRATOR:Tools. This means the user has Tools 
			 * disabled on both Administrator and Manager mode.
			 * 
			 * */
			if (getDisabledFeatureGroups(session) == null || !getDisabledFeatureGroups(session).contains(
					new StringBuilder().append(userMode.toString()).append(":").append(featureGroup).toString()))
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
