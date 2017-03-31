package com.softech.vu360.lms.web.controller.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.util.UserPermissionChecker;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class Menu implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private String url = null ;
	private String featureGroup = null ;
	private String feature = null ;
	Logger log=Logger.getLogger(Menu.class);
	boolean redirectingToLearner=false;
	boolean redirectingToManager=false;
	boolean redirectingToAdmin=false;
	boolean redirectingToRegulatory=false;
	
	public  Menu(com.softech.vu360.lms.vo.VU360User user, HttpServletRequest request) 
	{	
		boolean setup=false;
		
		if(user.isLearnerMode())
		{
			HttpSession session = request.getSession();
			//log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + session.getAttribute("isAdminSwitch"));
			//log.error("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + session.getAttribute("isAdminSwitch"));

			if(session.getAttribute("isAdminSwitch")==null){
					setup=setupLearner(user,request);
			}
			redirectingToLearner=true;
		}
		if(!setup && user.isTrainingAdministrator())
		{
			setup=setupManager(user,request);
			redirectingToManager=true;
		}
		if(!setup && user.isLMSAdministrator())
		{
			setup=setupAdmin(user,request);
			redirectingToAdmin=true;;
		}
		if(!setup && user.isRegulatoryAnalyst())
		{
			setup=setupRegulatory(user,request);
			redirectingToRegulatory=true;
			HttpSession session = request.getSession();
			session.removeAttribute("isAdminSwitch");
		}
	}
	public boolean redirectingToLearner()
	{
		return redirectingToLearner;
	}
	public boolean redirectingToManager()
	{
		return redirectingToManager;
	}
	public boolean redirectingToAdmin()
	{
		return redirectingToAdmin;
	}
	public boolean setupAdmin(com.softech.vu360.lms.vo.VU360User user,HttpServletRequest request)
	{
		boolean setupResult=false;
		if (UserPermissionChecker.hasAccessToFeatureGroup("Customers", user, request.getSession(true))) 
		{
			setupResult=true;
			this.featureGroup = "Customers";
			if(UserPermissionChecker.hasAccessToFeature("LMS-ADM-0013", user, request.getSession(true))) 
			{
				this.url = "lms_handleMenu.do?featureGroup=Customers&feature=LMS-ADM-0013&actionUrl=adm_editCustomer.do?method=editCustomerProfile";
				this.feature = "LMS-ADM-0013";
				setupResult=true;
			}  else if(UserPermissionChecker.hasAccessToFeature("ManageCustomers", user, request.getSession(true))) 
			{
				this.url = "lms_handleMenu.do?featureGroup=Customers&feature=ManageCustomers&actionUrl=adm_addCustomer.do";
				this.feature = "LMS-ADM-0012";
				setupResult=true;
			} else if(UserPermissionChecker.hasAccessToFeature("LMS-ADM-0014", user, request.getSession(true))) 
			{
				this.url = "lms_handleMenu.do?featureGroup=Customers&feature=LMS-ADM-0014&actionUrl=adm_editCustomer.do?method=editCustomerPreferences";
				this.feature = "LMS-ADM-0014";
				setupResult=true;
			} else if(UserPermissionChecker.hasAccessToFeature("LMS-ADM-0015", user, request.getSession(true))) 
			{
				this.url = "lms_handleMenu.do?featureGroup=Customers&feature=LMS-ADM-0015&actionUrl=adm_SearchEntitlements.do";
				this.feature = "LMS-ADM-0015";
				setupResult=true;
			}
			else if(UserPermissionChecker.hasAccessToFeature("LMS-ADM-0017", user, request.getSession(true))) {
				this.url = "lms_handleMenu.do?featureGroup=Customers&feature=LMS-ADM-0017&actionUrl=adm_manageCustomField.do?method=showCustomField&entity=customer";
				this.feature = "LMS-ADM-0017";
				setupResult=true;
			}
		}
		/*****************************For Admin Distributors************************************/
		else if (UserPermissionChecker.hasAccessToFeatureGroup("Resellers", user, request.getSession(true))) 
		{
			this.featureGroup = "Resellers";
			setupResult=true;
			if(UserPermissionChecker.hasAccessToFeature("LMS-ADM-0019", user, request.getSession(true))) 
			{
				this.url = "lms_handleMenu.do?featureGroup=Resellers&feature=LMS-ADM-0019&actionUrl=adm_editDistributorProfile.do";
				this.feature = "LMS-ADM-0019";
				setupResult=true;
			}  
			else if(UserPermissionChecker.hasAccessToFeature("ManageDistributors", user, request.getSession(true))) 
			{
				this.url = "lms_handleMenu.do?featureGroup=Resellers&feature=ManageDistributors&actionUrl=adm_addDistributor.do";
				this.feature = "ManageDistributors";
				setupResult=true;
			} 
			else if(UserPermissionChecker.hasAccessToFeature("LMS-ADM-0020", user, request.getSession(true))) 
			{
				this.url = "lms_handleMenu.do?featureGroup=Resellers&feature=LMS-ADM-0020&actionUrl=adm_editDistributorPreferences.do";
				this.feature = "LMS-ADM-0020";
				setupResult=true;
			} 
			else if(UserPermissionChecker.hasAccessToFeature("LMS-ADM-0027", user, request.getSession(true))) 
			{
				this.url = "lms_handleMenu.do?featureGroup=Resellers&feature=LMS-ADM-0027&actionUrl=adm_manageDistributorGroup.do";
				this.feature = "LMS-ADM-0027";
				setupResult=true;
			} 
			else if(UserPermissionChecker.hasAccessToFeature("LMS-ADM-0022", user, request.getSession(true))) 
			{
				this.url = "lms_handleMenu.do?featureGroup=Resellers&feature=LMS-ADM-0022&actionUrl=adm_distributorEntitlements.do";
				this.feature = "LMS-ADM-0022";
				setupResult=true;
			}
			else if(UserPermissionChecker.hasAccessToFeature("LMS-ADM-0029", user, request.getSession(true))) 
			{
				this.url = "lms_handleMenu.do?featureGroup=Resellers&feature=LMS-ADM-0029&actionUrl=adm_manageCustomField.do?method=showCustomField&entity=reseller";
				this.feature = "LMS-ADM-0029";
				setupResult=true;
			}
		}
		/*****************************For Admin Security************************************/
		else if (UserPermissionChecker.hasAccessToFeatureGroup("Security", user, request.getSession(true))) 
		{
			this.featureGroup = "Security";
			setupResult=true;
			if(UserPermissionChecker.hasAccessToFeature("LMS-ADM-0011", user, request.getSession(true))) 
			{
				this.url = "lms_handleMenu.do?featureGroup=Security&feature=LMS-ADM-0011&actionUrl=mgr_manageSecurityRoles.do?method=showSecurityRoles";
				this.feature = "LMS-ADM-0011";
				setupResult=true;
			} 
		}
		return setupResult;
	}
	/* TODO there shouldn't be any separate entry for FeatureGroup, it should be for Feature along FeatureGrooup.
	 * and in displaying the feature we shouldn't display radio button infront of group level.
	 */
	public boolean setupManager(com.softech.vu360.lms.vo.VU360User user, HttpServletRequest request)
	{
		boolean setupResult=false;
		String clickedFeatureGroup="";
		HttpSession session = request.getSession();
		if(session.getAttribute("featureGroup")!=null){
			clickedFeatureGroup=session.getAttribute("featureGroup").toString();
			if(clickedFeatureGroup.equalsIgnoreCase("My Courses")||clickedFeatureGroup.equalsIgnoreCase("Users & Groups")){
				clickedFeatureGroup="";
			}
		}
		
		if (UserPermissionChecker.hasAccessToFeatureGroup("Profile", user, request.getSession(true))&&((clickedFeatureGroup.equals("Profile")))||(StringUtils.isEmpty(clickedFeatureGroup))) 
		{
			
			this.featureGroup = "Profile";
			if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0023", user, request.getSession(true))){
				this.feature = "LMS-MGR-0023";
				this.url = "lms_handleMenu.do?featureGroup="+featureGroup+"&feature="+feature+"&actionUrl=mgr_editCustomer.do?method=editCustomerProfile";
				setupResult=true;
			}
			else
			if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0024", user, request.getSession(true))){
				this.feature = "LMS-MGR-0024";
				this.url = "lms_handleMenu.do?featureGroup="+featureGroup+"&feature="+feature+"&actionUrl=mgr_editCustomer.do?method=editCustomerPreferences";
				setupResult=true;
			}
			
		}
		if (UserPermissionChecker.hasAccessToFeatureGroup("Tools", user, request.getSession(true))&&((clickedFeatureGroup.equals("Tools")))||(StringUtils.isEmpty(clickedFeatureGroup))) 
		{
			setupResult=true;
			this.featureGroup = "Tools";
			this.url = "lms_handleMenu.do?featureGroup=Tools&feature=LMS-ADM-0006&actionUrl=mgr_sendMailToLearners.do";
			this.feature = "LMS-ADM-0006";
		}
		if (UserPermissionChecker.hasAccessToFeatureGroup("Tools", user, request.getSession(true))&&((clickedFeatureGroup.equals("Tools")))||(StringUtils.isEmpty(clickedFeatureGroup))) 
		{
			this.featureGroup = "Tools";
			setupResult=true;
			if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0020", user, request.getSession(true))) 
			{
				setupResult=true;
				this.url = "lms_handleMenu.do?featureGroup=Tools&feature=LMS-MGR-0020&actionUrl=mgr_manageSurveys.do";
				this.feature = "LMS-MGR-0020";
			}  
			else if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0020", user, request.getSession(true))) 
			{
				setupResult=true;
				this.url = "lms_handleMenu.do?featureGroup=Tools&feature=LMS-MGR-0020&actionUrl=mgr_manageSurveys.do?method=learnerSurveyResponseView";
				this.feature = "LMS-MGR-0020";
			}
		}
		if (UserPermissionChecker.hasAccessToFeatureGroup("Reports", user, request.getSession(true))&&((clickedFeatureGroup.equals("Reports")))||(StringUtils.isEmpty(clickedFeatureGroup)))
		{
			setupResult=true;
			this.featureGroup = "Reports";
			this.url = "lms_handleMenu.do?featureGroup=Reports&feature=LMS-MGR-0012&actionUrl=mgr_ManageReports.do";
			this.feature = "LMS-MGR-0012";
		}
		/*
		if (UserPermissionChecker.hasAccessToFeatureGroup("ExternalServices", user, request.getSession(true))&&((clickedFeatureGroup.equals("ExternalServices")))||(StringUtils.isEmpty(clickedFeatureGroup)))
		{
			setupResult=true;
			this.featureGroup = "ExternalServices";
			this.url = "mgr_manageExternalServices.do";
			this.feature = "ExternalServices";
		}
		*/
		if (UserPermissionChecker.hasAccessToFeatureGroup("Plan & Enroll", user, request.getSession(true))&&((clickedFeatureGroup.equals("Plan & Enroll"))||(StringUtils.isEmpty(clickedFeatureGroup)))) 
		{
			this.featureGroup = "Plan & Enroll";
			if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0011", user, request.getSession(true))){
				this.feature = "LMS-MGR-0011";
				this.url = "lms_handleMenu.do?featureGroup="+featureGroup+"&feature="+feature+"&actionUrl=mgr_viewAllEntitlements.do";
				setupResult=true;
			}
			else
			if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0008", user, request.getSession(true))){
				this.feature = "LMS-MGR-0008";
				this.url = "lms_handleMenu.do?featureGroup="+featureGroup+"&feature="+feature+"&actionUrl=mgr_assignEnrollments.do";
				setupResult=true;
			}
			else
			if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0010", user, request.getSession(true))){
				this.feature = "LMS-MGR-0010";
				this.url = "lms_handleMenu.do?featureGroup="+featureGroup+"&feature="+feature+"&actionUrl=mgr_manageCustomCourses.do";
				setupResult=true;
			}
			else
			if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0009", user, request.getSession(true))){
				this.feature = "LMS-MGR-0009";
				this.url = "lms_handleMenu.do?featureGroup="+featureGroup+"&feature="+feature+"&actionUrl=mgr_searchTrainingPlans.do";
				setupResult=true;
			}
			else
			if(UserPermissionChecker.hasAccessToFeature("AssignTrainingPlans", user, request.getSession(true))){
				this.feature = "AssignTrainingPlans";
				this.url = "lms_handleMenu.do?featureGroup="+featureGroup+"&feature="+feature+"&actionUrl=mgr_assignTraningPlan.do";
				setupResult=true;
			}
		}
		
		if((UserPermissionChecker.hasAccessToFeatureGroup("Users & Groups", user, request.getSession(true))&&((clickedFeatureGroup.equals("Users & Groups"))||(StringUtils.isEmpty(clickedFeatureGroup)))) || user.isLMSAdministrator()) 
		{
			this.featureGroup = "Users & Groups";
			if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0001", user, request.getSession(true)) || user.isLMSAdministrator()) 
			{
				setupResult=true;
				this.feature="LMS-MGR-0001";
				this.url = "lms_handleMenu.do?featureGroup=Users%20%26%20Groups&feature=LMS-ADM-0001&actionUrl=mgr_manageLearners.do";
			}else if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0002", user, request.getSession(true)) || user.isLMSAdministrator()){
				setupResult=true;
				this.url = "lms_handleMenu.do?featureGroup=Users%20%26%20Groups&feature=LMS-MGR-0002&actionUrl=mgr_batchImportLearners.do";
				this.feature = "LMS-MGR-0002";
			}else if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0003", user, request.getSession(true)) || user.isLMSAdministrator()) {
				setupResult=true;
				this.url = "lms_handleMenu.do?featureGroup=Users%20%26%20Groups&feature=LMS-MGR-0003&actionUrl=mgr_regInvitation-1.do";
				this.feature = "LMS-MGR-0003";
			}else if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0004", user, request.getSession(true)) || user.isLMSAdministrator()) {
				setupResult=true;
				this.url = "lms_handleMenu.do?featureGroup=Users%20%26%20Groups&feature=LMS-MGR-0004&actionUrl=mgr_manageOrganizationGroup.do";
				this.feature = "LMS-MGR-0004";
			}else if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0005", user, request.getSession(true)) || user.isLMSAdministrator()) {
				setupResult=true;
				this.url = "lms_handleMenu.do?featureGroup=Users%20%26%20Groups&feature=LMS-MGR-0005&actionUrl=mgr_manageLearnerGroups.do";
				this.feature = "LMS-MGR-0005";
			}else if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0006", user, request.getSession(true)) || user.isLMSAdministrator()) {
				setupResult=true;
				this.url = "lms_handleMenu.do?featureGroup=Users%20%26%20Groups&feature=LMS-MGR-0006&actionUrl=mgr_manageSecurityRoles.do?method=showSecurityRoles";
				this.feature = "LMS-MGR-0006";
			}else if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0007", user, request.getSession(true)) || user.isLMSAdministrator()) {
				setupResult=true;
				this.url = "lms_handleMenu.do?featureGroup=Users%20%26%20Groups&feature=LMS-MGR-0007&actionUrl=mgr_addNewSecurityRole.do";
				this.feature = "LMS-MGR-0007";
			}
		}
		
		
		return setupResult;
	}
	
	public boolean setupLearner(com.softech.vu360.lms.vo.VU360User user, HttpServletRequest request)
	{
		boolean setupResult=false;
		/*****************************For MyCourse ************************************/
		if (UserPermissionChecker.hasAccessToFeatureGroup("My Dashboard", user, request.getSession(true))) {
			this.featureGroup = "My Dashboard";
			this.feature="LMS-LRN-0007";
			this.url = "lms_handleMenu.do?featureGroup=My%20Dashboard&feature=LMS-LRN-0007&actionUrl=lrn_widgetDashboard.do";
			setupResult=true;
		} else
		/*****************************For MyCourse ************************************/
		if (UserPermissionChecker.hasAccessToFeatureGroup("My Courses", user, request.getSession(true))) {
			this.featureGroup = "My Courses";
			this.feature="LMS-LRN-0001";
			this.url = "lms_handleMenu.do?featureGroup=ShopCatalog&feature=ShopCatalog&actionUrl=lrn_myCourses.do";
			setupResult=true;
		}
		/*****************************For Learner Reports************************************/
		else if (UserPermissionChecker.hasAccessToFeatureGroup("My Transcripts", user, request.getSession(true))) {
			this.featureGroup = "My Transcripts";
				this.url = "lms_handleMenu.do?featureGroup=My%20Transcripts&feature=LMS-LRN-0002&actionUrl=lrn_ViewReports.do?method=browseReports";
				this.feature = "LMS-LRN-0002";
				setupResult=true;
		}
		/*****************************For Learner Profile************************************/
		else if (UserPermissionChecker.hasAccessToFeatureGroup("My Profile", user, request.getSession(true))) {
			this.featureGroup = "My Profile";
			
			if(UserPermissionChecker.hasAccessToFeature("LMS-LRN-0003", user, request.getSession(true))) {
				this.url = "lms_handleMenu.do?featureGroup=My%20Profile&feature=LMS-LRN-0003&actionUrl=lrn_learnerProfile.do";
				this.feature = "LMS-LRN-0003";
				setupResult=true;
			}
			else if(UserPermissionChecker.hasAccessToFeature("LMS-LRN-0005", user, request.getSession(true))) {
				this.url = "lms_handleMenu.do?featureGroup=My%20Profile&feature=LMS-LRN-0005&actionUrl=lrn_myAccountOption.do";
				this.feature = "LMS-LRN-0005";
				setupResult=true;
			}
		}
		
		if(request.getParameter("isSurvey")!=null){
			this.featureGroup = "My Courses";
			this.feature="LMS-LRN-0001";
			this.url = "lms_handleMenu.do?featureGroup=My%20Courses&feature=ShopCatalog&actionUrl=lrn_mySurveys.do?method=displaySurveys";	
			setupResult=true;
		}
		
		return setupResult;
	}
	
	public boolean setupRegulatory(com.softech.vu360.lms.vo.VU360User user, HttpServletRequest request)
	{
		boolean setupResult=false;
		/*****************************For MyCourse ************************************/
		if (UserPermissionChecker.hasAccessToFeatureGroup("Search", user, request.getSession(true))) {
			this.featureGroup = "Search";
			this.feature="LMS-ACC-0001";
			this.url = "lms_handleMenu.do?featureGroup=Search&feature=LMS-ACC-0001&actionUrl=acc_search.do";
			setupResult=true;
		}
		/*****************************For Learner Reports************************************/
		else if (UserPermissionChecker.hasAccessToFeatureGroup("Regulators", user, request.getSession(true))) {
			this.featureGroup = "Regulators";
			if(UserPermissionChecker.hasAccessToFeature("LMS-ACC-0002", user, request.getSession(true))){
				this.url = "lms_handleMenu.do?featureGroup=Regulators&feature=LMS-ACC-0002&actionUrl=acc_manageApproval.do";
				this.feature = "LMS-ACC-0002";
				setupResult=true;
			}
			if(UserPermissionChecker.hasAccessToFeature("LMS-ACC-0003", user, request.getSession(true))){
				this.url = "lms_handleMenu.do?featureGroup=Regulators&feature=LMS-ACC-0003&actionUrl=acc_manageCredential.do";
				this.feature = "LMS-ACC-0003";
				setupResult=true;
			}
		}
		/*****************************For Learner Reports************************************/
		else if (UserPermissionChecker.hasAccessToFeatureGroup("Approvals", user, request.getSession(true))) {
			this.featureGroup = "Approvals";
			if(UserPermissionChecker.hasAccessToFeature("LMS-ACC-0004", user, request.getSession(true))){
				this.url = "lms_handleMenu.do?featureGroup=Approvals&feature=LMS-ACC-0004&actionUrl=acc_manageApproval.do";
				this.feature = "LMS-ACC-0004";
				setupResult=true;
			}
			if(UserPermissionChecker.hasAccessToFeature("LMS-ACC-0005", user, request.getSession(true))){
				this.url = "lms_handleMenu.do?featureGroup=Approvals&feature=LMS-ACC-0005&actionUrl=acc_manageProvider.do";
				this.feature = "LMS-ACC-0005";
				setupResult=true;
			}
			if(UserPermissionChecker.hasAccessToFeature("LMS-ACC-0006", user, request.getSession(true))){
				this.url = "lms_handleMenu.do?featureGroup=Approvals&feature=LMS-ACC-0006&actionUrl=acc_manageInstructor.do";
				this.feature = "LMS-ACC-0006";
				setupResult=true;
			}
			if(UserPermissionChecker.hasAccessToFeature("LMS-ACC-0007", user, request.getSession(true))){
				this.url = "lms_handleMenu.do?featureGroup=Approvals&feature=LMS-ACC-0007&actionUrl=acc_manageCertificate.do";
				this.feature = "LMS-ACC-0007";
				setupResult=true;
			}
			if(UserPermissionChecker.hasAccessToFeature("LMS-ACC-0009", user, request.getSession(true))){
				this.url = "lms_handleMenu.do?featureGroup=Approvals&feature=LMS-ACC-0009&actionUrl=acc_manageCourseConfig.do";
				this.feature = "LMS-ACC-0009";
				setupResult=true;
			}
			if(UserPermissionChecker.hasAccessToFeature("LMS-ACC-0010", user, request.getSession(true))){
				this.url = "lms_handleMenu.do?featureGroup=Approvals&feature=LMS-ACC-0010&actionUrl=acc_addCreditReport.do";
				this.feature = "LMS-ACC-0010";
				setupResult=true;
			}
			
		}
		/*****************************For Learner Reports************************************/
		else if (UserPermissionChecker.hasAccessToFeatureGroup("ReportsAcc", user, request.getSession(true))) {
			this.featureGroup = "ReportsAcc";
			this.url = "lms_handleMenu.do?featureGroup=ReportsAcc&feature=ReportsAcc&actionUrl=acc_ManageReports.do";
			this.feature = "ReportsAcc";
			setupResult=true;
		}
		return setupResult;
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the featureGroup
	 */
	public String getFeatureGroup() {
		return featureGroup;
	}

	/**
	 * @param featureGroup the featureGroup to set
	 */
	public void setFeatureGroup(String featureGroup) {
		this.featureGroup = featureGroup;
	}

	/**
	 * @return the feature
	 */
	public String getFeature() {
		return feature;
	}

	/**
	 * @param feature the feature to set
	 */
	public void setFeature(String feature) {
		this.feature = feature;
	}
	
}
