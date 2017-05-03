package com.softech.vu360.lms.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.util.UserPermissionChecker;

@Component
public class ManagerHelper {

	@Autowired
	private SecurityAndRolesService _securityAndRoleService;
	
	private static SecurityAndRolesService securityAndRoleService;
	
	@PostConstruct
	private void init() {
		ManagerHelper.securityAndRoleService = this._securityAndRoleService;
	}
	
	public static String getManagerURL(HttpServletRequest request) {
		
		String redirectURL = null;
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<String> disabledFeatureCodes = new ArrayList<>();

		/*
		 *
		 * For users seamlessly landing from Cart (Storefront) 
		 * to My Courses page are not getting through from
		 * interceptor.do where disabled features are being
		 * setting up in user session
		 * 
		 * In such case, an explicit call to get disabled
		 * features is now being called here
		 * 
		 * */
		if(request.getSession().getAttribute("DISABLED_FEATURE_CODES") == null)
			UserPermissionChecker.setDisabledLmsFeatureCodesAndGroupsForUser(user, request.getSession());
			
		disabledFeatureCodes.addAll((Set<String>)request.getSession().getAttribute("DISABLED_FEATURE_CODES"));

		String enabledFeatureCode = securityAndRoleService.getAnyEnabledFeatureCodeInDisplayOrderByRoleType(user.getId(), LMSRole.ROLE_TRAININGMANAGER, disabledFeatureCodes);
		switch(enabledFeatureCode) {
			case "AssignTrainingPlans":
				redirectURL = "/mgr_assignTraningPlan.do";
				break;
			case "LMS-MGR-0029":
				redirectURL = "/mgr_learnerEnrollments.do?method=showSearchLearnerPage";
				break;
			case "LMS-MGR-0001":
				redirectURL = "/mgr_manageLearners.do";
				break;
			case "LMS-MGR-0002":
				redirectURL = "/mgr_batchImportLearners.do";
				break;
			case "LMS-MGR-0003":
				redirectURL = "/mgr_regInvitation-1.do";
				break;
			case "LMS-MGR-0004":
				redirectURL = "/mgr_manageOrganizationGroup.do";
				break;
			case "LMS-MGR-0005":
				redirectURL = "/mgr_manageLearnerGroups.do";
				break;
			case "LMS-MGR-0006":
				redirectURL = "/mgr_manageSecurityRoles.do?method=showSecurityRoles";
				break;
			case "LMS-MGR-0007":
				redirectURL = "/mgr_viewAssignSecurityRoleMain.do";
				break;
			case "LMS-MGR-0008":
				redirectURL = "/mgr_viewPlanAndEnroll.do";
				break;
			case "LMS-MGR-0009":
				redirectURL = "/mgr_searchTrainingPlans.do";
				break;
			case "LMS-MGR-0010":
				redirectURL = "/mgr_manageSynchronousCourse.do";
				break;
			case "LMS-MGR-0011":
				redirectURL = "/mgr_viewAllEntitlements.do";
				break;
			case "LMS-MGR-0012":
				redirectURL = "/mgr_ManageReports.do";
				break;
			case "LMS-MGR-0013":
				redirectURL = "/mgr_ManageReports.do";
				break;
			case "LMS-MGR-0014":
				redirectURL = "/mgr_ManageReports.do";
				break;
			case "LMS-MGR-0015":
				redirectURL = "/mgr_ManageReports.do";
				break;
			case "LMS-MGR-0032":
				redirectURL = "/mgr_ManageReports.do";
				break;
			case "LMS-MGR-0017":
				redirectURL = "/mgr_alertCourse.do";
				break;
			case "LMS-MGR-0018":
				redirectURL = "/mgr_sendMailToLearners.do";
				break;
			case "LMS-MGR-0019":
				redirectURL = "/mgr_viewAssignSurveyMain.do";
				break;
			case "LMS-MGR-0020":
				redirectURL = "/mgr_manageSurveys.do";
				break;
			case "LMS-MGR-0023":
				redirectURL = "/mgr_editCustomer.do?method=editCustomerProfile";
				break;
			case "LMS-MGR-0024":
				redirectURL = "/mgr_editCustomer.do?method=editCustomerPreferences";
				break;
			case "LMS-MGR-0025":
				redirectURL = "/mgr_ViewEnrollmentForCertEnableDisable.do?method=searchLearner";
				break;
		}
		return redirectURL;
	}
	
}
