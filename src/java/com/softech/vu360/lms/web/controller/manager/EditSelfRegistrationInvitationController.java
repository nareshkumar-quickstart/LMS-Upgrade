package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.SelfRegistrationInvitationForm;
import com.softech.vu360.lms.web.controller.validator.SelfRegistrationInvitationValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.TreeNode;

/**
 * 
 * @author Saptarshi
 *
 */
public class EditSelfRegistrationInvitationController extends VU360BaseMultiActionController{

	private LearnerService learnerService = null;
	private VU360UserService vu360UserService = null;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;

	private String editRegistrationInvitationTemplate;
	private String editMessageTemplate;
	private String redirectTemplate;

	public EditSelfRegistrationInvitationController() {
		super();
	}

	public EditSelfRegistrationInvitationController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//return displayEditRegistrationInvitation( request,  response);
		return null;
	}

	public ModelAndView displayEditRegistrationInvitation(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		SelfRegistrationInvitationForm registrationForm = (SelfRegistrationInvitationForm)command;
		VU360User loggedInUser =  VU360UserAuthenticationDetails.getCurrentUser();

		Long customerId = null;
		if (vu360UserService.hasAdministratorRole(loggedInUser))
			customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
		else
			customerId = loggedInUser.getLearner().getCustomer().getId();

		OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
		String[] orgGroupList = registrationForm.getGroups();
		List<Long> orgGroupIdList = new ArrayList<Long>();
		List<LearnerGroup> availableLernerGroupList = new ArrayList<LearnerGroup>();
		if(orgGroupList!=null && orgGroupList.length>0){
			for(String orgGroup:orgGroupList){
				OrganizationalGroup orgGrp = learnerService.loadForUpdateOrganizationalGroup(Long.valueOf(orgGroup));
				availableLernerGroupList=this.getParentLearnerGroup(availableLernerGroupList, orgGrp);
				Long orgGroupId = orgGrp.getId();
				orgGroupIdList.add(orgGroupId);
			}
		}
		List<LearnerGroup> selectedLearnersGroupList = new ArrayList<LearnerGroup>();
		String[] selecterLernerGroup = new String[registrationForm.getSelectedLearnerGroups().length];
		int i = 0;
		for (String id : registrationForm.getSelectedLearnerGroups()) {
			LearnerGroup leanerGroup = learnerService.loadForDisplayLearnerGroup(Long.valueOf(id));
			selecterLernerGroup[i++] = id;
			selectedLearnersGroupList.add(leanerGroup);
		}
		availableLernerGroupList=this.remainingLearnerGroup(availableLernerGroupList,selectedLearnersGroupList);
		TreeNode orgGroupRoot  = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup,orgGroupIdList,loggedInUser);
		List<TreeNode> treeAsList = orgGroupRoot.bfs();

		registrationForm.setTreeAsList(treeAsList);
		registrationForm.setAvailableLernerGroupList(availableLernerGroupList);
		registrationForm.setSelectedLearnersGroupList(selectedLearnersGroupList);

		return new ModelAndView(editRegistrationInvitationTemplate);
	}

	private List<LearnerGroup> getParentLearnerGroup(List<LearnerGroup> availableLernerGroupList, OrganizationalGroup orgGroup) {
		List<LearnerGroup> learnerGroups = orgGroupLearnerGroupService.getLearnerGroupsByOrgGroup(orgGroup.getId());
		for (LearnerGroup lnrGrp : learnerGroups)
			if(lnrGrp != null)
				availableLernerGroupList.add(lnrGrp);
		if(orgGroup.getParentOrgGroup() != null) 
			this.getParentLearnerGroup(availableLernerGroupList,orgGroup.getParentOrgGroup());
		else
			return availableLernerGroupList;
		return availableLernerGroupList;
	}

	private List<LearnerGroup> remainingLearnerGroup(List<LearnerGroup> availableLernerGroupList, List<LearnerGroup> selectedLearnersGroupList) {
		for (int i=0;i<availableLernerGroupList.size() ;i++)
			for(int j=i+1;j<availableLernerGroupList.size() ;j++)
				if (availableLernerGroupList.get(i).getId().equals(availableLernerGroupList.get(j).getId()))
					availableLernerGroupList.remove(j);
		for (int i=0;i<availableLernerGroupList.size() ;i++)
			for(int j=0;j<selectedLearnersGroupList.size() ;j++)
				if (availableLernerGroupList.get(i) != null && availableLernerGroupList.get(i).getId().equals(selectedLearnersGroupList.get(j).getId())) {
					availableLernerGroupList.remove(i);
					i--;
					break;
				}
		return availableLernerGroupList;
	}

	public ModelAndView editMessage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
