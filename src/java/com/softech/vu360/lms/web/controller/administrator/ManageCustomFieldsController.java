package com.softech.vu360.lms.web.controller.administrator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

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
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.CustomFieldVO;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldForm;
import com.softech.vu360.lms.web.controller.util.CustomFieldFormUtil;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.RedirectUtils;

/**
 * @author Dyutiman
 * created on 23 June 2009
 * 
 */
public class ManageCustomFieldsController extends VU360BaseMultiActionController {
	private String failureTemplate = null;
	
	private static final Logger log = Logger.getLogger(ManageCustomFieldsController.class.getName());

	public static final String CUSTOMFIELD_ENTITY_REGULATOR = "CUSTOMFIELD_REGULATOR";

	private AccreditationService accreditationService;
	private CustomFieldService customFieldService;
	private DistributorService distributorService;
	private CustomerService customerService; 

	HttpSession session = null;

	private String showCustomFieldsTemplate = null;
	private String showCustomFieldsDeletionSummaryTemplate = null;
	private String defaultCustomFieldTemplate = null;
	
	public ManageCustomFieldsController() {
		super();
	}

	public ManageCustomFieldsController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {

		log.debug(" IN handleNoSuch Method ");
		return new ModelAndView("");//searchRegulatorTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		if(command instanceof CustomFieldForm){
			CustomFieldForm form = (CustomFieldForm)command;
			if(methodName.equals("displayCustomFieldForEdit")){
				CustomField customField = accreditationService.loadForUpdateCustomField(form.getCustomFieldId());
                                boolean customFieldHasValue = accreditationService.customFieldHasValue(customField);
                                
				form.setCustomField(customField);
                                if(customFieldHasValue){
                                    form.setFieldsDisabled(true);
                                }else{
                                    form.setFieldsDisabled(false);
                                }
				
				//form.setEntity(this.getEntity(customField));
				/*set entity type for  custom fields that are not global*/
				if(form.getEntity().length() == 0 && request.getParameter("entity")!= null){
                                    form.setEntity(request.getParameter("entity"));
				}

			}
		}
                
