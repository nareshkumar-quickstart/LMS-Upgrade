package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.SelfRegistrationInvitationForm;
import com.softech.vu360.lms.web.controller.validator.SelfRegistrationInvitationValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

public class AddSelfRegistrationInvitationWizardController extends AbstractWizardFormController{
	private static final Logger log = Logger.getLogger(AddLearnerController.class.getName());
	private String closeTemplate = null;
	private LearnerService learnerService;
	private JavaMailSenderImpl mailSender;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private VelocityEngine velocityEngine;
	
	public AddSelfRegistrationInvitationWizardController() {
		super();
		setCommandName("selfRegistrationForm");
		setCommandClass(SelfRegistrationInvitationForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/userGroups/addSelfRegistration/invitation"
				, "manager/userGroups/addSelfRegistration/message"
				, "manager/userGroups/addSelfRegistration/confirmation"});
	}


	protected int getTargetPage(HttpServletRequest request, Object command,
			Errors errors, int currentPage) {
		// TODO Auto-generated method stub
		return super.getTargetPage(request, command, errors, currentPage);
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) throws Exception {
		// TODO Auto-generated method stub
		super.onBindAndValidate(request, command, errors, page);
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		return new ModelAndView(closeTemplate);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		SelfRegistrationInvitationForm registrationForm = (SelfRegistrationInvitationForm)command;
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
		.getAuthentication().getPrincipal();
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();


		RegistrationInvitation regInvitation = new RegistrationInvitation();
		regInvitation.setInvitationName(registrationForm.getInvitationName());
		regInvitation.setPasscode(registrationForm.getPassCode());
		if(registrationForm.isRegistrationUnlimited() == true) {
			regInvitation.setIsUnlimited(true);

		} else {
			regInvitation.setIsUnlimited(false);
			regInvitation.setMaximumRegistration(Integer.parseInt(registrationForm.getMaximumLimitedRegistration()));
		}
		List<OrganizationalGroup> orgGroupList = new ArrayList<OrganizationalGroup>();
		for(int i=0; i<registrationForm.getGroups().length;i++) {
			orgGroupList.add(learnerService.getOrganizationalGroupById(Long.valueOf(registrationForm.getGroups()[i])));
		}
		regInvitation.setOrgGroups(orgGroupList);

		if (registrationForm.getSelectedLearnerGroups() != null ) {
			List<LearnerGroup> learnerGroupList = new ArrayList<LearnerGroup>();
			for(int i=0; i<registrationForm.getSelectedLearnerGroups().length;i++) {
				learnerGroupList.add(learnerService.getLearnerGroupById(Long.valueOf(registrationForm.getSelectedLearnerGroups()[i])));
			}
			regInvitation.setLearnerGroups(learnerGroupList);
		}

		regInvitation.setInvitationMessage(registrationForm.getMessage());

		regInvitation.setCustomer(customer);

		String str = request.getParameter("sendMail");
		RegistrationInvitation regInvReturn =  learnerService.saveRegistrationInvitation(regInvitation);
		String ID = regInvReturn.getId().toString();
		if(StringUtils.isNotBlank(str)) {
			
				 Map<String, Object> model = new HashMap<>();
					//request.
				 
				 	model.put("user", user);
					model.put("customer", customer);
					model.put("passcode", regInvReturn.getPasscode());
					model.put("invMessage", registrationForm.getMessage());
					String  loginURL = VU360Properties.getVU360Property("lms.loginURL");
					model.put("url", loginURL+"selfRegistration.do?registrationId="+ID);
					model.put("registrationId", ID);
//					model.put("url", request.getScheme()+"://"+request.getServerName()
//							+":"+request.getServerPort()+request.getContextPath()+"/selfRegistration.do?registrationId="+ID);
					//commented by Faisal A. Siddiqui July 21 09 for new branding
					//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());
					Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
					 model.put("brander", brander);
					 String regInvTemplatePath =  brander.getBrandElement("lms.email.selfRegistration.body");
					 String fromAddress =  brander.getBrandElement("lms.email.selfRegistration.fromAddress");
					 String fromCommonName =  brander.getBrandElement("lms.email.selfRegistration.fromCommonName");
					 String subject =  brander.getBrandElement("lms.email.selfRegistration.subject");// + customer.getName();
					 String support =  brander.getBrandElement("lms.email.selfRegistration.fromCommonName");
					 model.put("support", support);
					 
					 String lmsDomain="";
					 lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
					 model.put("lmsDomain",lmsDomain);
					 
					String text = VelocityEngineUtils.mergeTemplateIntoString(
							velocityEngine, regInvTemplatePath, model);
					if(str.equalsIgnoreCase("on")) {
						
						SendMailService.sendSMTPMessage(user.getEmailAddress(), 
								fromAddress, fromCommonName, subject, text);
					}
					
				
			
		}
		return new ModelAndView(closeTemplate);
	}




	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		log.debug("IN referenceData");
		Map model = new HashMap();
		SelfRegistrationInvitationForm registrationForm = (SelfRegistrationInvitationForm)command;

		switch(page){
		case 0:
			//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();

			Long customerId = null;
			if (loggedInUser.isLMSAdministrator())
				customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
			else
				customerId = loggedInUser.getLearner().getCustomer().getId();

			OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
			String[] orgGroupList = registrationForm.getGroups();
			List<Long> orgGroupIdList = new ArrayList<Long>();
			if(orgGroupList!=null && orgGroupList.length>0){
				for(String orgGroup:orgGroupList){
					Long orgGroupId = Long.parseLong(orgGroup);
					orgGroupIdList.add(orgGroupId);
				}
			}
			TreeNode orgGroupRoot  = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup,orgGroupIdList,loggedInUser);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			
			model.put("orgGroupTreeAsList", treeAsList);

			//Available Learner Group List
			String[] availablelearnerGroupList=registrationForm.getAvailableLearnerGroups();
			if(availablelearnerGroupList!=null && availablelearnerGroupList.length>0){
				List<LearnerGroup> availableLearnersGroupList = new ArrayList<LearnerGroup>();

				for(String availablelearnerGroup:availablelearnerGroupList){
					Long availablelearnerGroupId = Long.parseLong(availablelearnerGroup);

					LearnerGroup learnerGroup=learnerService.getLearnerGroupById(availablelearnerGroupId);
					availableLearnersGroupList.add(learnerGroup);
				}
				model.put("availableLearnersGroupList", availableLearnersGroupList);
			}

			//Selected Learner Group List
			String[] selectedlearnerGroupList=registrationForm.getSelectedLearnerGroups();
			if(selectedlearnerGroupList!=null && selectedlearnerGroupList.length>0){
				List<LearnerGroup> selectedLearnersGroupList = new ArrayList<LearnerGroup>();

				for(String selectedlearnerGroup:selectedlearnerGroupList){
					Long selectedlearnerGroupId = Long.parseLong(selectedlearnerGroup);

					LearnerGroup learnerGroup=learnerService.getLearnerGroupById(selectedlearnerGroupId);
					selectedLearnersGroupList.add(learnerGroup);
				}
				model.put("selectedLearnersGroupList", selectedLearnersGroupList);
			}


			return model;

		case 1:
			break;

		case 2:
			String[] orgGroupsList = registrationForm.getGroups();
			List<Long> orgGroupsIdList = new ArrayList<Long>();
			if(orgGroupsList!=null && orgGroupsList.length>0){
				for(String orgGroup:orgGroupsList){
					Long orgGroupId = Long.parseLong(orgGroup);
					orgGroupsIdList.add(orgGroupId);
				}
			}
			List<String> orgGroupsNameList = new ArrayList<String>();
			for(Long id:orgGroupsIdList){
				String orgName=learnerService.getOrganizationalGroupById(id).getName();
				orgGroupsNameList.add(orgName);
			}
			model.put("orgGroupNameList", orgGroupsNameList);

			//Selected Learner Group List
			String[] selectedlearnerGroupIds=registrationForm.getSelectedLearnerGroups();
			if(selectedlearnerGroupIds!=null && selectedlearnerGroupIds.length>0){
				List<LearnerGroup> selectedLearnersGroupList = new ArrayList<LearnerGroup>();

				for(String selectedlearnerGroup:selectedlearnerGroupIds){
					Long selectedlearnerGroupId = Long.parseLong(selectedlearnerGroup);

					LearnerGroup learnerGroup=learnerService.getLearnerGroupById(selectedlearnerGroupId);
					selectedLearnersGroupList.add(learnerGroup);
				}
				model.put("selectedLearnersGroupList", selectedLearnersGroupList);
			}
			return model;

		default :
			break;
		}

		return super.referenceData(request, page);
	}

	private TreeNode getOrgGroupTree(TreeNode parentNode, OrganizationalGroup orgGroup, List<Long> selectedOrgGroups){
		if(orgGroup!=null){
			TreeNode node = new TreeNode(orgGroup);
			for(Long selectedId:selectedOrgGroups){
				if(orgGroup.getId().longValue()==selectedId.longValue()){
					node.setSelected(true);
					break;
				}
			}

			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i),selectedOrgGroups);
			}
			node.setEnabled(true);

			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}

	protected void validatePage(Object command, Errors errors, int page,
			boolean finish) {
		SelfRegistrationInvitationValidator regValidator = (SelfRegistrationInvitationValidator)this.getValidator();
		SelfRegistrationInvitationForm registrationForm = (SelfRegistrationInvitationForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			regValidator.validateInvitation(registrationForm, errors);
			break;

		case 1:
			regValidator.validateInvitationMessage(registrationForm, errors);
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		// TODO Auto-generated method stub
		return super.formBackingObject(request);
	}

	protected void onBind(HttpServletRequest request, Object command,
			BindException errors) throws Exception {
		// TODO Auto-generated method stub
		//super.onBind(request, command, errors);

		SelfRegistrationInvitationForm registrationForm = (SelfRegistrationInvitationForm)command;
		int Page=getCurrentPage(request);

		if (Page==0){
			if	(request.getParameter("selectedLearnerGroups")==null){
				if (registrationForm.getSelectedLearnerGroups()!=null){
					registrationForm.setSelectedLearnerGroups(null);
				}		
			}
			if (request.getParameter("availableLearnerGroups")==null){
				if (registrationForm.getAvailableLearnerGroups()!=null){
					registrationForm.setAvailableLearnerGroups(null);
				}		
			}

			if (request.getParameter("groups")==null){
				if (registrationForm.getGroups()!=null){
					registrationForm.setGroups(null);
				}		
			}
		}
		super.onBind(request, command, errors);
	}



	public String getCloseTemplate() {
		return closeTemplate;
	}


	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}


	public LearnerService getLearnerService() {
		return learnerService;
	}


	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}



	protected int getCurrentPage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return super.getCurrentPage(request);
	}


	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}


	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}


	/**
	 * @return the orgGroupService
	 */
	public OrgGroupLearnerGroupService getorgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}


	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}


	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

}