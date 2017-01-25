package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
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
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorPreferences;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.BrandService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddCustomerForm;
import com.softech.vu360.lms.web.controller.model.AddDistributors;
import com.softech.vu360.lms.web.controller.validator.AddCustomerValidator;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LabelBean;
import com.softech.vu360.util.VU360Branding;

public class AddNewCustomerWizardController extends AbstractWizardFormController{

	private static final Logger log = Logger.getLogger(AddNewDistributorWizardController.class.getName());
	private String closeTemplate = null;
	private String cancelTemplate = null;
	private BrandService brandService = null;
	private CustomerService customerService = null;
	private DistributorService distributorService = null;

	public static final int SUMMARY_PAGE_INDEX = 3;
	
	public AddNewCustomerWizardController() {
		super();
		setCommandName("addCustomerForm");
		setCommandClass(AddCustomerForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"administrator/customer/addNewCustomerPick"
				, "administrator/customer/addNewCustomerProfile"
				, "administrator/customer/addNewCustomerPreferences"
				, "administrator/customer/addNewCustomerSummary"
		});
	}

	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		AddCustomerForm addcustomerForm = (AddCustomerForm)command;
		final int targetPage = this.getTargetPage(request, page);
		
		if( page == 0 && targetPage != 1 ) {

			if( request.getParameter("action") != null ) {

				if(request.getParameter("action").equals("search") ||
						(request.getParameter("showAll").equals("true") && request.getParameter("action").equals("paging"))){

					VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
					List<Distributor> distributorList = null;

					List<AddDistributors> addDistributor =new ArrayList<AddDistributors>();
					distributorList = distributorService.findDistributorsByName(addcustomerForm.getDistName(),user,false,0,-1,new ResultSet(),null,0);

					if(distributorList != null){
						for(Distributor distributor : distributorList) {
							AddDistributors addDistributors = new AddDistributors();
							addDistributors.setDistributor(distributor);
							addDistributors.setSelected(false);
							addDistributor.add(addDistributors);
						}
					}
					addcustomerForm.setDistributors(addDistributor);
				} else {
					request.setAttribute("newPage","true");
				}
			} else {
				request.setAttribute("newPage","true");
			}
		} else if( targetPage == SUMMARY_PAGE_INDEX ) {
			
			setCountryLabels((AddCustomerForm) command);
		}
		// Added by Dyutiman  :: LMS 4300
		// By default customer preferences are copied from re seller's.
		// However, customer can change it...
		if( page == 1 ) {
			Distributor distributor = null;
			for( AddDistributors addDistributors : addcustomerForm.getDistributors() ) {
				if( addDistributors.isSelected() ) {
					distributor = addDistributors.getDistributor();
					//LMS-13477 - in clustering environment its (distributor) became dirty object 
					//(unable to get its composite objects)
					distributor = distributorService.getDistributorById(distributor.getId());
	                break;
				}
			}
			if( distributor != null ) {
				DistributorPreferences dp = distributor.getDistributorPreferences();
				if( dp != null ) {
					addcustomerForm.setAudio(dp.isAudioEnabled());
					addcustomerForm.setAudioLocked(dp.isAudioLocked());
					addcustomerForm.setCaptioning(dp.isCaptioningEnabled());
					addcustomerForm.setCaptioningLocked(dp.isCaptioningLocked());
					if( dp.getBandwidth().equalsIgnoreCase("high") )
						addcustomerForm.setBandwidth(true);
					if( dp.getBandwidth().equalsIgnoreCase("low") )
						addcustomerForm.setBandwidth(false);
					addcustomerForm.setBandwidthLocked(dp.isBandwidthLocked());
					addcustomerForm.setVideo(dp.isVedioEnabled());
					addcustomerForm.setVideoLocked(dp.isVideoLocked());
					addcustomerForm.setRegistrationEmails(dp.isEnableRegistrationEmailsForNewCustomers());
					addcustomerForm.setRegistrationEmailsLocked(dp.isRegistrationEmailLocked());
					addcustomerForm.setEnrollmentEmails(dp.isEnableEnrollmentEmailsForNewCustomers());
					addcustomerForm.setEnrollmentEmailsLocked(dp.isEnrollmentEmailLocked());
					addcustomerForm.setCourseCompCertificateEmails(dp.isCourseCompletionCertificateEmailEnabled() );
					addcustomerForm.setCourseCompCertificateEmailsLocked(dp.isCourseCompletionCertificateEmailLocked());
				}
			}
		}
		super.postProcessPage(request, command, errors, page);
	}

	protected int getTargetPage(HttpServletRequest request, Object command,
			Errors errors, int currentPage) {

		if( currentPage == 0 ) {
			if( request.getParameter("action") != null ) {
				if( request.getParameter("action").equals("paging") ) {
					return 0;
				}
			}
		}
		return super.getTargetPage(request, command, errors, currentPage);
	}

	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		AddCustomerForm customerForm  = (AddCustomerForm)command;
		log.debug("IN referenceData");
		//Map <Object, Object>model = new HashMap <Object, Object>();
		//Map<Object, Object> model = new HashMap<Object, Object>();
		//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		switch(page) {

		case 0:
			if( request.getParameter("action") == null && request.getParameter("showAll") == null ) {
				request.setAttribute("newPage","true");
			}
			break;
		case 1:
			customerForm.setBrandsList(brandService.getAllBrands());
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
	
	@Override
	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {

		AddCustomerForm addCustomerForm = (AddCustomerForm)command;
		
				
		//do the customer switch for the admin
		//ideally should have done this through the filter mechanism
		VU360UserAuthenticationDetails adminDetails = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) adminDetails.getOriginalPrincipal();
		Customer customer = customerService.addCustomer(user.getId(), addCustomerForm);
		
		request.setAttribute("SwitchType", AdminSearchType.CUSTOMER);
		request.setAttribute("SwitchCustomer", ProxyVOHelper.setCustomerProxy(customer));
		adminDetails.doPopulateAdminSearchInformation(request);

		return new ModelAndView(closeTemplate);
	}
	

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int currentPage) throws Exception {
		
		AddCustomerForm addCustomerForm = (AddCustomerForm)command;
		AddCustomerValidator validator = (AddCustomerValidator)this.getValidator();
		
		if(currentPage == 0){
			// on starting the wizard get thebrander object and place it into the form 
			addCustomerForm.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));
		}

		if( request.getParameter("action") != null ){
			String distId = "0";
			String[] selectedDists = request.getParameterValues("dists");
			if( selectedDists != null ) {
				for( int distLength = 0; distLength < selectedDists.length; distLength ++) {
					distId = selectedDists[distLength];
					break;
				}
			}
			long selectedDistId = Long.valueOf(distId);
			for(AddDistributors addDist : addCustomerForm.getDistributors()) {
				addDist.setSelected(false);
				if( addDist.getDistributor().getId() == selectedDistId ) {
					addDist.setSelected(true);
				}
			}
		}

		if( request.getParameter("action") != null ) {
			if(currentPage == 0){
				if( !request.getParameter("action").equals("search") && !request.getParameter("action").equals("paging") ){
					validator.validatePage1(addCustomerForm, errors);
				}
			}
		}
		if( currentPage == 1 && this.getTargetPage(request, 1) == 2) {
			validator.validatePage2(addCustomerForm, errors);
		}
		super.onBindAndValidate(request, command, errors, currentPage);
	}

	protected void validatePage(Object command, Errors errors, int page) {

		log.debug("IN validatePage");
		//AddCustomerForm customerDetails = (AddCustomerForm)command;
		//AddCustomerValidator addCustomerValidator = (AddCustomerValidator) getValidator();

		errors.setNestedPath("");
		switch (page) {

		case 0:
			break;
		case 1:
			break;
		default:
			break;
		}
		errors.setNestedPath("");
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		return new ModelAndView(cancelTemplate);
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}
	
	/**
	 * @author khurram.shehzad
	 * @param setCountryStateLabels Set Label to country 
	 */

	private void setCountryLabels(AddCustomerForm form) {
		
		Brander brander = form.getBrander();
		final List<LabelBean> countries = brander.getBrandMapElements("lms.manageUser.AddLearner.Country");
		form.setCountryLabel1( findCountryLabel( countries, form.getCountry1() ) );
		form.setCountryLabel2( findCountryLabel( countries, form.getCountry2() ) );
	}

	private String findCountryLabel( List<LabelBean> countries, String countryCode){

		String countryLabel = "";
		for( LabelBean country : countries ) {
			if( country.getValue().equalsIgnoreCase(countryCode) ) {
				countryLabel = country.getLabel();
				break;
			}
		}
		return countryLabel;
	}

	public BrandService getBrandService() {
		return brandService;
	}

	public void setBrandService(BrandService brandService) {
		this.brandService = brandService;
	}

}