package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.DistributorGroupItemForm;
import com.softech.vu360.lms.web.controller.model.ManagerDistributorGroupForm;
import com.softech.vu360.lms.web.controller.validator.AddDistributorGroupValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * @author Tapas Mondal
 *
 */
public class ManageDistributorGroupWizardController extends AbstractWizardFormController{

	private static final Logger log = Logger.getLogger(ManageDistributorGroupWizardController.class.getName());
	private String manageDistributorGroupTemplate = null;
	private String distributorGroupAddConfirmTemplate = null;
	DistributorService distributorService = null;

	public String getManageDistributorGroupTemplate() {
		return manageDistributorGroupTemplate;
	}

	public void setManageDistributorGroupTemplate(
			String manageDistributorGroupTemplate) {
		this.manageDistributorGroupTemplate = manageDistributorGroupTemplate;
	}

	ManageDistributorGroupWizardController(){
		super();
		setCommandName("distributorGroupForm");
		setCommandClass(ManagerDistributorGroupForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"administrator/distributor/addDistributorGroup"
				, "administrator/distributor/distributorGroupSelectDistributors"
				, "administrator/distributor/distributorGroupConfirm"
				, "administrator/distributor/distributorGroupSuccess"});
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
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		log.debug("IN initBinder");
		// TODO Auto-generated method stub
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
	protected void onBindOnNewForm(HttpServletRequest request, Object command, BindException binder) throws Exception {
		log.debug("IN onBindOnNewForm");
		// TODO Auto-generated method stub
		super.onBindOnNewForm(request, command, binder);
	}

