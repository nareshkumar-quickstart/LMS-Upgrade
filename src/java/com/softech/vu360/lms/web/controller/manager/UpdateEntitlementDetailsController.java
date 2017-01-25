package com.softech.vu360.lms.web.controller.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.util.TreeNode;

/**
 * @author arijit dutta
 *
 */
public class UpdateEntitlementDetailsController extends MultiActionController implements InitializingBean {
	private static final Logger log = Logger.getLogger(UpdateEntitlementDetailsController.class.getName());
	private String updateEntitlementDetailsTemplate = null;
	private static final String MANAGE_ENTITLEMENT_UPDATE_ACTION = "update";
	private LearnerService learnerService = null;
	private EntitlementService entitlementService = null;


	/**
	 * added by arijit
	 * this method update Entitlement Details
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView updateEntitlement(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String action = request.getParameter("action");
			if(StringUtils.isNotBlank(action)){
				if(action.equalsIgnoreCase(MANAGE_ENTITLEMENT_UPDATE_ACTION)){
					String EntitlementName = request.getParameter("EntitlementName");
					String MaximumEnrollments = request.getParameter("MaximumEnrollments");
					String AllowSelfEnrollment = request.getParameter("AllowSelfEnrollment");
					String EntitlementType = request.getParameter("EntitlementType");
					String StartDate = request.getParameter("StartDate");
					String MaximumEnrollmentsRadio = request.getParameter("MaximumEnrollmentsRadio");
					log.debug("MaximumEnrollmentsRadio  "+MaximumEnrollmentsRadio);
					String TermsofServicesRadio = request.getParameter("TermsofServicesRadio");
					String TermsofServices = request.getParameter("TermsofServices");
					String FixedEndDate = request.getParameter("EndDate");
					String[] selectedOrgGroupValues = request.getParameterValues("groups");
					log.debug("selectedOrgGroupValues"+selectedOrgGroupValues);
					if( selectedOrgGroupValues != null ){
						for( int i=0; i<selectedOrgGroupValues.length; i++ ) {
							String orgGroupID = selectedOrgGroupValues[i];
							if( StringUtils.isNotBlank(orgGroupID) ) {
								List<Learner> listLearners = new ArrayList();
								OrganizationalGroup orgGroup = learnerService.getOrganizationalGroupById(Long.valueOf(orgGroupID));
								log.debug("orgGroup"+orgGroup.getName());
								String str = orgGroup.getName();
								log.debug(request.getParameter(str));
							}	
						}		
					}
				}
			}
			Map<Object, Object> context = new HashMap<Object, Object>();
			OrganizationalGroup rootOrgGroup = null;
			//Creating OrgGroups list
			TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			context.put("orgGroupTreeAsList", treeAsList);
			Map<Object, Object> map1 = new HashMap<Object, Object>();
			String customerEntitlementId = request.getParameter("customerEntitlementId");
			CustomerEntitlement customerEntitlement= entitlementService.getCustomerEntitlementById(Long.valueOf(customerEntitlementId));
			List<OrgGroupEntitlement> orgGroupEntitlements= null;//TODO will implement after new DAO written comment on 02/04/09.
			//TODO will implement after new DAO written comment on 02/04/09.
			for(int loop1=0; loop1<treeAsList.size(); loop1++){
				//if(orgGroupEntitlements.size() == 0){
					map1.put(((OrganizationalGroup)treeAsList.get(loop1).getValue()).getId(), new Integer(0));
				/*}else{
					for(int loop2=0; loop2<orgGroupEntitlements.size(); loop2++){
						if(!((OrganizationalGroup) treeAsList.get(loop1).getValue()).getId().equals(orgGroupEntitlements.get(loop2).getOrganizationalGroup().getId())){
							map1.put(((OrganizationalGroup)treeAsList.get(loop1).getValue()).getId(), new Integer(0));
						}
					}
				}*/
			}
			/*for(int loop3=0; loop3<orgGroupEntitlements.size(); loop3++){
				if(orgGroupEntitlements.get(loop3).isAllowUnlimitedEnrollments()){
					OrganizationalGroup orgGroup = orgGroupEntitlements.get(loop3).getOrganizationalGroup();
					map1.put(orgGroup.getId(), new Integer(orgGroupEntitlements.get(loop3).getMaxNumberSeats()));
				}else{
					OrganizationalGroup orgGroup = orgGroupEntitlements.get(loop3).getOrganizationalGroup();
					map1.put(orgGroup.getId(), new Integer(orgGroupEntitlements.get(loop3).getMaxNumberSeats()));
				}
			}*/
			String startDateString = null;
			String endDateString = null;
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			if(customerEntitlement.getEndDate() != null) {
				endDateString = formatter.format(customerEntitlement.getEndDate());
			}
			if(customerEntitlement.getStartDate() != null){
				startDateString = formatter.format(customerEntitlement.getStartDate());
			}
			if(customerEntitlement instanceof CourseGroupCustomerEntitlement){
				context.put("CourseGroup", new Boolean("True"));
				context.put("Course", new Boolean("False"));
			}
			if(customerEntitlement instanceof CourseCustomerEntitlement){
				context.put("Course", new Boolean("True"));
				context.put("CourseGroup", new Boolean("False"));
			}
			context.put("customerEntitlement",customerEntitlement);
			context.put("EndDate",endDateString);
			context.put("StartDate",startDateString);
			context.put("countmap", map1);
			return new ModelAndView(updateEntitlementDetailsTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(updateEntitlementDetailsTemplate);
	}

	private TreeNode getOrgGroupTree( TreeNode parentNode, OrganizationalGroup orgGroup){

		if(orgGroup!=null){

			TreeNode node = new TreeNode(orgGroup);
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i));
			}

			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	public String getUpdateEntitlementDetailsTemplate() {
		return updateEntitlementDetailsTemplate;
	}

	public void setUpdateEntitlementDetailsTemplate(
			String updateEntitlementDetailsTemplate) {
		this.updateEntitlementDetailsTemplate = updateEntitlementDetailsTemplate;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}	

}	