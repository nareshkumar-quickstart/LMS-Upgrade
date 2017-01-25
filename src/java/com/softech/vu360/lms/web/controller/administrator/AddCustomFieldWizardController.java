package com.softech.vu360.lms.web.controller.administrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldForm;
import com.softech.vu360.lms.web.controller.validator.CustomFieldValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class AddCustomFieldWizardController extends AbstractWizardFormController {

	private CustomFieldService customFieldService=null;
	private	String redirectTemplate=null;
	private	String successfulTemplate=null;
	private DistributorService distributorService=null;
	private CustomerService customerService=null;
	
	
	
	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	//public static final String CUSTOMFIELD_DATE_OF_BIRTH = "Date of Birth";
	private static final String[] CUSTOMFIELD_TYPES = {
		CUSTOMFIELD_SINGLE_LINE_OF_TEXT
		, CUSTOMFIELD_DATE
		, CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT
		, CUSTOMFIELD_NUMBER
		, CUSTOMFIELD_RADIO_BUTTON
		, CUSTOMFIELD_CHOOSE
		, CUSTOMFIELD_CHECK_BOX
		, CUSTOMFIELD_SOCIAL_SECURITY_NUMBER
	};

	public AddCustomFieldWizardController() {
		super();
		setCommandName("customFieldForm");
		setCommandClass(CustomFieldForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"administrator/customField/add_customfield_credentials"
				, "administrator/customField/add_customfield_details_container"
				, "administrator/customField/add_customfield_confirm"
		});
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("static-access")
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		CustomFieldForm form = (CustomFieldForm)command;
		CustomField customField = new CustomField();
		form.setCustomField(customField);
		form.setFieldType(CUSTOMFIELD_SINGLE_LINE_OF_TEXT);

		if (request.getParameter("entity") != null)
			form.setEntity(request.getParameter("entity"));
		/*
		if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
			long credentialID = Long.parseLong(request.getParameter("customerId"));
			Customer customer = accreditationService.getCredentialById(credentialID);
			form.setCustomer(customer);
		} else if (form.getEntity().equalsIgnoreCase(form.RESELLER)) {
			long regulatorID = Long.parseLong(request.getParameter("resellerID"));
			Distributor reseller = accreditationService.getRegulatorById(regulatorID);
			form.setDistributor(reseller );
		} 
		*/
		
	return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		CustomFieldForm form = (CustomFieldForm)command;
		if(this.getTargetPage(request, page) == 2) {
			if(form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_RADIO_BUTTON)
					||form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHOOSE)
					||form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHECK_BOX)) {
				if( !StringUtils.isBlank(form.getOption()) ) {
					this.readOptions(form);
				}
			}
		}
		super.onBindAndValidate(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		super.postProcessPage(request, command, errors, page);
	}

	private void readOptions(CustomFieldForm form){
		String str;
		BufferedReader reader = new BufferedReader(new StringReader(form.getOption()));

		try {
			List<String> optionList = new ArrayList<String>();
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0){
					if(!StringUtils.isBlank(str)) {
						optionList.add(str);
					}
				}
			}
			form.setOptionList(optionList);
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@SuppressWarnings("static-access")
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		
		CustomFieldForm form = (CustomFieldForm)command;
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		if( form.getEntity()!=null){
			if( form.getEntity().equalsIgnoreCase(form.CUSTOMER) )
				context.put("entity", form.getEntity().toLowerCase());
			else if( form.getEntity().equalsIgnoreCase(form.RESELLER) )
				context.put("entity", form.getEntity().toLowerCase());
		}
		return new ModelAndView(redirectTemplate,"context",context );
		
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@SuppressWarnings("static-access")
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3)
	throws Exception {

		Customer customer=null;
		Distributor distributor=null;
		
		//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<Object, Object> context = new HashMap<Object, Object>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomFieldForm form = (CustomFieldForm)command;
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			customer = details.getCurrentCustomer();
			if(customer != null ){
				//LMS-18300
				//Not a recommended should be lazy fetch
				customer = customerService.getCustomerById(customer.getId());
				form.setEntity(form.CUSTOMER);
			}
			if( details.getCurrentDistributor() != null ){
				distributor= details.getCurrentDistributor();
				form.setEntity(form.RESELLER);
			}
		}

		CustomField customField = form.getCustomField();
		if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
			form.setCustomer(customer);
			if(form.getCustomer().getCustomFields()==null)
				form.getCustomer().setCustomFields(new ArrayList<CustomField>());
		}else if(form.getEntity().equalsIgnoreCase(form.RESELLER)) {
			form.setDistributor(distributor);
			if(form.getDistributor().getCustomFields()==null)
				form.getDistributor().setCustomFields(new ArrayList<CustomField>());
		}
		
		if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_SINGLE_LINE_OF_TEXT)) {
			customField.setFieldType("SINGLELINETEXTCUSTOMFIELD");
			SingleLineTextCustomFiled singleLineTextCustomFiled = new SingleLineTextCustomFiled();
			singleLineTextCustomFiled.setCustomFieldDescription(customField.getCustomFieldDescription());
			singleLineTextCustomFiled.setFieldEncrypted(customField.getFieldEncrypted());
			singleLineTextCustomFiled.setFieldLabel(customField.getFieldLabel().trim());
			singleLineTextCustomFiled.setFieldRequired(customField.getFieldRequired());
			//singleLineTextCustomFiled.setCustomFieldContext(customFieldContext);
			if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
				form.getCustomer().getCustomFields().add(singleLineTextCustomFiled);
			}else if(form.getEntity().equalsIgnoreCase(form.RESELLER)) {
				form.getDistributor().getCustomFields().add(singleLineTextCustomFiled);
			} 
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_DATE)) {
			customField.setFieldType("DATETIMECUSTOMFIELD");
			DateTimeCustomField dateTimeCustomField =  new DateTimeCustomField();
			dateTimeCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			dateTimeCustomField.setFieldEncrypted(customField.getFieldEncrypted());
			dateTimeCustomField.setFieldLabel(customField.getFieldLabel().trim());
			dateTimeCustomField.setFieldRequired(customField.getFieldRequired());
			//dateTimeCustomField.setCustomFieldContext(customFieldContext);
			if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
				form.getCustomer().getCustomFields().add(dateTimeCustomField);
			}else if(form.getEntity().equalsIgnoreCase(form.RESELLER)) {
				form.getDistributor().getCustomFields().add(dateTimeCustomField);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT)) {
			customField.setFieldType("MULTIPLELINETEXTCUSTOMFIELD");
			MultipleLineTextCustomfield multipleLineTextCustomfield = new MultipleLineTextCustomfield();
			multipleLineTextCustomfield.setCustomFieldDescription(customField.getCustomFieldDescription());
			multipleLineTextCustomfield.setFieldEncrypted(customField.getFieldEncrypted());
			multipleLineTextCustomfield.setFieldLabel(customField.getFieldLabel().trim());
			multipleLineTextCustomfield.setFieldRequired(customField.getFieldRequired());
			//multipleLineTextCustomfield.setCustomFieldContext(customFieldContext);
			if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
				form.getCustomer().getCustomFields().add(multipleLineTextCustomfield);
			}else if(form.getEntity().equalsIgnoreCase(form.RESELLER)) {
				form.getDistributor().getCustomFields().add(multipleLineTextCustomfield);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_NUMBER)) {
			customField.setFieldType("NUMERICCUSTOMFIELD");
			NumericCusomField numericCusomField = new NumericCusomField();
			numericCusomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			numericCusomField.setFieldEncrypted(customField.getFieldEncrypted());
			numericCusomField.setFieldLabel(customField.getFieldLabel().trim());
			numericCusomField.setFieldRequired(customField.getFieldRequired());
			//numericCusomField.setCustomFieldContext(customFieldContext);
			if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
				form.getCustomer().getCustomFields().add(numericCusomField);
			}else if(form.getEntity().equalsIgnoreCase(form.RESELLER)) {
				form.getDistributor().getCustomFields().add(numericCusomField);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_SOCIAL_SECURITY_NUMBER)) {
			customField.setFieldType("SSNCUSTOMFIELD");
			SSNCustomFiled ssnCustomFiled = new SSNCustomFiled();
			ssnCustomFiled.setCustomFieldDescription(customField.getCustomFieldDescription());
			ssnCustomFiled.setFieldEncrypted(customField.getFieldEncrypted());
			ssnCustomFiled.setFieldLabel(customField.getFieldLabel().trim());
			ssnCustomFiled.setFieldRequired(customField.getFieldRequired());
			//ssnCustomFiled.setCustomFieldContext(customFieldContext);
			if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
				form.getCustomer().getCustomFields().add(ssnCustomFiled);
			}else if(form.getEntity().equalsIgnoreCase(form.RESELLER)) {
				form.getDistributor().getCustomFields().add(ssnCustomFiled);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_RADIO_BUTTON)) {
			customField.setFieldType(CUSTOMFIELD_RADIO_BUTTON.toUpperCase());
			SingleSelectCustomField singleSelectCustomField = new SingleSelectCustomField();
			singleSelectCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			singleSelectCustomField.setFieldEncrypted(customField.getFieldEncrypted());
			singleSelectCustomField.setFieldLabel(customField.getFieldLabel().trim());
			//singleSelectCustomField.setCustomFieldContext(customFieldContext);
			singleSelectCustomField.setFieldRequired(customField.getFieldRequired());
			if (form.isAlignment())
				singleSelectCustomField.setAlignment(singleSelectCustomField.HORIZONTAL);
			else
				singleSelectCustomField.setAlignment(singleSelectCustomField.VERTICAL);
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(singleSelectCustomField);
				//accreditationService.saveOption(option);
				customFieldService.saveOption(option);
			}
			if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
				form.getCustomer().getCustomFields().add(singleSelectCustomField);
			}else if(form.getEntity().equalsIgnoreCase(form.RESELLER)) {
				form.getDistributor().getCustomFields().add(singleSelectCustomField);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHOOSE)) {
			customField.setFieldType(CUSTOMFIELD_CHOOSE.toUpperCase());
			MultiSelectCustomField multiSelectCustomField = new MultiSelectCustomField();
			multiSelectCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			multiSelectCustomField.setFieldEncrypted(customField.getFieldEncrypted());
			multiSelectCustomField.setFieldRequired(customField.getFieldRequired());
			multiSelectCustomField.setFieldLabel(customField.getFieldLabel().trim());
			//multiSelectCustomField.setCustomFieldContext(customFieldContext);
			multiSelectCustomField.setCheckBox(false);
			if (form.isAlignment())
				multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
			else
				multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(multiSelectCustomField);
				//accreditationService.saveOption(option);
				customFieldService.saveOption(option);
			}
			if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
				form.getCustomer().getCustomFields().add(multiSelectCustomField);
			}else if(form.getEntity().equalsIgnoreCase(form.RESELLER)) {
				form.getDistributor().getCustomFields().add(multiSelectCustomField);
			}
		} else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHECK_BOX)) {
			customField.setFieldType(CUSTOMFIELD_CHECK_BOX.toUpperCase());
			MultiSelectCustomField multiSelectCustomField = new MultiSelectCustomField();
			multiSelectCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
			multiSelectCustomField.setFieldEncrypted(customField.getFieldEncrypted());
			multiSelectCustomField.setFieldRequired(customField.getFieldRequired());
			multiSelectCustomField.setFieldLabel(customField.getFieldLabel().trim());
			//multiSelectCustomField.setCustomFieldContext(customFieldContext);
			multiSelectCustomField.setCheckBox(true);
			if (form.isAlignment())
				multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
			else
				multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(multiSelectCustomField);
				//accreditationService.saveOption(option);
				customFieldService.saveOption(option);
			}
			if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
				form.getCustomer().getCustomFields().add(multiSelectCustomField);
			}else if(form.getEntity().equalsIgnoreCase(form.RESELLER)) {
				form.getDistributor().getCustomFields().add(multiSelectCustomField);
			} 
		}


			
		if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
			for( CustomField cf : form.getCustomer().getCustomFields() ){
				if( cf.getFieldLabel().equals( customField.getFieldLabel()))
					cf.setFieldType(customField.getFieldType());
			}
			
			//LMS-18300
			Customer updatedCustomer=customerService.loadForUpdateCustomer(form.getCustomer().getId());
			List<CustomField> custif = form.getCustomer().getCustomFields();
			custif = customFieldService.save(custif);
			updatedCustomer.setCustomFields(custif);
			customerService.updateCustomer(updatedCustomer);
			
		}else if(form.getEntity().equalsIgnoreCase(form.RESELLER)) {
			for( CustomField cf : form.getDistributor().getCustomFields() ){
				if( cf.getFieldLabel().equals( customField.getFieldLabel()) )
					cf.setFieldType(customField.getFieldType());
			}
//			Distributor updatedDistributor=distributorService.getDistributorById(form.getDistributor().getId());
//			updatedDistributor.setCustomFields(form.getDistributor().getCustomFields());
//			distributorService.saveDistributor(updatedDistributor);
			//Distributor updatedDistributor=distributorService.getDistributorById(form.getDistributor().getId());
			
			//LMS-18319
			List<CustomField> dcustif = form.getDistributor().getCustomFields();
			dcustif = customFieldService.save(dcustif);
			form.getDistributor().setCustomFields(dcustif);
			//distributorService.updateDistributor(form.getDistributor());
		} 
		
		/*
		 * if (form.getEntity().equalsIgnoreCase(form.CUSTOMER)) {
		 
			accreditationService.saveCredential(form.getCredential());
			return new ModelAndView(closeCredentialTemplate);
		} else if (form.getEntity().equalsIgnoreCase(form.RESELLER)) {
			accreditationService.saveRegulator(form.getDistributor());
			return new ModelAndView(closeRegulatorTemplate);
		} 
		*/

		
		if( form.getEntity().equalsIgnoreCase(form.CUSTOMER) )
			context.put("entity", form.getEntity().toLowerCase());
		else if( form.getEntity().equalsIgnoreCase(form.RESELLER) )
			context.put("entity", form.getEntity().toLowerCase());
		
		return new ModelAndView(redirectTemplate,"context",context );


	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	@SuppressWarnings("unchecked")
	protected Map<Object,Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		Map<Object,Object> model = new HashMap<Object,Object>();
		switch(page){
		case 0:
			model.put("customFieldTypes", CUSTOMFIELD_TYPES);
			return model;
		case 1:
			break;
		case 2:
			break;
		}
		return super.referenceData(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		CustomFieldValidator validator = (CustomFieldValidator)this.getValidator();
		CustomFieldForm form = (CustomFieldForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			break;
		case 1:
			validator.validateAddCustomFieldPage(form, errors);
			break;
		case 2:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	/**
	 * @return the accreditationService
	 */
//	public AccreditationService getAccreditationService() {
//		return accreditationService;
//	}

	/**
	 * @param accreditationService the accreditationService to set
	 */
//	public void setAccreditationService(AccreditationService accreditationService) {
//		this.accreditationService = accreditationService;
//	}

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

	public CustomFieldService getCustomFieldService() {
		return customFieldService;
	}

	public void setCustomFieldService(CustomFieldService customFieldService) {
		this.customFieldService = customFieldService;
	}

	public String getSuccessfulTemplate() {
		return successfulTemplate;
	}

	public void setSuccessfulTemplate(String successfulTemplate) {
		this.successfulTemplate = successfulTemplate;
	}
	
}