		if(command instanceof CustomFieldForm){
			CustomFieldForm form = (CustomFieldForm)command;
			if(methodName.equals("displaySurveys")) {

			}
		}

	}


	public ModelAndView showCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		

		Map<Object, Object> context = new HashMap<Object, Object>();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String type = "";
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();	
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();
			type = details.getCurrentSearchType().toString();

			
				
				List<CustomField> lstCustomFields = null ; 
					
				String entity = "";
				
				if (request.getParameter("entity") != null)
					entity = request.getParameter("entity");
				
				if(entity == null){
					entity = "";
				}
				//----LMS-9774 - restriction added. Custom field cannot added without selection of Customer or Reseller
				if( entity.equalsIgnoreCase(CustomFieldForm.CUSTOMER) 
						&& type.equalsIgnoreCase(CustomFieldForm.CUSTOMER)) 
				{
					if(vu360AuthDetails.getCurrentCustomer() != null ) 
					{
						lstCustomFields = getCustomerCustomFields();
						context.put("title", "Customer Custom Fields");
						context.put("description", "Below is the list of customer custom fields");
						context.put("type", "customer");	
						//KS - 2009-15-12 - LMS-3545 - Change in implementation made as per FS
						List<CustomFieldVO> customFieldsList = transform(lstCustomFields);
						context.put("customFieldsList", customFieldsList);
					}
					else
						return new ModelAndView(failureTemplate,"isRedirect","c");
				}
				else if(entity.equalsIgnoreCase(CustomFieldForm.RESELLER) 
						&& type.equalsIgnoreCase("distributor") ) 
				{
					if( vu360AuthDetails.getCurrentDistributor() != null ) 
					{
						lstCustomFields = getResellerCustomFields();
						context.put("title", "Reseller Custom Fields");
						context.put("description", "Below is the list of reseller custom fields");
						context.put("type", "reseller");		
						//KS - 2009-15-12 - LMS-3545 - Change in implementation made as per FS
						List<CustomFieldVO> customFieldsList = transform(lstCustomFields);
						context.put("customFieldsList", customFieldsList);
					}
					else
						throw new IllegalArgumentException("Distributor not found");
				}
				else{
					if(entity.equalsIgnoreCase(CustomFieldForm.RESELLER) )
							throw new IllegalArgumentException("Distributor not found");
					else if(entity.equalsIgnoreCase(CustomFieldForm.CUSTOMER))
							return new ModelAndView(failureTemplate,"isRedirect","c");
					else
							return new ModelAndView(failureTemplate,"isRedirect","");
				}
				return new ModelAndView(showCustomFieldsTemplate, "context", context);
			
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
	}
	
	@SuppressWarnings("static-access")
	public ModelAndView displayCustomFieldForEdit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CustomFieldForm form = (CustomFieldForm)command;
		CustomField customField = form.getCustomField();
		if (customField instanceof SingleLineTextCustomFiled) {
			SingleLineTextCustomFiled singleLineTextCustomFiled = (SingleLineTextCustomFiled)form.getCustomField();
			form.setFieldType(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
			form.setFieldLabel(singleLineTextCustomFiled.getFieldLabel());
			form.setFieldEncrypted(singleLineTextCustomFiled.getFieldEncrypted());
			form.setFieldRequired(singleLineTextCustomFiled.getFieldRequired());
			form.setCustomFieldDescription(singleLineTextCustomFiled.getCustomFieldDescription());
		} else if (customField instanceof DateTimeCustomField) {
			DateTimeCustomField dateTimeCustomField = (DateTimeCustomField)form.getCustomField();
			form.setFieldLabel(dateTimeCustomField.getFieldLabel());
			form.setFieldEncrypted(dateTimeCustomField.getFieldEncrypted());
			form.setFieldRequired(dateTimeCustomField.getFieldRequired());
			form.setCustomFieldDescription(dateTimeCustomField.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_DATE);
		} else if (customField instanceof MultipleLineTextCustomfield) {
			MultipleLineTextCustomfield multipleLineTextCustomfield = (MultipleLineTextCustomfield)form.getCustomField();
			form.setFieldLabel(multipleLineTextCustomfield.getFieldLabel());
			form.setFieldEncrypted(multipleLineTextCustomfield.getFieldEncrypted());
			form.setFieldRequired(multipleLineTextCustomfield.getFieldRequired());
			form.setCustomFieldDescription(multipleLineTextCustomfield.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
		} else if (customField instanceof NumericCusomField) {
			NumericCusomField numericCusomField = (NumericCusomField)form.getCustomField();
			form.setFieldLabel(numericCusomField.getFieldLabel());
			form.setFieldEncrypted(numericCusomField.getFieldEncrypted());
			form.setFieldRequired(numericCusomField.getFieldRequired());
			form.setCustomFieldDescription(numericCusomField.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_NUMBER);
		} else if (customField instanceof SSNCustomFiled) {
			SSNCustomFiled sSNCustomFiled = (SSNCustomFiled)form.getCustomField();
			form.setFieldLabel(sSNCustomFiled.getFieldLabel());
			form.setFieldEncrypted(sSNCustomFiled.getFieldEncrypted());
			form.setFieldRequired(sSNCustomFiled.getFieldRequired());
			form.setCustomFieldDescription(sSNCustomFiled.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
		} else if (customField instanceof SingleSelectCustomField) {
			SingleSelectCustomField singleSelectCustomField = (SingleSelectCustomField)form.getCustomField();
			form.setFieldLabel(singleSelectCustomField.getFieldLabel());
			form.setFieldEncrypted(singleSelectCustomField.getFieldEncrypted());
			form.setFieldRequired(singleSelectCustomField.getFieldRequired());
			List<CustomFieldValueChoice> options = accreditationService.getOptionsByCustomField(singleSelectCustomField);
			form.setOptions(options);
			form.setOption(this.getOption(options));
			if(StringUtils.isNotBlank(singleSelectCustomField.getAlignment())){
				if (singleSelectCustomField.getAlignment().equalsIgnoreCase(singleSelectCustomField.VERTICAL))
					form.setAlignment(false);
			}
			form.setCustomFieldDescription(singleSelectCustomField.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_RADIO_BUTTON);
		} else if (customField instanceof MultiSelectCustomField) {
			MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)form.getCustomField();
			form.setFieldLabel(multiSelectCustomField.getFieldLabel());
			form.setFieldEncrypted(multiSelectCustomField.getFieldEncrypted());
			form.setFieldRequired(multiSelectCustomField.getFieldRequired());
			List<CustomFieldValueChoice> options = accreditationService.getOptionsByCustomField(multiSelectCustomField);
			form.setOptions(options);
			form.setOption(this.getOption(options));
			if(StringUtils.isNotBlank(multiSelectCustomField.getAlignment())){
				if (multiSelectCustomField.getAlignment().equalsIgnoreCase(multiSelectCustomField.VERTICAL))
					form.setAlignment(false);
			}
			form.setCustomFieldDescription(multiSelectCustomField.getCustomFieldDescription());
			if (multiSelectCustomField.getCheckBox()){
				form.setFieldType(form.CUSTOMFIELD_CHECK_BOX);
			}else{
				form.setFieldType(form.CUSTOMFIELD_CHOOSE);
			}
		} 
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("type", form.getEntity());
		
		return new ModelAndView(defaultCustomFieldTemplate,"context", context);
	}
	
	@SuppressWarnings("static-access")
	public ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CustomFieldForm form = (CustomFieldForm)command;
		return new ModelAndView("redirect:adm_manageCustomField.do?method=showCustomField&entity="+form.getEntity());
	}
        
        @SuppressWarnings("static-access")
	public ModelAndView saveCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
            
            CustomFieldForm form = (CustomFieldForm)command;
            CustomField customField = CustomFieldFormUtil.processCustomField(form, accreditationService);
            
            return new ModelAndView("redirect:adm_manageCustomField.do?method=showCustomField&entity="+form.getEntity());
        }
        
	
	private String getOption(List<CustomFieldValueChoice> optionList) {
		String optionString="";
		//Collections.sort(optionList);
		for (CustomFieldValueChoice option : optionList) {
			optionString = optionString + option.getLabel() + "\n";
		}
		return optionString;
	}
	
	private List<CustomField> getCustomerCustomFields(){
		
		List<CustomField> lstCustomFields =null; 
			
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			if( details.getCurrentCustomer() != null ){
				customer = details.getCurrentCustomer();
				lstCustomFields = customer.getCustomFields();
			}
		}
		
		return lstCustomFields ;
	}
	
	private List<CustomField> getResellerCustomFields(){
		
		List<CustomField> lstCustomFields =null; 
			
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 
		
		Distributor distributor = null;
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			if( details.getCurrentDistributor() != null ){
				distributor = details.getCurrentDistributor();
				
				/*
				 * LMS-13475
				 * Session Replication our all models having dependency with toplink that is why we have to do this.
				 */
				if(isNullModel(distributor)){
					Distributor distributor_ = distributorService.getDistributorById(distributor.getId());
					lstCustomFields = distributor_.getCustomFields();
				}else{
					lstCustomFields = distributor.getCustomFields();
				}
			}
		}
		return lstCustomFields ;
	}
	
	
	/**
	 * 
	 * @param distributor
	 * @return
	 */
	private boolean isNullModel(Distributor  distributor){
		boolean isCheck=false;
		try{
			List<CustomField> ls = distributor.getCustomFields();
			if(ls.isEmpty()) 
				isCheck=true;
		}catch(NullPointerException e){
			isCheck=true;
		}
		return isCheck;
	}
	
	public ModelAndView deleteCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		String entity="";
		if (request.getParameter("entity") != null)
			entity = request.getParameter("entity");
		
		List<CustomField> usedCustomFields = new ArrayList<CustomField>();
		List<CustomField> unUsedCustomFields = new ArrayList<CustomField>();
		String[] strIdsToDelete=request.getParameterValues("customfield");
		if  ( strIdsToDelete!=null && strIdsToDelete.length>0) {
			Long[] ids = new Long[strIdsToDelete.length];
			for ( int i = 0 ; i < strIdsToDelete.length ; i++ ) {
				ids[i] = Long.valueOf(strIdsToDelete[i]);
			}
			usedCustomFields = customFieldService.getUsedCustomFieldsList(ids);
			unUsedCustomFields = customFieldService.getUnUsedCustomFields(ids ,usedCustomFields );
			if(unUsedCustomFields!=null && unUsedCustomFields.size()>0){
				usedCustomFields = removerepeatingValuesFromList(usedCustomFields) ;
				unUsedCustomFields = removerepeatingValuesFromList(unUsedCustomFields) ;
				
				Long[] unUsedCustomFieldIds = new Long[unUsedCustomFields.size()];
				int count = 0 ;
				for( CustomField cf :unUsedCustomFields ){
					unUsedCustomFieldIds[count] =  cf.getId();
					count++;
				}
				 
				if( entity.equalsIgnoreCase(CustomFieldForm.CUSTOMER)) {
					Customer customer=this.getSelectedCustomer();
					customFieldService.deleteCustomerCustomFields(customer, unUsedCustomFieldIds);
				} else if(entity.equalsIgnoreCase(CustomFieldForm.RESELLER)) {
					Distributor distributor=this.getSelectedDistributor();
					customFieldService.deleteDistributorCustomFields(distributor, unUsedCustomFieldIds);
				}		
				
			}
		}
		Map<String,Object> context=new HashMap<String,Object>();
		context.put("usedCustomFields", usedCustomFields);
		context.put("unUsedCustomFields", unUsedCustomFields);
		
		if (request.getParameter("entity") != null)
			entity = request.getParameter("entity");
		
		if( entity.equalsIgnoreCase(CustomFieldForm.CUSTOMER)) {
			context.put("title", "Customer Custom Fields");
			context.put("description", "Below is the list of customer custom fields");
			context.put("type", "customer");	
		}
		else if(entity.equalsIgnoreCase(CustomFieldForm.RESELLER)) {
			context.put("title", "Reseller Custom Fields");
			context.put("description", "Below is the list of reseller custom fields");
			context.put("type", "reseller");			
		}
		return new ModelAndView(showCustomFieldsDeletionSummaryTemplate,"context",context );
	}

	protected void validate( HttpServletRequest request, Object command,
			BindException errors, String methodName ) throws Exception {

		
	}

	private List<CustomField> removerepeatingValuesFromList(List<CustomField> aList){
		List<CustomField> distinctList = new ArrayList<CustomField>();
		int existCount = 0;
		//long objId = 0;
		for( CustomField cf : aList ) {
			existCount = 0 ;
			//objId = cf.getId().longValue();
			for(CustomField cfr : distinctList ) {
				 
				if( cfr.getId().equals( cf.getId()) )
					++existCount;
				
			}
			if(existCount == 0 )
				distinctList.add(cf);
		}
		return distinctList;
	}
	/*
	 * getters & setters 
	 */

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	/**
	 * @return the showCustomFieldsTemplate
	 */
	public String getShowCustomFieldsTemplate() {
		return showCustomFieldsTemplate;
	}

	/**
	 * @param showCustomFieldsTemplate the showCustomFieldsTemplate to set
	 */
	public void setShowCustomFieldsTemplate(String showCustomFieldsTemplate) {
		this.showCustomFieldsTemplate = showCustomFieldsTemplate;
	}

	public CustomFieldService getCustomFieldService() {
		return customFieldService;
	}

	public void setCustomFieldService(CustomFieldService customFieldService) {
		this.customFieldService = customFieldService;
	}

	public Customer getSelectedCustomer()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			customer = details.getCurrentCustomer();
		}
		return customer;
	}

	public Distributor getSelectedDistributor()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Distributor distributor = null;
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			distributor = details.getCurrentDistributor();
			//LMS-18300 Not a recommended
			//distributor = distributorService.getDistributorById(distributor.getId());
		}
		return distributor;
	}

	/**
	 * @return the showCustomFieldsDeletionSummaryTemplate
	 */
	public String getShowCustomFieldsDeletionSummaryTemplate() {
		return showCustomFieldsDeletionSummaryTemplate;
	}

	/**
	 * @param showCustomFieldsDeletionSummaryTemplate the showCustomFieldsDeletionSummaryTemplate to set
	 */
	public void setShowCustomFieldsDeletionSummaryTemplate(
			String showCustomFieldsDeletionSummaryTemplate) {
		this.showCustomFieldsDeletionSummaryTemplate = showCustomFieldsDeletionSummaryTemplate;
	}


	//KS - 2009-15-12 - LMS-3545 - Change in implementation made as per FS
	private String getFieldType(CustomField customField){
		if(customField instanceof SingleLineTextCustomFiled){
			return CustomFieldForm.CUSTOMFIELD_SINGLE_LINE_OF_TEXT;
		}else if(customField instanceof MultipleLineTextCustomfield){
			return CustomFieldForm.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT;
		}else if(customField instanceof NumericCusomField){
			return CustomFieldForm.CUSTOMFIELD_NUMBER;
		}else if(customField instanceof DateTimeCustomField){
			return CustomFieldForm.CUSTOMFIELD_DATE;
		}else if (customField instanceof SingleSelectCustomField){
			return CustomFieldForm.CUSTOMFIELD_RADIO_BUTTON;
		}else if (customField instanceof MultiSelectCustomField){
			MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField;
			if (multiSelectCustomField.getCheckBox()){
				return CustomFieldForm.CUSTOMFIELD_CHECK_BOX;
			}else {
				return CustomFieldForm.CUSTOMFIELD_CHOOSE;
			}
		}else if (customField instanceof SSNCustomFiled){
			return CustomFieldForm.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER;
		}
		return "";
	}
	
	/**
	 * 	@author khurram.shehzad
	 *  
	 *  @param transform Transform CustomField(Model object) into CustomFieldVO (Value Object)
	 *  
	 */
	public List<CustomFieldVO> transform(List<CustomField> customFieldList) {
		
		List<CustomFieldVO> customFieldVOList = new ArrayList<CustomFieldVO>();
		if( customFieldList != null && !customFieldList.isEmpty()){
			
			for(CustomField customField : customFieldList){
				
				CustomFieldVO customFieldVO = new CustomFieldVO();
				
				customFieldVO.setId( customField.getId() );
				customFieldVO.setFieldLabel( customField.getFieldLabel() );
				customFieldVO.setFieldType( this.getFieldType(customField) );
				
				customFieldVOList.add( customFieldVO );
			}
		}

		return customFieldVOList;
	}

	/**
	 * exception handler method for this multiaction
	 * to be used if the distributor could not be resolved
	 */
	public void handleDistributorNotFoundException(HttpServletRequest request, HttpServletResponse response, IllegalArgumentException ex ) throws IOException{
		String url = "/adm_searchMember.do?method=displaySearchMemberView&isRedirect=d";
		RedirectUtils.sendRedirect(request, response, url, false);
	}
	
	public String getFailureTemplate() {
		return failureTemplate;
	}

	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}

	public String getDefaultCustomFieldTemplate() {
		return defaultCustomFieldTemplate;
	}

	public void setDefaultCustomFieldTemplate(String defaultCustomFieldTemplate) {
		this.defaultCustomFieldTemplate = defaultCustomFieldTemplate;
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
	
}