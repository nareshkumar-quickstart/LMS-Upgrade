package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddOrganizationGroupForm;
import com.softech.vu360.lms.web.controller.validator.AddOrganizationGroupValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.TreeNode;

public class AddOrganizationGroupController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddOrganizationGroupController.class.getName());
	private String finishTemplate = null;
	private String cancelTemplate = null;

	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private LearnerService learnerService;

	public AddOrganizationGroupController() {
		super();
		setCommandName("addOrgGroupForm");
		setCommandClass(AddOrganizationGroupForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/userGroups/addOrgGroup/Step1"
				, "manager/userGroups/addOrgGroup/Step2"});
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);
		return command;
	}

	protected Map<Object, Object> referenceData( HttpServletRequest request, Object command, 
			Errors errors, int page) throws Exception {

		log.debug("IN referenceData");
		AddOrganizationGroupForm form = (AddOrganizationGroupForm)command;
		Map model = new HashMap();

		switch(page) {

		case 0:
			
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();//Verify LMS-19638 if using another ways to get loggedInUser instance
			Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId(); // Please verify LMS-19638, LMS-19636 IF CHANGE THIS LINE
			OrganizationalGroup rootOrgGroup =  orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);

			String[] orgGroupList = form.getGroups();
			List<Long> orgGroupIdList = new ArrayList<Long>();

			if( orgGroupList != null && orgGroupList.length > 0 ){
				for( String orgGroup : orgGroupList ){
					Long orgGroupId = Long.parseLong(orgGroup);
					orgGroupIdList.add(orgGroupId);
				}
			}
			TreeNode orgGroupRoot = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup, orgGroupIdList,loggedInUser);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			for( TreeNode node : treeAsList )
				log.debug(node.toString());
			model.put("orgGroupTreeAsList", treeAsList);
			return model;

		case 1:
			
			if( form.getGroups() != null ) {
				String[] orgGroupIds = form.getGroups();
				Long orgGroupId = Long.parseLong(orgGroupIds[0]);
				OrganizationalGroup parentGroup = learnerService.getOrganizationalGroupById(orgGroupId);
				form.setParentGroupName(parentGroup.getName());
			}
			break;
			
		default :
			break;
		}

		return super.referenceData(request, page);
	}	

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException excep) throws Exception {

		AddOrganizationGroupForm form = (AddOrganizationGroupForm)command;
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer(); //LMS-19636
		
		OrganizationalGroup rootGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
		//List<OrganizationalGroup> newOrganizationalGroups = new ArrayList<OrganizationalGroup>();
		List<String> orgGroupNames = new ArrayList<String>();
		List<Long> orgGroupIdList = new ArrayList<Long>();

		//for( OrganizationalGroup organizationalGroup : organizationalGroups ) {

		String[] orgGroupIds = form.getGroups();
		Long orgGroupId = Long.parseLong(orgGroupIds[0]);
			String organizationalGroupId = form.getGroups().toString();
			String orgGroupName = form.getNewGroupName();

			if( !StringUtils.isBlank(orgGroupName) ) {
				OrganizationalGroup newOrganizationalGroup = new OrganizationalGroup();
				orgGroupIdList.add(orgGroupId);
				newOrganizationalGroup.setName(orgGroupName);
				orgGroupNames.add(orgGroupName);
				newOrganizationalGroup.setCustomer(customer);
				OrganizationalGroup parentOrganizationalGroup = learnerService.loadForUpdateOrganizationalGroup(orgGroupId);
				//parentOrganizationalGroup.setId(orgGroupId);
				newOrganizationalGroup.setRootOrgGroup(rootGroup);
				newOrganizationalGroup.setParentOrgGroup(parentOrganizationalGroup);
				parentOrganizationalGroup.getChildrenOrgGroups().add(newOrganizationalGroup);
				newOrganizationalGroup = orgGroupLearnerGroupService.addOrgGroup(parentOrganizationalGroup);
				//newOrganizationalGroups.add(newOrganizationalGroup);
			} else {
			}
		//}
		/*if ( newOrganizationalGroups.size() > 0 ){
			orgGroupLearnerGroupService.addOrgGroup(newOrganizationalGroups);
		}*/
		return new ModelAndView(finishTemplate);
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) throws Exception {

		AddOrganizationGroupValidator validator = (AddOrganizationGroupValidator)this.getValidator();
		AddOrganizationGroupForm form = (AddOrganizationGroupForm)command;
		
		switch(page) {
		case 0:
			validator.validateFirstPage(form, errors);
			break;
		case 1:
			break;
		default:
			break;
		}	
		super.onBindAndValidate(request, command, errors, page);
	}

	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {
		log.debug("PAGE - "+page);
		super.postProcessPage(request, command, errors, page);
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		return new ModelAndView(cancelTemplate);
		//return super.processCancel(request, response, command, errors);
	}

	private TreeNode getOrgGroupTree(TreeNode parentNode, OrganizationalGroup orgGroup,
			List<Long> selectedOrgGroups){
		
		if( orgGroup != null ){
			TreeNode node = new TreeNode(orgGroup);
			for( Long selectedId : selectedOrgGroups ){
				if(orgGroup.getId().longValue() == selectedId.longValue()){
					node.setSelected(true);
					break;
				}
			}
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i),selectedOrgGroups);
			}
			node.setEnabled(true);

			if( parentNode != null ){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

}