	/**
	 * Step 4.
	 * Shows the first form view.
	 * Called on the first request to this form wizard.
	 */
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException binder) throws Exception {
		log.debug("IN showForm");
		// TODO Auto-generated method stub request, response, binder
		ModelAndView modelNView = super.showForm(request, response, binder);
		String view = modelNView.getViewName();
		log.debug("OUT showForm for view = "+view);
		return modelNView;
	}

	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		log.debug("IN referenceData");
		//Map <Object, Object>model = new HashMap <Object, Object>();
		//Map<Object, Object> model = new HashMap<Object, Object>();
		//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		switch(page) {

		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		default :
			break;
		}

		return super.referenceData(request, page);
	}

	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {

		if ( currentPage == 1 ) { 
			if( request.getParameter("action") != null
					&& request.getParameter("action").equals("simpleSearch") ) {
				return 1;
			}
		}
		//	session.setAttribute("pageIndex",1);
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/**
	 * The Validator's default validate method WILL NOT BE CALLED by a wizard form controller!
	 * We need to do implementation specific - page by page - validation
	 * by explicitly calling the validateXXX function in the validator
	 */
	protected void validatePage(Object command, Errors errors, int page) {

		log.debug("IN validatePage");
		// TODO Auto-generated method stub
		AddDistributorGroupValidator validator = (AddDistributorGroupValidator)this.getValidator();
		ManagerDistributorGroupForm managerDistributorGroupForm = (ManagerDistributorGroupForm)command;

		errors.setNestedPath("");
		switch (page) {

		case 0:
			validator.validateFirstPage(managerDistributorGroupForm, errors);
			break;
		case 1:
			//			if(!managerDistributorGroupForm.getAction().equalsIgnoreCase("search"))
			//				validator.validateSecondPage(managerDistributorGroupForm, errors);
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			break;
		}
		errors.setNestedPath("");
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException error, int page) throws Exception {

		ManagerDistributorGroupForm managerDistributorGroupForm= (ManagerDistributorGroupForm)command;

		//sync the learner list with the selected learner list 
		if(( page == 1 
				&& !StringUtils.isBlank(managerDistributorGroupForm.getAction()) 
				&& managerDistributorGroupForm.getAction().equalsIgnoreCase("search"))
				||(this.getTargetPage(request, command, error, 1)==2)
				||(this.getTargetPage(request, command, error, 1)==0) )
		{

			List<DistributorGroupItemForm> distributorList = managerDistributorGroupForm.getDistributors();
			Collections.sort(distributorList);

			List<DistributorGroupItemForm> selectedDistributorList = managerDistributorGroupForm.getSelectedDistributors();
			Collections.sort(selectedDistributorList);

			List<DistributorGroupItemForm> mergedDistributorList = new ArrayList<DistributorGroupItemForm>();
			int i=0, j=0;
			DistributorGroupItemForm item, selecteditem;
			for(;i<distributorList.size()&&j<selectedDistributorList.size();){
				item = distributorList.get(i);
				selecteditem = selectedDistributorList.get(j);
				if(item.compareTo(selecteditem)<0){
					i++;
					if(item.getSelected())
						mergedDistributorList.add(item);
				}else if(item.compareTo(selecteditem)>0){
					j++;
					mergedDistributorList.add(selecteditem);
				}else{
					if(item.getSelected())
						mergedDistributorList.add(item);
					i++; j++;
				}
			}
			for(;i<distributorList.size();i++){
				item = distributorList.get(i);
				if(item.getSelected())
					mergedDistributorList.add(item);
			}
			for(;j<selectedDistributorList.size();j++){
				selecteditem = selectedDistributorList.get(j);
				mergedDistributorList.add(selecteditem);
			}
			log.debug("SELECTED D SIZE -- "+mergedDistributorList.size());
			managerDistributorGroupForm.setSelectedDistributors(mergedDistributorList);
		}
		super.onBindAndValidate(request, command, error, page);
	}

	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {

		ManagerDistributorGroupForm managerDistributorGroupForm = (ManagerDistributorGroupForm)command;
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser(); 
				//(VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		//CourseStatistics newCourseStatistics=new CourseStatistics();
		if (( page == 1 &&
				((!StringUtils.isBlank(managerDistributorGroupForm.getAction()) &&
						managerDistributorGroupForm.getAction().equalsIgnoreCase("search"))
						||(this.getTargetPage(request, command, errors, 1)==2) && errors.hasErrors()))
						||(page == 2 && this.getTargetPage(request, command, errors, 1)==1)//back from next page
						||(page == 0 && this.getTargetPage(request, command, errors, 1)==1) )
		{

			if(!StringUtils.isBlank(managerDistributorGroupForm.getSearchType())){
				Map<Object,Object> results = new HashMap<Object,Object>();

				int pageNo = managerDistributorGroupForm.getPageIndex()<0 ? 0 : managerDistributorGroupForm.getPageIndex()/VelocityPagerTool.DEFAULT_PAGE_SIZE;
				//	String sortBy = (StringUtils.isBlank(managerDistributorGroupForm.getSortColumn())) ? MANAGE_USER_SORT_FIRSTNAME : managerDistributorGroupForm.getSortColumn();
				int sortDirection = managerDistributorGroupForm.getSortDirection()<0 ? 0:1;
				List<Distributor> distributorList=null;
				String test = managerDistributorGroupForm.getSearchType();
				if(managerDistributorGroupForm.getSearchType().equalsIgnoreCase("simplesearch")) {

					results=distributorService.findDistributorsByName(managerDistributorGroupForm.getSearchDistributorGroupName(), loggedInUser,pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, "name", sortDirection);
					Integer totalResults = (Integer)results.get("recordSize");
					distributorList = (List<Distributor>)results.get("list");
					// for pagination to work for a paged list
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("totalCount", totalResults.toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}else if(managerDistributorGroupForm.getSearchType().equalsIgnoreCase("allsearch")) {
					log.debug("IN ALL SEARCH");
					distributorList = distributorService.findDistributorsByName(managerDistributorGroupForm.getSearchDistributorGroupName(), loggedInUser, false,0,-1,new ResultSet(),null,0);
					log.debug(" RECORD SIZE - "+results.size());
					//Integer totalResults = (Integer)results.get("recordSize");
					//distributorList = (List<Distributor>)results.get("list");
					// for pagination to work for a paged list
					//Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					//pagerAttributeMap.put("totalCount", totalResults.toString());
					//request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}else{
					request.setAttribute("newPage","true");
				}

				if(distributorList!=null && distributorList.size()>0){
					List<DistributorGroupItemForm> distributorList1 = new ArrayList<DistributorGroupItemForm>();
					for(Distributor distributor:distributorList){
						DistributorGroupItemForm distributorItem = new DistributorGroupItemForm();
						distributorItem.setSelected(false);
						distributorItem.setDistributor(distributor);
						distributorList1.add(distributorItem);
					}
					for(DistributorGroupItemForm learnerItem: distributorList1) {
						for(DistributorGroupItemForm preSelectedItem : managerDistributorGroupForm.getSelectedDistributors()){
							if(learnerItem.compareTo(preSelectedItem)==0){
								learnerItem.setSelected(true);
								break;
							}
						}
					}
					managerDistributorGroupForm.setDistributors(distributorList1);
				}else{
					/*List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
						form.setLearners(learnerList);*/
				}
			}else{
				request.setAttribute("newPage","true");
			}
		}

		/*if ( page == 2 ) {
			saveDistributorGroup(managerDistributorGroupForm,loggedInUser);
		}*/
		super.postProcessPage(request, command, errors, page);
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(manageDistributorGroupTemplate);
		//return super.processCancel(request, response, command, error);
	}

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processFinish");
		ManagerDistributorGroupForm managerDistributorGroupForm= (ManagerDistributorGroupForm)command;
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();

		int Page=getCurrentPage(request);
		saveDistributorGroup(managerDistributorGroupForm,loggedInUser);
		return new ModelAndView(distributorGroupAddConfirmTemplate);
	}

	private void saveDistributorGroup(ManagerDistributorGroupForm managerDistributorGroupForm,VU360User loggedInUser)throws Exception{
		DistributorGroup distributorGroup =  new DistributorGroup();

		//need to mofify after brand is fixed
		Customer customer = loggedInUser.getLearner().getCustomer();

		distributorGroup.setBrand(customer.getBrand());
		distributorGroup.setName(managerDistributorGroupForm.getDistributorGroupName());
		if (managerDistributorGroupForm.getSelectedDistributors() != null) {
			for(DistributorGroupItemForm distributorItem : managerDistributorGroupForm.getSelectedDistributors()) {
				Distributor distributor = distributorItem.getDistributor();
				distributorGroup.addDistributor(distributor);
			}
		}
		distributorService.addDistributorGroup(distributorGroup);
	}

	public String getDistributorGroupAddConfirmTemplate() {
		return distributorGroupAddConfirmTemplate;
	}

	public void setDistributorGroupAddConfirmTemplate(
			String distributorGroupAddConfirmTemplate) {
		this.distributorGroupAddConfirmTemplate = distributorGroupAddConfirmTemplate;
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

}