package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddLearnerGroupForm;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.lms.web.controller.validator.AssignLearnerGroupValidator;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.VU360Branding;

public class AddLearnerGroupController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddOrganizationGroupController.class.getName());
	private LearnerService learnerService;
	private EntitlementService entitlementService = null;
	private EnrollmentService enrollmentService;
	private VelocityEngine velocityEngine;
	private LearnersToBeMailedService learnersToBeMailedService;
	private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;

	private String finishTemplate = null;
	private String cancelTemplate = null;

	@Inject
	private VU360UserService vu360UserService;

	public AddLearnerGroupController() {
		super();
		setCommandName("addLearnerGroupForm");
		setCommandClass(AddLearnerGroupForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
						"manager/addLearnerGroup/step1"
						//, "manager/addLearnerGroup/step2"
				}
		);
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);
		AddLearnerGroupForm form = (AddLearnerGroupForm)command;
		form.setLearnerGroupId(request.getParameter("learnerGroupId"));
		return command;
	}

	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData( HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		AddLearnerGroupForm form = (AddLearnerGroupForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		log.debug("IN referenceData");

		switch(page) {

			case 0:
				if(form.getAction().equalsIgnoreCase("search")){
					request.setAttribute("newPage","true");
				}
				break;
			case 1:
				context.put("learners", form.getSelectedLearners());
				return context;
			default:
				break;
		}
		return super.referenceData(request, page);
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors,
									 int page) throws Exception {

		AddLearnerGroupForm form = (AddLearnerGroupForm)command;

		//sync the learner list with the selected learner list
		if ((page == 0
				&& !StringUtils.isBlank(form.getAction())
				&& form.getAction().equalsIgnoreCase("search"))
				|| !StringUtils.isBlank(form.getAction()) && form.getAction().equalsIgnoreCase("finish")
				||(this.getTargetPage(request, command, errors, 0)==1)
				){

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
				selecteditem.getUser().getLearner().setLearnerGroup(selecteditem.getUser().getLearner().getLearnerGroup());
			}
			form.setSelectedLearners(mergedLearnerList);
		}
		super.onBindAndValidate(request, command, errors, page);
	}


	@SuppressWarnings("unchecked")
	protected void postProcessPage(HttpServletRequest request, Object command,
								   Errors errors, int page) throws Exception {

		AddLearnerGroupForm form = (AddLearnerGroupForm)command;

		if( ( page == 0
				&& ((!StringUtils.isBlank(form.getAction()) && form.getAction().equalsIgnoreCase("search"))
				||(page == 1 && this.getTargetPage(request, command, errors, 0)==0)) ) ) {

			if( !StringUtils.isBlank(form.getSearchType()) ) {

				Map<Object,Object> results = new HashMap<Object,Object>();
				//VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				int pageNo = form.getPageIndex()<0 ? 0 : form.getPageIndex()/VelocityPagerTool.DEFAULT_PAGE_SIZE;
				List<VU360User> userList = null;
				String sortBy = form.getSortBy();
				int sortDirection = form.getSortDirection();

				/*
				 * simple search is no more used
				 */
				if( form.getSearchType().equalsIgnoreCase("simplesearch") ) {
					Integer totalResults = 0;
					if( !loggedInUser.isLMSAdministrator() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if( tempManagedGroups==null || (tempManagedGroups!=null &&  tempManagedGroups.size() == 0) ) {
							results = learnerService.findLearner1(form.getSearchKey(),
									loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(),
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups,
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
									pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, 0);
							totalResults = (Integer)results.get("recordSize");
							userList = (List<VU360User>)results.get("list");
						}
					} else {
						results = learnerService.findLearner1(form.getSearchKey(),
								loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(),
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups,
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
								pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, 0);
						totalResults = (Integer)results.get("recordSize");
						userList = (List<VU360User>)results.get("list");
					}
					//for pagination to work for a paged list
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
					pagerAttributeMap.put("totalCount", totalResults.toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}
				// advanced search...
				else if( form.getSearchType().equalsIgnoreCase("advancesearch") ) {

					Integer totalResults = 0;
					if( !loggedInUser.isLMSAdministrator() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups() ) {
						if( tempManagedGroups!=null && tempManagedGroups.size() > 0 ) {

							results = learnerService.findLearner1(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(),
									loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(),
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups,
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
									pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
							totalResults = (Integer)results.get("recordSize");
							userList = (List<VU360User>)results.get("list");
						}
					} else {

						results = learnerService.findLearner1(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(),
								loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(),
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups,
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
								pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
						totalResults = (Integer)results.get("recordSize");
						userList = (List<VU360User>)results.get("list");
					}
					//for pagination to work for a paged list
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
					pagerAttributeMap.put("totalCount", totalResults.toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);

				}else if( form.getSearchType().equalsIgnoreCase("allsearch") ) {

					if( !loggedInUser.isLMSAdministrator() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups() ) {
						if (tempManagedGroups!=null && tempManagedGroups.size() > 0 ) {

							/*
							 * @added by Dyutiman :: new service is needed to search
							 * all learners without pagination, & with search criteria.
							 */
							results = learnerService.findAllLearnersWithCriteria(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(),
									loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(),
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups,
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
									sortBy, sortDirection);
							userList = (List<VU360User>)results.get("list");
						}
					} else {

						results = learnerService.findAllLearnersWithCriteria(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(),
								loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(),
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups,
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
								sortBy, sortDirection);
						userList = (List<VU360User>)results.get("list");
					}

					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
					pagerAttributeMap.put("totalCount", ((Integer)results.get("recordSize")).toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}
				if( userList != null && userList.size() > 0 ) {
					List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
					for( VU360User auser : userList ) {
						LearnerItemForm learnerItem = new LearnerItemForm();
						learnerItem.setSelected(false);
						learnerItem.setUser(auser);
						learnerList.add(learnerItem);
					}
					for( LearnerItemForm learnerItem : learnerList ) {
						for( LearnerItemForm preSelectedItem : form.getSelectedLearners() ) {
							if( learnerItem.compareTo(preSelectedItem) == 0 ) {
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
			}
		}else if((page==0)&&(!form.getAction().equalsIgnoreCase("search"))){
			request.setAttribute("newPage","true");
		}
		super.postProcessPage(request, command, errors, page);
	}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		AssignLearnerGroupValidator validator = (AssignLearnerGroupValidator)this.getValidator();
		AddLearnerGroupForm form = (AddLearnerGroupForm)command;
		log.debug("Page num --- "+page);
		switch(page) {

			case 0:
				if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateFirstPage(form, errors);
				break;
			case 1:
				break;
			default:
				break;
		}
		super.validatePage(command, errors, page, finish);
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response,
										 Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		AddLearnerGroupForm form = (AddLearnerGroupForm)command;

		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("learnerGroupId", form.getLearnerGroupId());
		return new ModelAndView(cancelTemplate, "context", context);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
										 HttpServletResponse response, Object command, BindException error) throws Exception {

		log.debug("IN process processFinish *1");
		AddLearnerGroupForm form = (AddLearnerGroupForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		List<LearnerItemForm> selectedLearnerList = form.getSelectedLearners();
		List<Learner> selectedLearners = new ArrayList<Learner>();
		LearnerGroup learnerGroup = learnerService.getLearnerGroupById(Long.valueOf(form.getLearnerGroupId()));
		for(LearnerItemForm item:selectedLearnerList){
			//TODO need to test if isSelected method being manipulated correctly
			if(item.isSelected()){
				selectedLearners.add(vu360UserService.getUserById(item.getUser().getId()).getLearner());
			}
		}

		if (!selectedLearners.isEmpty()) {
			log.debug("IN process addLearnersInLearnerGroup start *2  selectedLearners.size() "+selectedLearners.size());
			learnerService.addLearnersInLearnerGroup(selectedLearners,learnerGroup);
			Brander brand =  VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
			log.debug("IN process addLearnersInLearnerGroup end *2 ");
			enrollmentService.enrollLearnersInLearnerGroupCourses(selectedLearners, learnerGroup.getLearnerGroupItems(), brand);
		}

		context.put("selectedLearners", form.getSelectedLearners().size());
		context.put("addedLearners", selectedLearners.size());
		context.put("learners", selectedLearners);

		context.put("learnerGroupName", learnerGroup.getName());
		context.put("learnerGroupId", learnerGroup.getId().toString());

		//After saving control will be transfered to member page
		return new ModelAndView(cancelTemplate, "context", context);
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
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

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}

	public void setLearnersToBeMailedService(
			LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}

	public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
		return asyncTaskExecutorWrapper;
	}

	public void setAsyncTaskExecutorWrapper(
			AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
		this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
	}

}