package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
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

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.BrandService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddDistributorForm;
import com.softech.vu360.lms.web.controller.model.AddDistributorGroups;
import com.softech.vu360.lms.web.controller.validator.AddDistributorValidator;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

/**
 * 
 * @author Saptarshi
 *
 */
public class AddNewDistributorWizardController extends AbstractWizardFormController{

	private static final Logger log = Logger.getLogger(AddNewDistributorWizardController.class.getName());
	private String closeTemplate = null;
	private String finishTemplate = null;
	private BrandService brandService = null;
	private DistributorService distributorService = null;

	public AddNewDistributorWizardController() {
		super();
		setCommandName("addDistributorForm");
		setCommandClass(AddDistributorForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"administrator/distributor/addNewDistributorProfile"
				, "administrator/distributor/addNewDistributorPreferences"
				, "administrator/distributor/addNewDistributorGroups"
				, "administrator/distributor/addNewDistributorSummary"
		});
	}

	protected int getTargetPage(HttpServletRequest request, Object command,
			Errors errors, int currentPage) {
		if(currentPage==2){
			if(request.getParameter("action")!=null){
				if(request.getParameter("action").equals("paging")){
					return 2;
				}
			}
		}
		return super.getTargetPage(request, command, errors, currentPage);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {

		AddDistributorForm addDistributorForm = (AddDistributorForm)command;

		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Distributor distributor = distributorService.addDistributor(loggedInUser, addDistributorForm);
		
		VU360UserAuthenticationDetails adminDetails = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
		request.setAttribute("SwitchType", AdminSearchType.DISTRIBUTOR);
		request.setAttribute("SwitchDistributor", ProxyVOHelper.setDistributorProxy(distributor));
		adminDetails.doPopulateAdminSearchInformation(request);


		return new ModelAndView(finishTemplate);
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {
		Map model = new HashMap();
		AddDistributorForm addDistributorForm = (AddDistributorForm)command;

		switch(page){
		case 0:
			addDistributorForm.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));			
			addDistributorForm.setBrandsList(brandService.getAllBrands());
			
			break;
		case 1:	
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		default :
			break;
		}	
		return super.referenceData(request, command, errors, page);
	}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		AddDistributorValidator validator = (AddDistributorValidator)this.getValidator();
		AddDistributorForm form = (AddDistributorForm)command;
		switch(page){
		case 0:
			if( ! form.getEventSource().equalsIgnoreCase("donotValidate"))
				validator.validateProfilePage(form, errors);
			break;
		case 1:
			break;
		case 2:
			//validator.validateDistributorGroupPage(form, errors);
			break;
		case 3:
			break;
		case 4:
			break;
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	protected boolean isFormSubmission(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return super.isFormSubmission(request);
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int currentPage) throws Exception {
		AddDistributorForm addDistributorForm = (AddDistributorForm)command;
		AddDistributorValidator validator = (AddDistributorValidator)this.getValidator();
		/*if(request.getParameter("action")!=null) {
			if(currentPage==2){
				if( !request.getParameter("action").equals("search") && !request.getParameter("action").equals("paging") ){
					validator.validateDistributorGroupPage(addDistributorForm, errors);
				}
			}
		}*/
		super.onBindAndValidate(request, command, errors, currentPage);
	}

	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {
		if(page==1){
			request.setAttribute("newPage","true");
		}
		if(page==2 && this.getTargetPage(request, page) != 3){
			if(request.getParameter("action")!=null){
				if(request.getParameter("action").equals("search") 
						|| (request.getParameter("showAll").equals("true") && request.getParameter("action").equals("paging"))){
					AddDistributorForm addDistributorForm = (AddDistributorForm)command;


					com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

					List<DistributorGroup> distributorGroupList = null;
					List<AddDistributorGroups> addDistributorGroupsList = new ArrayList<AddDistributorGroups>();
					distributorGroupList = distributorService.findDistributorGroupsByName(addDistributorForm.getDistName(),user,false);

					if(distributorGroupList != null){
						for(DistributorGroup distributorGroup : distributorGroupList) {
							AddDistributorGroups addDistributorGroups = new AddDistributorGroups();
							addDistributorGroups.setDistributorGroup(distributorGroup);
							addDistributorGroups.setSelected(false);
							addDistributorGroupsList.add(addDistributorGroups);
						}
					}

					addDistributorForm.setDistributors(addDistributorGroupsList);

				}{
					request.setAttribute("newPage","true");
				}
			}{
				request.setAttribute("newPage","true");
			}
		}
		super.postProcessPage(request, command, errors, page);
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		return new ModelAndView(closeTemplate);
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	/**
	 * @return the finishTemplate
	 */
	public String getFinishTemplate() {
		return finishTemplate;
	}

	/**
	 * @param finishTemplate the finishTemplate to set
	 */
	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public BrandService getBrandService() {
		return brandService;
	}

	public void setBrandService(BrandService brandService) {
		this.brandService = brandService;
	}
	
}