//		SelfRegistrationInvitationForm registrationForm = (SelfRegistrationInvitationForm)command;
	//	RegistrationInvitation regInvitation = learnerService.getRegistrationInvitationByID(registrationForm.getId());
	//	registrationForm.setMessage(regInvitation.getInvitationMessage());
		return new ModelAndView(editMessageTemplate);
	}
	public ModelAndView cancelInvitation(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(redirectTemplate);
	}
	public ModelAndView saveInvitation(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		if(errors.hasErrors()){
			if(request.getParameter("action").equalsIgnoreCase("saveInvitation"))
				return new ModelAndView(editRegistrationInvitationTemplate);
			else if(request.getParameter("action").equalsIgnoreCase("saveMessage"))
				return new ModelAndView(editMessageTemplate);
		}
		SelfRegistrationInvitationForm registrationForm = (SelfRegistrationInvitationForm)command;
		RegistrationInvitation regInvitation = learnerService.loadForUpdateRegistrationInvitation(registrationForm.getId());
		regInvitation.setInvitationName(registrationForm.getInvitationName());
		regInvitation.setPasscode(registrationForm.getPassCode());
		if(registrationForm.isRegistrationUnlimited() == true) {
			regInvitation.setIsUnlimited(true);
			regInvitation.setMaximumRegistration(0);
		} else {
			regInvitation.setIsUnlimited(false);
			regInvitation.setMaximumRegistration(Integer.parseInt(registrationForm.getMaximumLimitedRegistration()));
		}
		List<OrganizationalGroup> orgGroupList = new ArrayList<OrganizationalGroup>();
		if(registrationForm.getGroups()!= null && registrationForm.getGroups().length>0){
			for(int i=0; i<registrationForm.getGroups().length;i++) {
				orgGroupList.add(learnerService.getOrganizationalGroupById(Long.valueOf(registrationForm.getGroups()[i])));
			}
			regInvitation.setOrgGroups(orgGroupList);
		}else{
			regInvitation.setOrgGroups(null);
		}
	

		if (registrationForm.getSelectedLearnerGroups() != null ) {
			List<LearnerGroup> learnerGroupList = new ArrayList<LearnerGroup>();
			for(int i=0; i<registrationForm.getSelectedLearnerGroups().length;i++) {
				learnerGroupList.add(learnerService.getLearnerGroupById(Long.valueOf(registrationForm.getSelectedLearnerGroups()[i])));
			}
			regInvitation.setLearnerGroups(learnerGroupList);
		}
		
		regInvitation.setInvitationMessage(registrationForm.getMessage().trim());
		regInvitation=learnerService.saveRegistrationInvitation(regInvitation);
		registrationForm.setMessage(regInvitation.getInvitationMessage());
		return new ModelAndView(redirectTemplate);
	}

	public ModelAndView saveMessage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		if(errors.hasErrors()){
			if(request.getParameter("action").equalsIgnoreCase("saveMessage"))
				return new ModelAndView(editMessageTemplate);
		}
		SelfRegistrationInvitationForm registrationForm = (SelfRegistrationInvitationForm)command;
		RegistrationInvitation regInvitation = learnerService.loadForUpdateRegistrationInvitation(registrationForm.getId());
		
		regInvitation.setInvitationMessage(registrationForm.getMessage().trim());
		regInvitation=learnerService.saveRegistrationInvitation(regInvitation);
		registrationForm.setMessage(regInvitation.getInvitationMessage());
		return new ModelAndView(redirectTemplate);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.web.controller.VU360BaseMultiActionController#onBind(javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String)
	 */
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if(command instanceof SelfRegistrationInvitationForm){
			SelfRegistrationInvitationForm form = (SelfRegistrationInvitationForm)command;
			if(methodName.equals("displayEditRegistrationInvitation")
					||methodName.equals("editMessage")){
				//form.reset();
				//load the survey data from the command.getSid() into the command object using service
				RegistrationInvitation registrationInvitation = learnerService.getRegistrationInvitationByID(form.getId());
				form.setInvitationName(registrationInvitation.getInvitationName());
				form.setPassCode(registrationInvitation.getPasscode());
				if (registrationInvitation.getIsUnlimited()){
					form.setRegistrationUnlimited(true);
					form.setMaximumLimitedRegistration(null);
				} else {
					form.setRegistrationUnlimited(false);
					form.setMaximumLimitedRegistration(registrationInvitation.getMaximumRegistration()+"");
				}
				List<OrganizationalGroup> orgGroup = registrationInvitation.getOrgGroups();
				String[] organizationalGroups = new String[orgGroup.size()];
				int i=0;
				for (OrganizationalGroup orgGrp : orgGroup)
					organizationalGroups[i++] = orgGrp.getId().toString();
				form.setGroups(organizationalGroups);


				List<LearnerGroup> selectedLearnerGrps = registrationInvitation.getLearnerGroups();
				String[] learnerGrps = new String[selectedLearnerGrps.size()];
				i=0;
				for (LearnerGroup lnrGrp : selectedLearnerGrps)
					learnerGrps[i++] = lnrGrp.getId().toString();
				form.setSelectedLearnerGroups(learnerGrps);

				form.setMessage(registrationInvitation.getInvitationMessage().trim());

			}else if(methodName.equals("saveInvitation")){
				if (request.getParameter("groups")==null){
					
					form.setGroups(null);
							
				}
				
			}else if(methodName.equals("editMessage")){
				if (request.getParameter("message").trim()==null){
					
					form.setMessage(request.getParameter("message").trim());
							
				}
				
			}
			
		}
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

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.web.controller.VU360BaseMultiActionController#validate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, java.lang.String)
	 */
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		SelfRegistrationInvitationForm form = (SelfRegistrationInvitationForm)command;
		SelfRegistrationInvitationValidator validator = (SelfRegistrationInvitationValidator)this.getValidator();
		if(methodName.equals("saveInvitation")){
			validator.validateInvitation(form, errors);
			//validator.validateInvitationMessage(form, errors);
		}else if(methodName.equals("saveMessage")){
			//validator.validateInvitation(form, errors);
			validator.validateInvitationMessage(form, errors);
		}
	}

	/**
	 * @return the editRegistrationInvitationTemplate
	 */
	public String getEditRegistrationInvitationTemplate() {
		return editRegistrationInvitationTemplate;
	}

	/**
	 * @param editRegistrationInvitationTemplate the editRegistrationInvitationTemplate to set
	 */
	public void setEditRegistrationInvitationTemplate(
			String editRegistrationInvitationTemplate) {
		this.editRegistrationInvitationTemplate = editRegistrationInvitationTemplate;
	}

	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}

	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	/**
	 * @return the editMessageTemplate
	 */
	public String getEditMessageTemplate() {
		return editMessageTemplate;
	}

	/**
	 * @param editMessageTemplate the editMessageTemplate to set
	 */
	public void setEditMessageTemplate(String editMessageTemplate) {
		this.editMessageTemplate = editMessageTemplate;
	}

	/**
	 * @return the redirectTemplate
	 */
	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	/**
	 * @param redirectTemplate the redirectTemplate to set
	 */
	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
}
