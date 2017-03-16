package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.LearnerEmailForm;
import com.softech.vu360.lms.web.controller.model.LearnerGroupMailItem;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.lms.web.controller.validator.SendMailValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Branding;

public class ManageEmailController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(ManageEnrollmentController.class.getName());

	private LearnerService learnerService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private VelocityEngine velocityEngine;
	private VU360UserService vu360UserService;

	private String closeTemplate = null;
	private String cancelTemplate = null;

	private static final String MANAGE_USER_SORT_FIRSTNAME = "firstName";
	private static final String ENROLLMENT_METHOD_LEARNER = "Learner";
	private static final String ENROLLMENT_METHOD_ORGGROUP = "OrgGroup";
	private static final String ENROLLMENT_METHOD_LEARNERGROUP = "LearnerGroup";

	public ManageEmailController() {
		super();
		setCommandName("learnerEmailForm");
		setCommandClass(LearnerEmailForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/mail/sendEmail"
				, "manager/mail/assignMailLearners"
				, "manager/mail/assignMailOrgGroups"
				, "manager/mail/assignMailLearnerGroups"
				, "manager/mail/mailContent"
				, "manager/mail/mailConfirmation"});
	}

	/**
	 * Step 2.
	 * We do not need to override this method now.
	 * This method lets us hook in to the point
	 * before the request data is bound into the form/command object
	 * and just before the binder is initialized...
	 * We can have customized binders used here to interpret the request fields
	 * according to our requirements. It allows us to register 
	 * custom editors for certain fields.
	 * Called on the first request to this form wizard.
	 */
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		// Auto-generated method stub
		super.initBinder(request, binder);
	}

	/**
	 * Step 3.
	 * We do not need to override this method now.
	 * Since we have bindOnNewForm property set to true in the constructor
	 * this method will be called when the first request is processed.
	 * We can add custom implentation here later to populate the command object
	 * accordingly.
	 * Called on the first request to this form wizard.
	 */
	protected void onBindOnNewForm(HttpServletRequest request, Object command,
			BindException errors) throws Exception {
		// Auto-generated method stub
		HttpSession ssn=request.getSession(true);
		String cancelUrl=request.getParameter("cancelUrl");
		ssn.setAttribute("cancelUrl",cancelUrl);
		super.onBindOnNewForm(request, command, errors);
	}

	/**
	 * Step 4.
	 * Shows the first form view.
	 * Called on the first request to this form wizard.
	 */
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors) throws Exception {
		// Auto-generated method stub
		return super.showForm(request, response, errors);
	}

	protected Object formBackingObject(HttpServletRequest request)throws Exception {

		Object command = super.formBackingObject(request);
		try {
			LearnerEmailForm emailForm = (LearnerEmailForm)command;
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			List <LearnerGroup> selectedLearnerGroupsAssociatedWithOrgGroup = new ArrayList <LearnerGroup>();
			@SuppressWarnings("unused")
			List <OrganizationalGroup> learnerOrgGroups = null;
			Long customerId = null;

			if (vu360UserService.hasAdministratorRole(loggedInUser))
				customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
			else
				customerId = loggedInUser.getLearner().getCustomer().getId();

			if(vu360UserService.hasAdministratorRole(loggedInUser)) {
				learnerOrgGroups = orgGroupLearnerGroupService.getAllOrganizationalGroups(customerId);
			} else {
				if( loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups() ) {
					learnerOrgGroups = orgGroupLearnerGroupService.getAllOrganizationalGroups(customerId);
				} else {
					learnerOrgGroups = loggedInUser.getTrainingAdministrator().getManagedGroups();
				}
			}

			selectedLearnerGroupsAssociatedWithOrgGroup.addAll(orgGroupLearnerGroupService.getAllLearnerGroups(customerId,loggedInUser));
			List<LearnerGroupMailItem> learnerGrpItems = new ArrayList <LearnerGroupMailItem>();
			for(int learnerGroupNo=0; learnerGroupNo < selectedLearnerGroupsAssociatedWithOrgGroup.size(); learnerGroupNo++ ) {

				LearnerGroup lgrp = selectedLearnerGroupsAssociatedWithOrgGroup.get(learnerGroupNo);
				LearnerGroupMailItem item = new LearnerGroupMailItem();
				item.setLearnerGroupId(lgrp.getId());
				item.setLearnerGroupName(lgrp.getName());
				item.setSelected(false);
				learnerGrpItems.add(item);
			}
			emailForm.setLearnerGroupMailItems(learnerGrpItems);

		} catch (Exception e) {
			log.error(e, e);
		}
		return command;
	}

	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		Map <Object, Object>model = new HashMap <Object, Object>();
		List<Learner> leranersToBeMailed = new ArrayList <Learner>();
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		LearnerEmailForm emailForm = (LearnerEmailForm)command;

		switch(page){
			case 0:
				Map <Object, Object>enrollmentMethodMap = new LinkedHashMap <Object, Object>();
				enrollmentMethodMap.put(ENROLLMENT_METHOD_LEARNER, ENROLLMENT_METHOD_LEARNER);
				enrollmentMethodMap.put(ENROLLMENT_METHOD_ORGGROUP, ENROLLMENT_METHOD_ORGGROUP);
				enrollmentMethodMap.put(ENROLLMENT_METHOD_LEARNERGROUP, ENROLLMENT_METHOD_LEARNERGROUP);
	
				model.put("enrollmentMethods", enrollmentMethodMap);
				return model;
			case 1:
				break;
			case 2:
				Long customerId = null;
				if (vu360UserService.hasAdministratorRole(loggedInUser))
					customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
				else
					customerId = loggedInUser.getLearner().getCustomer().getId();
	
				OrganizationalGroup  rootOrgGroup =  orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
				String[] orgGroupList = emailForm.getGroups();
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
				return model;
				
			case 3:
				break;
				
			case 4:
				String enrollmentMethod = emailForm.getEmailMethod();
				if( enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNER) ) {
					List<LearnerItemForm> selectedUsers = emailForm.getSelectedLearners();
					if ( selectedUsers!=null && selectedUsers.size()>0 ){
						for(int i=0;i<selectedUsers.size();i++){
							LearnerItemForm learnerItemForm = selectedUsers.get(i);
							if (learnerItemForm.isSelected()){
								//LMS-13524
								Learner dbLearner = learnerService.getLearnerByVU360UserId( learnerItemForm.getUser() );
								leranersToBeMailed.add(dbLearner);
							}
						}
					}
				} else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_ORGGROUP) ) {
					//Get Learners from selected Organizational Groups 
					String[] strOrgGroupIdsArray = emailForm.getGroups();
					List<Long> orgGroupIdsList = new ArrayList<Long>();
					for( int i=0; i<strOrgGroupIdsArray.length; i++ ) {
						String strOrgGrpId = strOrgGroupIdsArray[i];
						if( StringUtils.isNotBlank(strOrgGrpId)) {
							orgGroupIdsList.add(Long.valueOf(strOrgGrpId));
						}	
					}
					Long orgGroupIdArray[] = new Long[orgGroupIdsList.size()];
					orgGroupIdArray = orgGroupIdsList.toArray(orgGroupIdArray);
					leranersToBeMailed = orgGroupLearnerGroupService.getLearnersByOrgGroupIds(orgGroupIdArray);
	//				List<OrganizationalGroup> organizationGroupList = orgGroupLearnerGroupService.getOrgGroupsById(orgGroupIdArray);
	//				Set<Long> learneIdSet = new HashSet<Long>();
	//				for(OrganizationalGroup orgGroup:organizationGroupList){
	//					for(Learner learner:orgGroup.getMembers()){
	//						if(!learneIdSet.contains(learner.getId())){
	//							learneIdSet.add(learner.getId());
	//							leranersToBeMailed.add(learner);
	//						}
	//					}
	//				}
				} else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNERGROUP) ) {
					List<LearnerGroupMailItem> learnerGrpMailItem = emailForm.getLearnerGroupMailItems();
					if(learnerGrpMailItem!=null && learnerGrpMailItem.size()>0){
						List<Long> learnerGroupIdList = new ArrayList<Long>();
						for( LearnerGroupMailItem item:learnerGrpMailItem) {
							if (item.isSelected()){
								learnerGroupIdList.add(item.getLearnerGroupId());
							} 
						}
						Long learnerGroupIdArray[] = new Long[learnerGroupIdList.size()]; 
						learnerGroupIdArray = learnerGroupIdList.toArray(learnerGroupIdArray);
						leranersToBeMailed = orgGroupLearnerGroupService.getLearnersByLearnerGroupIds(learnerGroupIdArray);
	//					List<LearnerGroup> learnerGroups = orgGroupLearnerGroupService.getLearnerGroupsByLearnerGroupIDs(learnerGroupIdArray);
	//					Set<Long> learneIdSet = new HashSet<Long>();
	//					for(LearnerGroup lrnGroup:learnerGroups){
	//						for(Learner learner:lrnGroup.getLearners()){
	//							if(!learneIdSet.contains(learner.getId())){
	//								learneIdSet.add(learner.getId());
	//								leranersToBeMailed.add(learner);
	//							}
	//						}
	//					}
					}
				}
				emailForm.setLeranersToBeMailed(leranersToBeMailed);
				String emailAddresses="";
				for(Learner learner:leranersToBeMailed){
					emailAddresses=emailAddresses + learner.getVu360User().getEmailAddress() + ",";
				}
				emailForm.setToEmails(emailAddresses);
				break;

			case 5:
				leranersToBeMailed = emailForm.getLeranersToBeMailed();
				//emailForm.setToEmails(emailAddresses);
				log.debug("ls size :::- "+leranersToBeMailed.size()+" BODY "+emailForm.getMessage());
				//commented by Faisal A. Siddiqui July 21 09 for new branding
				//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());
				//Added By Faisal A. Siddiqui July 21 2009 for new branding
				Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
				this.sendMail(emailForm.getFromEmail(), emailForm.getToEmails(), emailForm.getMailSubject(), emailForm.getMessage(), emailForm.getNotifyMe(),brander);
				//emailForm.formReset();
				break;

			default:
				break;
		}
		return super.referenceData(request, page);
	}	

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException error, int page) throws Exception {

		LearnerEmailForm form = (LearnerEmailForm)command;

		//sync the learner list with the selected learner list 
		if(( page == 1 
				&& !StringUtils.isBlank(form.getAction()) 
				&& form.getAction().equalsIgnoreCase("search"))
				||(this.getTargetPage(request, command, error, 1)==4)
				||(this.getTargetPage(request, command, error, 1)==0) )
		{

			List<LearnerItemForm> learnerList = form.getLearners();
			Collections.sort(learnerList);

			List<LearnerItemForm> selectedLearnerList = form.getSelectedLearners();
			Collections.sort(selectedLearnerList);

			List<LearnerItemForm> mergedLearnerList = new ArrayList<LearnerItemForm>();
			int i=0, j=0;
			LearnerItemForm item, selecteditem;
			for(;i<learnerList.size()&&j<selectedLearnerList.size();){
				item = learnerList.get(i);
				selecteditem = selectedLearnerList.get(j);
				if(item.compareTo(selecteditem)<0){
					i++;
					if(item.isSelected())
						mergedLearnerList.add(item);
				}else if(item.compareTo(selecteditem)>0){
					j++;
					mergedLearnerList.add(selecteditem);
				}else{
					if(item.isSelected())
						mergedLearnerList.add(item);
					i++; j++;
				}
			}
			for(;i<learnerList.size();i++){
				item = learnerList.get(i);
				if(item.isSelected())
					mergedLearnerList.add(item);
			}
			for(;j<selectedLearnerList.size();j++){
				selecteditem = selectedLearnerList.get(j);
				mergedLearnerList.add(selecteditem);
			}
			log.debug("SELECTED D SIZE -- "+mergedLearnerList.size());
			form.setSelectedLearners(mergedLearnerList);
		}
		super.onBindAndValidate(request, command, error, page);
	}

	
	@SuppressWarnings("unchecked")
	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {

		LearnerEmailForm form = (LearnerEmailForm)command;
		HttpSession session = request.getSession();

		if(( page == 1 &&
				((!StringUtils.isBlank(form.getAction()) &&
						form.getAction().equalsIgnoreCase("search"))
						||(this.getTargetPage(request, command, errors, 3)==4) && errors.hasErrors()))
						||(page == 4 && this.getTargetPage(request, command, errors, 1)==1) //back from next page
						||(page == 0 && this.getTargetPage(request, command, errors, 1)==1) ) {
			
			if( !StringUtils.isBlank(form.getSearchType()) ) {

				Map<Object,Object> results = new HashMap<Object,Object>();
				VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser(); 
				int pageNo = form.getSortPageIndex()<0 ? 0 : form.getSortPageIndex()/VelocityPagerTool.DEFAULT_PAGE_SIZE;
				String sortBy = (StringUtils.isBlank(form.getSortColumn())) ? MANAGE_USER_SORT_FIRSTNAME : form.getSortColumn();
				int sortDirection = form.getSortDirection();
				List<VU360User> userList = null;
				if( form.getSearchType().equalsIgnoreCase("simplesearch") ) {
					Integer totalResults = 0;
					if( !vu360UserService.hasAdministratorRole(loggedInUser) &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if (loggedInUser.getTrainingAdministrator().getManagedGroups().size()>0 ) {

							results = learnerService.findLearner1(form.getSearchKey(), 
									vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
							totalResults = (Integer)results.get("recordSize");
							userList = (List<VU360User>)results.get("list");
						}
					} else {
						results = learnerService.findLearner1(form.getSearchKey(), 
								vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
								pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
						totalResults = (Integer)results.get("recordSize");
						userList = (List<VU360User>)results.get("list");
					}
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("pageIndex", Integer.toString(form.getSortPageIndex()));
					pagerAttributeMap.put("totalCount", totalResults.toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}
				else if( form.getSearchType().equalsIgnoreCase("allsearch") ) {
					if( !vu360UserService.hasAdministratorRole(loggedInUser) &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if (loggedInUser.getTrainingAdministrator().getManagedGroups().size()>0 ) {
							
							/*
							 * @added by Dyutiman -
							 * new service is needed to show all learners without pagination,
							 * and with searching criteria...
							 */
							results = learnerService.findAllLearnersWithCriteria(form.getSearchFirstName(),
									form.getSearchLastName(), form.getSearchEmailAddress(), 
									vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									sortBy, sortDirection);
							//findAllLearners("", loggedInUser, sortBy, sortDirection);
							userList = (List<VU360User>)results.get("list");
						}
					} else {
						results = learnerService.findAllLearnersWithCriteria(form.getSearchFirstName(), form.getSearchLastName(), form.getSearchEmailAddress(), 
								vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
								sortBy, sortDirection);
						//findAllLearners("", loggedInUser, sortBy, sortDirection);
						userList = (List<VU360User>)results.get("list");
					}
				}
				else if(form.getSearchType().equalsIgnoreCase("advancesearch")){
					Integer totalResults = 0;
					if( !vu360UserService.hasAdministratorRole(loggedInUser) &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if (loggedInUser.getTrainingAdministrator().getManagedGroups().size()>0 ) {

							results = learnerService.findLearner1(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(), 
									vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
							totalResults = (Integer)results.get("recordSize");
							userList = (List<VU360User>)results.get("list");
						}
					} else {
						results = learnerService.findLearner1(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(), 
								vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
								pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
						totalResults = (Integer)results.get("recordSize");
						userList = (List<VU360User>)results.get("list");
					}
					//for pagination to work for a paged list
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("pageIndex", Integer.toString(form.getSortPageIndex()));
					pagerAttributeMap.put("totalCount", totalResults.toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}
				if( userList != null && userList.size() > 0 ) {
					List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
					for(VU360User auser:userList){
						LearnerItemForm learnerItem = new LearnerItemForm();
						learnerItem.setSelected(false);
						learnerItem.setUser(auser);
						learnerList.add(learnerItem);
					}
					for(LearnerItemForm learnerItem: learnerList) {
						for(LearnerItemForm preSelectedItem : form.getSelectedLearners()){
							if(learnerItem.compareTo(preSelectedItem)==0){
								learnerItem.setSelected(true);
								break;
							}
						}
					}
					form.setLearners(learnerList);
				} else {
					List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
					form.setLearners(learnerList);
				}
			}else{
				request.setAttribute("newPage","true");
			}
		} else if(((page == 1))&& (!form.getAction().equalsIgnoreCase("search"))){
			request.setAttribute("newPage","true");
		}else if( page == 3 ) {
			
			List<LearnerGroupMailItem> lGroups = form.getLearnerGroupMailItems();
			
			String sortDirection = request.getParameter("sortDirection");
//			if( (StringUtils.isBlank(sortDirection)) && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			if(StringUtils.isBlank(sortDirection)){
				sortDirection="0";
			}
			
			String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			String pageIndex = request.getParameter("pageCurrIndex");
			if( pageIndex == null ) pageIndex = "0";
			
			Map<String, Object> PagerAttributeMap = new HashMap <String, Object>();
			PagerAttributeMap.put("pageIndex",pageIndex);
			session.setAttribute("showAll", showAll);
			
			/** Added by Dyutiman...
			 *  manual sorting of learner groups
			 */
			request.setAttribute("PagerAttributeMap", PagerAttributeMap);
			
			if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex) ) {
				// sorting against course name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						for( int i = 0 ; i < lGroups.size() ; i ++ ) {
							for( int j = 0 ; j < lGroups.size() ; j ++ ) {
								if( i != j ) {
									LearnerGroupMailItem c1 = (LearnerGroupMailItem) lGroups.get(i);
									LearnerGroupMailItem c2 = (LearnerGroupMailItem) lGroups.get(j);
									if( c1.getLearnerGroupName().toUpperCase().compareTo
											(c2.getLearnerGroupName().toUpperCase()) > 0 ) {
										LearnerGroupMailItem tempLG = lGroups.get(i);
										lGroups.set(i, lGroups.get(j));
										lGroups.set(j, tempLG);
									}
								}
							}
						}
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 0);
					} else {
						for( int i = 0 ; i < lGroups.size() ; i ++ ) {
							for( int j = 0 ; j < lGroups.size() ; j ++ ) {
								if( i != j ) {
									LearnerGroupMailItem c1 = (LearnerGroupMailItem) lGroups.get(i);
									LearnerGroupMailItem c2 = (LearnerGroupMailItem) lGroups.get(j);
									if( c1.getLearnerGroupName().toUpperCase().compareTo
											(c2.getLearnerGroupName().toUpperCase()) < 0 ) {
										LearnerGroupMailItem tempLG = lGroups.get(i);
										lGroups.set(i, lGroups.get(j));
										lGroups.set(j, tempLG);
									}
								}
							}
						}
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 0);
					}
				}
			}
			form.setLearnerGroupMailItems(lGroups);
			
		} else if( page == 5 ) {
			form.formReset();
		}
	}

	/**
	 * method used to send Emails to selected learners. 
	 */
	@SuppressWarnings("unchecked")
	public void sendMail(String from, String toEmails, String subject, String msgText, String notifyMe,Brander brander){

		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.debug(loggedInUser.getEmailAddress() + " send mail");

		if( notifyMe.equalsIgnoreCase("on") ) {
			log.debug("sending mail to logged in user");
			Map model = new HashMap();

			model.put("message", msgText);
			//commented by Faisal A. Siddiqui July 21 09 for new branding
			//Brander brander = VU360Branding.getInstance().getBrander("default", new Language());

			String anouncementTemplatePath =  brander.getBrandElement("lms.email.anouncement.body");
			String fromAddress =  from;
			String fromCommonName =  brander.getBrandElement("lms.email.anouncement.fromCommonName");
			// String subject = subject;
			// String support =  brander.getBrandElement("lms.email.anouncement.fromCommonName");
			// model.put("support", support);

			String lmsDomain="";
			lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
			model.put("lmsDomain",lmsDomain);
			model.put("brandeR",brander);
			
			String text = VelocityEngineUtils.mergeTemplateIntoString( velocityEngine, anouncementTemplatePath, model);

			SendMailService.sendSMTPMessage(loggedInUser.getEmailAddress(), fromAddress, fromCommonName, subject, text); 

			/*SimpleMailMessage message = new SimpleMailMessage() ;
			message.setTo(loggedInUser.getEmailAddress());
			message.setText(msgText) ;
			message.setFrom(from) ;
			message.setSubject(subject) ;
			try {
				mailSender.send(message) ;
			} catch ( MailException m ) {
				log.debug("COUGHT EXCEPTION WHILE SENDING MAIL: ManageEmailController" + m.toString());
			}*/
		}
		StringTokenizer token = new StringTokenizer(toEmails,",");
		
		while( token.hasMoreElements() ) {
			
			String toEmail = token.nextElement().toString();
			Map model = new HashMap();
			model.put("message", msgText);
			//commented by Faisal A. Siddiqui July 21 09 for new branding
			//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());

			String anouncementTemplatePath =  brander.getBrandElement("lms.email.anouncement.body");
			String fromAddress =  from;
			String fromCommonName =  brander.getBrandElement("lms.email.anouncement.fromCommonName");
			
			if(from.contains("<")){
				fromAddress=from.substring(0, from.indexOf("<"));
				fromCommonName=from.substring(from.indexOf("<")+1, from.indexOf(">"));
				if(fromCommonName.isEmpty())fromCommonName =  brander.getBrandElement("lms.email.anouncement.fromCommonName");
			}
			
			// String subject = subject;
			// String support =  brander.getBrandElement("lms.email.anouncement.fromCommonName");
			// model.put("support", support);
			
			String lmsDomain="";
			lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
			model.put("lmsDomain",lmsDomain);
			model.put("brandeR",brander);
			
			String text = VelocityEngineUtils.mergeTemplateIntoString( velocityEngine, anouncementTemplatePath, model);
			SendMailService.sendSMTPMessage(toEmail, fromAddress, fromCommonName, subject, text); 
		}
	}

	protected ModelAndView processFinish(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, BindException arg3) {

		log.debug("IN processFinish");
		@SuppressWarnings("unused")
		List<Learner> leranersToBeMailed = new ArrayList <Learner>();
		LearnerEmailForm form = (LearnerEmailForm)arg2;
		leranersToBeMailed = form.getLeranersToBeMailed();
		//commented by Faisal A. Siddiqui July 21 09 for new branding
		//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());
		//Brander brander=(Brander)arg0.getSession().getAttribute(VU360Branding.BRAND);
		Brander brander=VU360Branding.getInstance().getBrander((String)arg0.getSession().getAttribute(VU360Branding.BRAND), new com.softech.vu360.lms.vo.Language());
		

		this.sendMail(form.getFromEmail(), form.getToEmails(), form.getMailSubject(), form.getMessage(), form.getNotifyMe(),brander);
		//	form.formReset();
		return new ModelAndView(closeTemplate);
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		log.debug("IN processCancel");
		//	form.formReset();
		return new ModelAndView(cancelTemplate);
	}

	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {

		LearnerEmailForm form = (LearnerEmailForm)command;
		HttpSession session = request.getSession();

		if( currentPage == 0 ) {
			String enrollmentMethod = form.getEmailMethod();

			if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNER)) {
				return 1;
			} else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_ORGGROUP)) {
				return 2;
			} else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNERGROUP)) {
				return 3;
			}
		} else if ( currentPage == 1 ) { 

			if(request.getParameter("action")!=null
					&& request.getParameter("action").equals("simpleSearch")) {
				return 1;
			}/*else if(request.getParameter("action")!=null
					&& request.getParameter("action").equals("allSearch")) {
				return 1;
			}*/
			session.setAttribute("pageIndex",1);
		} else if ( currentPage == 2 ) {
			session.setAttribute("pageIndex",2);
		} else if ( currentPage == 3 ) {
			session.setAttribute("pageIndex",3);
		}
		return super.getTargetPage(request, command, errors, currentPage);
	}

	protected void validatePage(Object command, Errors errors, int page ) {

		SendMailValidator validator = (SendMailValidator)this.getValidator();
		LearnerEmailForm emailForm = (LearnerEmailForm)command;
		log.debug("Page num --- "+page);
		switch(page) {
		case 0:
			validator.validateFirstPage(emailForm, errors);
			break;
		case 1:
			if(!emailForm.getAction().equalsIgnoreCase("search"))
				validator.validateSecondPage(emailForm, errors);
			break;
		case 2:
			validator.validateThirdPage(emailForm, errors);
			break;
		case 3:
			if(!emailForm.getLearnerGroupSearchAction().equalsIgnoreCase("search"))
				validator.validateFourthPage(emailForm, errors);
			break;
		case 4:
			validator.validateFifthPage(emailForm, errors);
			break;
		case 5:
			break;
		default:
			break;
		}
		errors.setNestedPath("");
		//super.validatePage(command, errors, page, finish);
	}

	@SuppressWarnings("unused")
	private TreeNode getOrgGroupTree(TreeNode parentNode, OrganizationalGroup orgGroup, 
			List<Long> selectedOrgGroups) {
		
		if( orgGroup != null ) {
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

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}


	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

}