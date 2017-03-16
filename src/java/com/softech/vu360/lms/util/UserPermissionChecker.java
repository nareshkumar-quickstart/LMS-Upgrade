package com.softech.vu360.lms.util;

import java.util.Set;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.softech.vu360.lms.service.VU360UserService;

@Component
public class UserPermissionChecker {

	private static 
	VU360UserService vu360UserService;

	@Autowired
	VU360UserService _vu360UserService;
	
	@PostConstruct     
	public void initServices () {
		vu360UserService = this._vu360UserService;
	}
	
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
					hasAccess = vu360UserService.hasAccessToFeatureGroup(loggedInUser.getId(), loggedInUser.getLogInAsManagerRole().getId(), featureGroup);
				else
				{
					hasAccess = vu360UserService.hasAccessToFeatureGroup(loggedInUser.getId(), featureGroup);
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
					hasAccess = vu360UserService.hasAccessToFeatureCode(loggedInUser.getId(), loggedInUser.getLogInAsManagerRole().getId(), featureCode);
				else
				{
					hasAccess = vu360UserService.hasAccessToFeatureCode(loggedInUser.getId(), featureCode);
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
