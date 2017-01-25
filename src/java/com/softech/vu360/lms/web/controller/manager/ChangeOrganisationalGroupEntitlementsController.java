/**
 * 
 */
package com.softech.vu360.lms.web.controller.manager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.web.controller.SimpleFormController;
import com.softech.vu360.lms.web.controller.model.OrganisationalGroupEntitlementItem;
import com.softech.vu360.lms.web.controller.model.OrganisationalGroupEntitlementsForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.TreeNode;

/**
 * @author arijit
 * @date 13-01-09
 */

public class ChangeOrganisationalGroupEntitlementsController extends SimpleFormController {

	private static final Logger log = Logger.getLogger(ChangeOrganisationalGroupEntitlementsController.class.getName());
	private LearnerService learnerService = null;
	private EntitlementService entitlementService = null;
	private String viewAllEntitlementsTemplate = null;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;



	/**
	 * added by arijit dutta
	 * this method shows org group assignments
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */			

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		OrganisationalGroupEntitlementsForm form = (OrganisationalGroupEntitlementsForm)command; 
		OrganizationalGroup rootOrgGroup = null;
		Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentCustomerId();
		
			rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
		
		//Creating OrgGroups list
		List selectedEntitlements = form.getOrganisationalGroupEntitlementItems();
		String customerEntitlementId = request.getParameter("customerEntitlementId");
		String errorMsg = request.getParameter("errMsg");
		CustomerEntitlement customerEntitlement= entitlementService.getCustomerEntitlementById(Long.valueOf(customerEntitlementId));
		List<OrgGroupEntitlement> orgGroupEntitlements = entitlementService.getOrgGroupsEntilementsForCustomerEntitlement(customerEntitlement); 
		TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup, orgGroupEntitlements, selectedEntitlements);
		List<TreeNode> treeAsList = orgGroupRoot.bfs();

		Map model = new HashMap();
		model.put("errorMsg", errorMsg);
		model.put("orgGroupTreeAsList", treeAsList);
		model.put("customerEntitlement",customerEntitlement);
		return model;
	}

	public ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,Object command,BindException errors)
	throws Exception {
		try
		{
			Map<Object, Object> context = new HashMap<Object, Object>();
			String customerEntitlementId = request.getParameter("customerEntitlementId");
			CustomerEntitlement customerEntitlement= entitlementService.getCustomerEntitlementById(Long.valueOf(customerEntitlementId));
			List<OrgGroupEntitlement> orgGroupEntitlements = entitlementService.getOrgGroupsEntilementsForCustomerEntitlement(customerEntitlement);
			OrganisationalGroupEntitlementsForm form = (OrganisationalGroupEntitlementsForm)command;
			int sumMaxEnrollments = 0;
			//outer loop traverses all organizational group 
			for(int loop1=0;loop1<form.getOrganisationalGroupEntitlementItems().size();loop1++){
				if(((OrganisationalGroupEntitlementItem)form.getOrganisationalGroupEntitlementItems().get(loop1)).getOrganisationalGroupId() != null){
					int flagOldOrgGroupEntitlement = 0;
					//inner loop traverses all organizational group entitlement
					for(int loop2=0;loop2<orgGroupEntitlements.size();loop2++){
						if(orgGroupEntitlements.get(loop2).getOrganizationalGroup().getId().longValue() == ((OrganisationalGroupEntitlementItem)form.getOrganisationalGroupEntitlementItems().get(loop1)).getOrganisationalGroupId().longValue()){
							if(((OrganisationalGroupEntitlementItem)form.getOrganisationalGroupEntitlementItems().get(loop1)).getMaxEnrollments().isEmpty()){
								orgGroupEntitlements.get(loop2).setMaxNumberSeats(0);
							}else{
								orgGroupEntitlements.get(loop2).setMaxNumberSeats(Integer.parseInt(((OrganisationalGroupEntitlementItem)form.getOrganisationalGroupEntitlementItems().get(loop1)).getMaxEnrollments()));
								sumMaxEnrollments = sumMaxEnrollments + Integer.parseInt(((OrganisationalGroupEntitlementItem)form.getOrganisationalGroupEntitlementItems().get(loop1)).getMaxEnrollments());
							}
							flagOldOrgGroupEntitlement = 1;
							break;
						}
					}
					//for new OrganizationalGroupEntitlement 
					if(flagOldOrgGroupEntitlement == 0){
						OrgGroupEntitlement newOrgGroupEntitlement = new OrgGroupEntitlement();
						if(((OrganisationalGroupEntitlementItem)form.getOrganisationalGroupEntitlementItems().get(loop1)).getMaxEnrollments().isEmpty()){
							newOrgGroupEntitlement.setMaxNumberSeats(0);
						}else{
							newOrgGroupEntitlement.setMaxNumberSeats(Integer.parseInt(((OrganisationalGroupEntitlementItem)form.getOrganisationalGroupEntitlementItems().get(loop1)).getMaxEnrollments()));
							sumMaxEnrollments = sumMaxEnrollments + Integer.parseInt(((OrganisationalGroupEntitlementItem)form.getOrganisationalGroupEntitlementItems().get(loop1)).getMaxEnrollments());
						}
						newOrgGroupEntitlement.setOrganizationalGroup((OrganizationalGroup)learnerService.getOrganizationalGroupById(((OrganisationalGroupEntitlementItem)form.getOrganisationalGroupEntitlementItems().get(loop1)).getOrganisationalGroupId().longValue()));
						newOrgGroupEntitlement.setCustomerEntitlement(customerEntitlement);
						orgGroupEntitlements.add(newOrgGroupEntitlement);
					}
				}
			}
			if(customerEntitlement.isAllowUnlimitedEnrollments()){
				entitlementService.addOrgGroupEntitlementInCustomerEntitlement(customerEntitlement,orgGroupEntitlements);
			}else{
				if(customerEntitlement.getMaxNumberSeats() < sumMaxEnrollments){
					context.put("errorMsg", new Boolean(true));
				}else{
					context.put("errorMsg", new Boolean(false));
					entitlementService.addOrgGroupEntitlementInCustomerEntitlement(customerEntitlement,orgGroupEntitlements);
				}
			}
			context.put("customerEntitlementId",customerEntitlementId);
			ModelAndView modelAndView = super.onSubmit(request, response, command, errors);
			modelAndView = modelAndView.addObject("context", context);
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
			return super.onSubmit(request, response, command, errors);
		}
	}

	private TreeNode getOrgGroupTree( TreeNode parentNode, OrganizationalGroup orgGroup, List<OrgGroupEntitlement> orgGroupEntitlements, List selectedEntitlements){

		if(orgGroup!=null){
			OrganisationalGroupEntitlementItem entitlementItem = new OrganisationalGroupEntitlementItem(orgGroup.getId(), orgGroup.getName());
			TreeNode node = new TreeNode(entitlementItem);
			node.setEnabled(true);

			for(OrgGroupEntitlement dbEntitlement:orgGroupEntitlements){
				if(dbEntitlement.getOrganizationalGroup().getId().longValue()==orgGroup.getId().longValue()){
					entitlementItem.setMaxEnrollments(Integer.toString(dbEntitlement.getMaxNumberSeats()));
					break;
				}
			}

			for(int loop2=0;loop2<orgGroupEntitlements.size();loop2++){
				if(orgGroupEntitlements.get(loop2).getOrganizationalGroup().getId().longValue() == orgGroup.getId().longValue()){
					node.setSelected(true);
					break;
				}
			}


			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i), orgGroupEntitlements, selectedEntitlements);
			}

			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}


	public LearnerService getLearnerService() {
		return learnerService;
	}


	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}




	public String getViewAllEntitlementsTemplate() {
		return viewAllEntitlementsTemplate;
	}




	public void setViewAllEntitlementsTemplate(String viewAllEntitlementsTemplate) {
		this.viewAllEntitlementsTemplate = viewAllEntitlementsTemplate;
	}


	public EntitlementService getEntitlementService() {
		return entitlementService;
	}


	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	/**
	 * @return the orgGroupLearnerGroupService
	 */
	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	/**
	 * @param orgGroupLearnerGroupService the orgGroupLearnerGroupService to set
	 */
	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}


}

