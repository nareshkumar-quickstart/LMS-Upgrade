package com.softech.vu360.lms.web.controller.accreditation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Contact;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.RegulatoryAnalystService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.DocumentForm;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;
import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorContact;
import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorCredential;
import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorForm;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.Accreditation.EditRegulatorValidator;
import com.softech.vu360.util.ContactSort;
import com.softech.vu360.util.CredentialSort;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.CustomFieldSort;
import com.softech.vu360.util.DocumentSort;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.RegulatorCategorySort;
import com.softech.vu360.util.RegulatorSort;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Dyutiman
 * created on 23 June 2009
 * 
 */
public class ManageAndEditRegulatorController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageAndEditRegulatorController.class.getName());

	public static final String CUSTOMFIELD_ENTITY_REGULATOR = "CUSTOMFIELD_REGULATOR";

	private AccreditationService accreditationService;
	@Inject
	private RegulatoryAnalystService regulatoryAnalystService;

	private String searchRegulatorTemplate = null;
	private String editRegulatorSummaryTemplate = null;
	private String editRegulatorCredentialTemplate = null;
	private String editRegulatorContactTemplate = null;
	private String editRegulatorDocumentTemplate = null;
	private String editRegulatorEditContactTemplate = null;
	private String editRegulatorCustomFieldTemplate = null;
	private String editRegulatorEditDocumentTemplate = null; 
	private String redirectEditRegulatorEditDocumentTemplate = null;
	private String redirectEditRegulatorEditContactTemplate = null;
	private String editRegulatorRedirectTemplate = null;
	private String listRegulatorCategoriesTemplate= null;

	public ManageAndEditRegulatorController() {
		super();
	}

	public ManageAndEditRegulatorController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
		request.setAttribute("newPage","true");

		log.debug(" IN handleNoSuch Method ");
		return new ModelAndView(searchRegulatorTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {

		if( command instanceof RegulatorForm ){
			RegulatorForm form = (RegulatorForm)command;
			if( methodName.equals("editRegulatorSummary")
					|| methodName.equals("editRegulatorCredential")
					|| methodName.equals("editRegulatorContact")
					|| methodName.equals("editRegulatorDocument")
					|| methodName.equals("searchRegulatorCategories")
					|| methodName.equals("listRegulatorCategories")					
					|| methodName.equals("editRegulatorCustomField")) {

				Regulator regulator = null;
				String id = request.getParameter("id");
				if( id != null ) {
					regulator = accreditationService.loadForUpdateRegulator(Long.parseLong(id));
					regulator.setAddress(regulator.getAddress()==null?new Address():regulator.getAddress());
					regulator.setAddress2(regulator.getAddress2()==null?new Address():regulator.getAddress2());
					form.setRID(Long.parseLong(id));
					form.setRegulator(regulator);
					form.setCategories(accreditationService.findRegulatorCategories("","",form.getRID()));

				} else if(form != null && form.getRID() > 0){
					regulator = accreditationService.loadForUpdateRegulator(form.getRID());
					if(regulator != null){
						regulator.setAddress(regulator.getAddress()==null?new Address():regulator.getAddress());
						regulator.setAddress2(regulator.getAddress2()==null?new Address():regulator.getAddress2());
						form.setRegulator(regulator);
					}
					
					form.setCategories(accreditationService.findRegulatorCategories("","",form.getRID()));
				}
				
				form.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));

			} else if( methodName.equals("searchRegulatorCredential")
					|| methodName.equals("searchRegulatorContact") 
					|| methodName.equals("searchRegulatorDocument") 
					|| methodName.equals("searchRegulatorCustomField") 
					|| methodName.equals("deleteRegulatorCustomField") 
					|| methodName.equals("showCustomField") 
					|| methodName.equals("deleteRegulatorCredential")
					|| methodName.equals("deleteRegulatorDocument")
					|| methodName.equals("deleteRegulatorContact") ) {
				// refreshing the regulator
				if( form.getRegulator() != null )
					form.setRegulator(accreditationService.loadForUpdateRegulator(form.getRegulator().getId()));

			}else if(methodName.equals("editRegulatorEditContact")) {
				String contactId = request.getParameter("id");
				Contact contact= accreditationService.loadForUpdateContact(Long.parseLong(contactId)); 
				contact.setAddress(contact.getAddress()==null?new Address():contact.getAddress());
				contact.setAddress2(contact.getAddress2()==null?new Address():contact.getAddress2());
				form.setContact(contact);				
				
			}else if(methodName.equals("editRegulatorEditDocument")) {
				String documentId = request.getParameter("id");
				form.setDocument(accreditationService.loadForUpdateDocument(Long.parseLong(documentId)));
			}
			if( methodName.equals("editRegulatorSummary")){

				Regulator regulator = form.getRegulator();
				if (regulator != null){

					CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
					List<CustomFieldValue> customFieldValues = regulator.getCustomfieldValues();
					List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
					List<CustomField> regulatorCustomFieldList = regulator.getCustomfields();
					List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_REGULATOR), "", "");

					for (CustomField globalCustomField : globalCustomFieldList){
						totalCustomFieldList.add(globalCustomField);
					}
					for (CustomField customField : regulatorCustomFieldList){
						totalCustomFieldList.add(customField);
					}

					Map<Long,List<CustomFieldValueChoice>> existingCustomFieldValueChoiceMap = new HashMap<Long,List<CustomFieldValueChoice>>();

					for(CustomField customField:totalCustomFieldList){
						if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField ){
							List<CustomFieldValueChoice> customFieldValueChoices=accreditationService.getOptionsByCustomField(customField);
							fieldBuilder.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);

							if (customField instanceof MultiSelectCustomField){
								CustomFieldValue customFieldValue=this.getCustomFieldValueByCustomField(customField, customFieldValues);
								existingCustomFieldValueChoiceMap.put(customField.getId(), customFieldValue.getValueItems());
							}

						}else {
							fieldBuilder.buildCustomField(customField,0,customFieldValues);
						}
					}

					List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();

					for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField:customFieldList){
						if (customField.getCustomFieldRef() instanceof MultiSelectCustomField ){
							List<CustomFieldValueChoice> existingCustomFieldValueChoiceList = existingCustomFieldValueChoiceMap.get(customField.getCustomFieldRef().getId());
							Map<Long,CustomFieldValueChoice> tempChoiceMap = new HashMap<Long,CustomFieldValueChoice>();

							for(CustomFieldValueChoice customFieldValueChoice :existingCustomFieldValueChoiceList){
								tempChoiceMap.put(customFieldValueChoice.getId(), customFieldValueChoice);
							}

							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField.getCustomFieldValueChoices()){
								if (tempChoiceMap.containsKey(customFieldValueChoice.getCustomFieldValueChoiceRef().getId())){
									customFieldValueChoice.setSelected(true);
								}
							}
						}
					}
					form.setCustomFields(customFieldList);
				}
			}
		}
	}

	private CustomFieldValue getCustomFieldValueByCustomField(com.softech.vu360.lms.model.CustomField customField,List<CustomFieldValue> customFieldValues){
		if (customFieldValues != null){
			for (CustomFieldValue customFieldValue : customFieldValues){
				if (customFieldValue.getCustomField()!=null){
					if (customFieldValue.getCustomField().getId().compareTo(customField.getId())==0){
						return customFieldValue;
					}
				}
			}
		}
		return new CustomFieldValue();
	}


	public ModelAndView searchRegulator( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String name = (request.getParameter("regName") == null) ? "" : request.getParameter("regName");
			String alias = (request.getParameter("regAlias") == null) ? "" : request.getParameter("regAlias");
			String email = (request.getParameter("regEmailAdd") == null) ? "" : request.getParameter("regEmailAdd");

			name = HtmlEncoder.escapeHtmlFull(name).toString();
			context.put("name", name);
			alias = HtmlEncoder.escapeHtmlFull(alias).toString();
			context.put("alias", alias);
			email = HtmlEncoder.escapeHtmlFull(email).toString();
			context.put("email", email);

			List<Regulator> regulators = accreditationService.findRegulator(name, alias, email, 
					regulatoryAnalystService.getRegulatoryAnalystById(loggedInUserVO.getRegulatoryAnalyst().getId()));

			String pageIndex = request.getParameter("pageIndex");
			if( pageIndex == null ) pageIndex = "0";

			PagerAttributeMap.put("pageIndex",pageIndex);
			if( regulators.size() <= 10 ) {
				PagerAttributeMap.put("pageIndex", "0");
			}
			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			String sortDirection = request.getParameter("sortDirection");
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			context.put("showAll", showAll);

			if( sortColumnIndex != null && sortDirection != null ) {

				request.setAttribute("PagerAttributeMap", PagerAttributeMap);

				// sorting against regulator name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						RegulatorSort RegulatorSort = new RegulatorSort();
						RegulatorSort.setSortBy("name");
						RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(regulators,RegulatorSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
					} else {
						RegulatorSort RegulatorSort = new RegulatorSort();
						RegulatorSort.setSortBy("name");
						RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(regulators,RegulatorSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
					}
					// sorting against alias
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						RegulatorSort RegulatorSort = new RegulatorSort();
						RegulatorSort.setSortBy("alias");
						RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(regulators,RegulatorSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 1);
					} else {
						RegulatorSort RegulatorSort=new RegulatorSort();
						RegulatorSort.setSortBy("alias");
						RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(regulators,RegulatorSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 1);
					}
					// sorting against email - address
				} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						RegulatorSort RegulatorSort=new RegulatorSort();
						RegulatorSort.setSortBy("emailAddress");
						RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(regulators,RegulatorSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 2);
					} else {
						RegulatorSort RegulatorSort=new RegulatorSort();
						RegulatorSort.setSortBy("emailAddress");
						RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(regulators,RegulatorSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 2);
					}
					// sorting against jurisdiction...
				} else if( sortColumnIndex.equalsIgnoreCase("3") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						RegulatorSort RegulatorSort=new RegulatorSort();
						RegulatorSort.setSortBy("jurisdiction");
						RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(regulators,RegulatorSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 3);
					} else {
						RegulatorSort RegulatorSort=new RegulatorSort();
						RegulatorSort.setSortBy("jurisdiction");
						RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(regulators,RegulatorSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 3);
					}
				}
			}
			context.put("regulators", regulators);
			List<Contact> allContacts = new ArrayList <Contact>();
			for( Regulator reg : regulators) {
				allContacts.addAll(accreditationService.getContactsByRegulator(reg.getId()));
			}
			if( allContacts != null && allContacts.size()>0) {
				context.put("contact", allContacts.get(0));
			}
			return new ModelAndView(searchRegulatorTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchRegulatorTemplate);
	}


	@SuppressWarnings("static-access")
	public ModelAndView searchRegulatorCustomField( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();

			RegulatorForm form = (RegulatorForm)command;
			List<CustomField> customFields = form.getRegulator().getCustomfields();


			String pageIndex = request.getParameter("pageIndex");
			if( pageIndex == null ) pageIndex = "0";

			PagerAttributeMap.put("pageIndex",pageIndex);
			if( customFields.size() <= 10 ) {
				PagerAttributeMap.put("pageIndex", "0");
			}

			List<ManageCustomField> manageCustomFields = new ArrayList <ManageCustomField>();

			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			String sortDirection = request.getParameter("sortDirection");
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			context.put("showAll", showAll);

			if( sortColumnIndex != null && sortDirection != null ) {

				request.setAttribute("PagerAttributeMap", PagerAttributeMap);
				for( CustomField customField : customFields ) {
					ManageCustomField manageCustomField = new ManageCustomField();
					manageCustomField.setFieldName(customField.getFieldLabel());
					manageCustomField.setSelected(false);
					if (customField instanceof SingleLineTextCustomFiled) {
						manageCustomField.setFieldType(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
					} else if (customField instanceof DateTimeCustomField) {
						manageCustomField.setFieldType(form.CUSTOMFIELD_DATE);
					} else if (customField instanceof MultipleLineTextCustomfield) {
						manageCustomField.setFieldType(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
					} else if (customField instanceof NumericCusomField) {
						manageCustomField.setFieldType(form.CUSTOMFIELD_NUMBER);
					} else if (customField instanceof SSNCustomFiled) {
						manageCustomField.setFieldType(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
					} else if (customField instanceof SingleSelectCustomField) {
						manageCustomField.setFieldType(form.CUSTOMFIELD_RADIO_BUTTON);
					} else if (customField instanceof MultiSelectCustomField) {
						if (((MultiSelectCustomField) customField).getCheckBox())
							manageCustomField.setFieldType(form.CUSTOMFIELD_CHECK_BOX);
						else 
							manageCustomField.setFieldType(form.CUSTOMFIELD_CHOOSE);
					}
					manageCustomFields.add(manageCustomField);
				}

				// sorting against officialLicenseName name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CustomFieldSort customFieldSort = new CustomFieldSort();
						customFieldSort.setSortBy("fieldLabel");
						customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(manageCustomFields,customFieldSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
					} else {
						CustomFieldSort customFieldSort = new CustomFieldSort();
						customFieldSort.setSortBy("fieldLabel");
						customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(manageCustomFields,customFieldSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
					}
					// sorting against jurisdiction
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CustomFieldSort customFieldSort = new CustomFieldSort();
						customFieldSort.setSortBy("fieldType");
						customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(manageCustomFields,customFieldSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 1);
					} else {
						CustomFieldSort customFieldSort = new CustomFieldSort();
						customFieldSort.setSortBy("fieldType");
						customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(manageCustomFields,customFieldSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 1);
					}
				}
			}
			form.setManageCustomField(manageCustomFields);
			return new ModelAndView(editRegulatorCustomFieldTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorCustomFieldTemplate);
	}


	public ModelAndView deleteRegulator( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if  ( request.getParameterValues("regulators") != null ) {
				long[] id = new long[request.getParameterValues("regulators").length];
				String []checkID = request.getParameterValues("regulators");
				for ( int i = 0 ; i < id.length ; i++ ) {
					id[i] = Long.valueOf(checkID[i]);
					Regulator regulator = accreditationService.loadForUpdateRegulator(id[i]);
					regulator.setActive(false);
					accreditationService.saveRegulator(regulator);
				}
				log.debug("TO BE DELETED ::- "+id.length);
			}
			List<Regulator> regulators = accreditationService.findRegulator("", "", "", 
					regulatoryAnalystService.getRegulatoryAnalystById(loggedInUserVO.getRegulatoryAnalyst().getId()));
			context.put("regulators", regulators);
			List<Contact> allContacts = new ArrayList <Contact>();
			for( Regulator reg : regulators) {
				allContacts.addAll(accreditationService.getContactsByRegulator(reg.getId()));
			}
			if( allContacts != null ) {
				context.put("contact", allContacts.get(0));
			}
			return new ModelAndView(searchRegulatorTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchRegulatorTemplate);
	}


	public ModelAndView editRegulatorSummary( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			// do nothing
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorSummaryTemplate);
	}


	public ModelAndView editRegulatorEditContact( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
//			RegulatorForm form = (RegulatorForm)command;
//			Regulator regulator = accreditationService.getRegulatorById(form.getRegulator().getId());
//			form.setRegulator(regulator);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorEditContactTemplate);
	}

	public ModelAndView downloadDocument( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			RegulatorForm form = (RegulatorForm)command;

			String documentPath = VU360Properties.getVU360Property("document.saveLocation");
			String filepath = documentPath+"/Regulator/"+form.getRegulator().getId().toString()+"/"+
			form.getDocument().getId().toString();
			String path = filepath+"_"+form.getDocument().getFileName();
			String mimetype = this.getServletContext().getMimeType(path);
			File f = new File(path);

			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(f));

			response.setContentType(mimetype);
			response.setContentLength((int)f.length());
			response.setHeader("Content-Disposition", "attachment; filename="+form.getDocument().getFileName());
			FileCopyUtils.copy(in ,response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorEditDocumentTemplate);
	}

	public ModelAndView editRegulatorEditDocument( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
//			RegulatorForm form = (RegulatorForm)command;
//			Regulator regulator = accreditationService.getRegulatorById(form.getRegulator().getId());
//			form.setRegulator(regulator);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorEditDocumentTemplate);
	}

	public ModelAndView editRegulatorSaveContact( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			if( errors.hasErrors() ) {
				return new ModelAndView(editRegulatorEditContactTemplate);
			}
			RegulatorForm form = (RegulatorForm)command;
			Contact contact = form.getContact();
			contact.setFirstName(contact.getFirstName().trim());
			contact.setLastName(contact.getLastName().trim());
			contact.setPhone(contact.getPhone().trim());
			if(contact.getTitle()==null)
				contact.setTitle(StringUtils.EMPTY);
			contact.getAddress().setStreetAddress(contact.getAddress().getStreetAddress().trim());
			contact.getAddress().setStreetAddress2(contact.getAddress().getStreetAddress2().trim());
			contact.getAddress().setCity(contact.getAddress().getCity().trim());
			contact.getAddress().setZipcode(contact.getAddress().getZipcode().trim());
			accreditationService.saveContact(contact);
			
			if( form.getEventSource().equalsIgnoreCase("false") ) {
				return new ModelAndView(editRegulatorEditContactTemplate);
			} else {
				return new ModelAndView(redirectEditRegulatorEditContactTemplate);
			}
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(redirectEditRegulatorEditContactTemplate);
	}


	public ModelAndView editRegulatorSaveDocument( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			if( errors.hasErrors() ) {
				return new ModelAndView(editRegulatorEditDocumentTemplate);
			}
			RegulatorForm form = (RegulatorForm)command;
			Document document = form.getDocument();
			accreditationService.saveDocument(document);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(redirectEditRegulatorEditDocumentTemplate);
	}


	public ModelAndView saveRegulatorSummary( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			RegulatorForm form = (RegulatorForm)command;			 
			Regulator regulatorForm = form.getRegulator();
			Regulator regulatorDB = accreditationService.loadForUpdateRegulator(regulatorForm.getId());
			
			regulatorDB.setAlias(HtmlEncoder.escapeHtmlFull(regulatorForm.getAlias()).toString());
			regulatorDB.setName(HtmlEncoder.escapeHtmlFull(regulatorForm.getName()).toString());
			regulatorDB.setEmailAddress(HtmlEncoder.escapeHtmlFull(regulatorForm.getEmailAddress()).toString());
			regulatorDB.setFax(HtmlEncoder.escapeHtmlFull(regulatorForm.getFax()).toString());
			regulatorDB.setJurisdiction(HtmlEncoder.escapeHtmlFull(regulatorForm.getJurisdiction()).toString());
			regulatorDB.setPhone(HtmlEncoder.escapeHtmlFull(regulatorForm.getPhone()).toString());
			regulatorDB.setWebsite(HtmlEncoder.escapeHtmlFull(regulatorForm.getWebsite()).toString());
			if(form.getRegulator().getJurisdiction().equalsIgnoreCase("other")){
				regulatorDB.setJurisdiction(form.getRegulator().getOtherJurisdiction());
			}
			
			Address address = regulatorForm.getAddress();
			address.setStreetAddress(HtmlEncoder.escapeHtmlFull(address.getStreetAddress()).toString());
			address.setStreetAddress2(HtmlEncoder.escapeHtmlFull(address.getStreetAddress2()).toString());
			address.setCity(HtmlEncoder.escapeHtmlFull(address.getCity()).toString());
			address.setZipcode(HtmlEncoder.escapeHtmlFull(address.getZipcode()).toString());
			regulatorDB.setAddress(address);
			
			Address address2 = regulatorForm.getAddress2();
			address2.setStreetAddress(HtmlEncoder.escapeHtmlFull(address2.getStreetAddress()).toString());
			address2.setStreetAddress2(HtmlEncoder.escapeHtmlFull(address2.getStreetAddress2()).toString());
			address2.setCity(HtmlEncoder.escapeHtmlFull(address2.getCity()).toString());
			address2.setZipcode(HtmlEncoder.escapeHtmlFull(address2.getZipcode()).toString());
			regulatorDB.setAddress2(address2);
			
			if( errors.hasErrors() ) {

				CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
				Map<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomField>  customFieldMap = new HashMap<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
				for(com.softech.vu360.lms.web.controller.model.customfield.CustomField tempcustomField:form.getCustomFields()){

					CustomField customField = tempcustomField.getCustomFieldRef();
					List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
					customFieldValues.add(tempcustomField.getCustomFieldValueRef());

					if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField){
						List<CustomFieldValueChoice> customFieldValueChoices=accreditationService.getOptionsByCustomField(customField);
						fieldBuilder.buildCustomField(customField,tempcustomField.getStatus(),customFieldValues,customFieldValueChoices);

						if (customField instanceof MultiSelectCustomField){
							customFieldMap.put(tempcustomField.getCustomFieldRef().getId(), tempcustomField);
						}

					}else {
						customFieldValues.get(0).setValue(HtmlEncoder.escapeHtmlFull(customFieldValues.get(0).getValue().toString()).toString());
						fieldBuilder.buildCustomField(customField,tempcustomField.getStatus(),customFieldValues);
					}
				}

				List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();
				for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : customFieldList){
					if (customField.getCustomFieldRef() instanceof MultiSelectCustomField ){

						com.softech.vu360.lms.web.controller.model.customfield.CustomField tempCustomField = customFieldMap.get(customField.getCustomFieldRef().getId());
						if (tempCustomField != null){

							Map<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice> tempCustomFieldValueChoiceMap = new HashMap<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice>();

							for(com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice tempCustomFieldValueChoice:tempCustomField.getCustomFieldValueChoices()){
								tempCustomFieldValueChoiceMap.put(tempCustomFieldValueChoice.getCustomFieldValueChoiceRef().getId(), tempCustomFieldValueChoice);
							}

							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice:customField.getCustomFieldValueChoices()){
								if (tempCustomFieldValueChoiceMap.containsKey(customFieldValueChoice.getCustomFieldValueChoiceRef().getId())){
									customFieldValueChoice.setSelected(tempCustomFieldValueChoiceMap.get(customFieldValueChoice.getCustomFieldValueChoiceRef().getId()).isSelected());
								}
							}

						}
					}
				}
				/*
				 * Now re set the multi select option
				 * */
				form.setRegulator(regulatorDB);
				form.setCustomFields(customFieldList);

				return new ModelAndView(editRegulatorSummaryTemplate);
			}

			if (form.getCustomFields().size()>0){
				List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
				for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField :form.getCustomFields()){

					if(customField.getCustomFieldRef() instanceof MultiSelectCustomField){

						MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField.getCustomFieldRef();
						if (multiSelectCustomField.getCheckBox()){

							List<CustomFieldValueChoice> customFieldValueChoices=new ArrayList<CustomFieldValueChoice>();
							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){

								if (customFieldValueChoice.isSelected()){
									CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoice.getCustomFieldValueChoiceRef();
									customFieldValueChoices.add(customFieldValueChoiceRef);
								}

							}
							CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
							customFieldValue.setCustomField(customField.getCustomFieldRef());
							customFieldValue.setValueItems(customFieldValueChoices);

							customFieldValues.add(customFieldValue);
						} else {
							List<CustomFieldValueChoice> customFieldValueChoices=new ArrayList<CustomFieldValueChoice>();
							if(customField.getSelectedChoices()!=null){
								Map<Long,CustomFieldValueChoice> customFieldValueChoiceMap = new HashMap<Long,CustomFieldValueChoice>();
								for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){
									customFieldValueChoiceMap.put(customFieldValueChoice.getCustomFieldValueChoiceRef().getId(), customFieldValueChoice.getCustomFieldValueChoiceRef());
								}

								for(String selectedChoiceIdString : customField.getSelectedChoices()){
									if(customFieldValueChoiceMap.containsKey(new Long(selectedChoiceIdString))){
										CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoiceMap.get(new Long(selectedChoiceIdString));
										customFieldValueChoices.add(customFieldValueChoiceRef);
									}
								}
							}
							CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
							customFieldValue.setCustomField(customField.getCustomFieldRef());
							customFieldValue.setValueItems(customFieldValueChoices);
							
							customFieldValues.add(customFieldValue);
						}

					} else {
						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						if(customFieldValue.getValue()!=null){
							customFieldValue.setValue(customFieldValue.getValue().toString());
						}
						customFieldValues.add(customFieldValue);
					}
				}
				if (customFieldValues.size()>0){
					regulatorDB.setCustomfieldValues(customFieldValues);
				}
			}

			accreditationService.saveRegulator(regulatorDB);

			if( form.getEventSource().equalsIgnoreCase("false")) {
				return new ModelAndView(editRegulatorSummaryTemplate);
			} else {
				return new ModelAndView(searchRegulatorTemplate);
			}
			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchRegulatorTemplate);
	}


	public ModelAndView editRegulatorCredential( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			request.setAttribute("newPage","true");
			RegulatorForm form = (RegulatorForm)command;
			form.setRegCredential(null);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorCredentialTemplate);
	}
	
	public ModelAndView listRegulatorCategories( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			request.setAttribute("newPage","true");
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(listRegulatorCategoriesTemplate);
	}
	public ModelAndView searchRegulatorCategories( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();

			RegulatorForm form = (RegulatorForm)command;
			List<RegulatorCategory> categories = accreditationService.findRegulatorCategories("", "", form.getRID());
			String categoryName = (request.getParameter("categoryName") == null) ? "" : request.getParameter("categoryName");
			String categoryType = (request.getParameter("categoryType") == null) ? "" : request.getParameter("categoryType");

			categoryName = HtmlEncoder.escapeHtmlFull(categoryName).toString();
			context.put("categoryName", categoryName);
			categoryType = HtmlEncoder.escapeHtmlFull(categoryType).toString();
			context.put("categoryType", categoryType);

			String pageIndex = request.getParameter("pageIndex");
			if( pageIndex == null ) pageIndex = "0";

			PagerAttributeMap.put("pageIndex",pageIndex);
			if( categories.size() <= 10 ) {
				PagerAttributeMap.put("pageIndex", "0");
			}
			ArrayList<RegulatorCategory> regCategories = new ArrayList <RegulatorCategory>();
			ArrayList<RegulatorCategory> searchedCategories=null;
			searchedCategories = accreditationService.findRegulatorCategories(categoryName,categoryType,form.getRID());
			if(searchedCategories ==null || searchedCategories.size()==0) {
				context.put("sortDirection", 0);
				context.put("sortColumnIndex", 0);
				form.setCategories(new ArrayList<RegulatorCategory>());
				return new ModelAndView(listRegulatorCategoriesTemplate, "context", context);
			}
			
			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			String sortDirection = request.getParameter("sortDirection");
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			context.put("showAll", showAll);

			if( sortColumnIndex != null && sortDirection != null ) {

				request.setAttribute("PagerAttributeMap", PagerAttributeMap);

				// sorting against officialLicenseName name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						RegulatorCategorySort categorySort = new RegulatorCategorySort();
						categorySort.setSortBy("categoryName");
						categorySort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCategories,categorySort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
					} else {
						RegulatorCategorySort categorySort = new RegulatorCategorySort();
						categorySort.setSortBy("officialLicenseName");
						categorySort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCategories,categorySort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
					}
					// sorting against jurisdiction
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						RegulatorCategorySort categorySort = new RegulatorCategorySort();
						categorySort.setSortBy("categoryType");
						categorySort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCategories,categorySort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 1);
					} else {
						RegulatorCategorySort credentialSort = new RegulatorCategorySort();
						credentialSort.setSortBy("categoryType");
						credentialSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCategories,credentialSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 1);
					}
				}
			}

			for( RegulatorCategory category : searchedCategories ) {
				category.setSelected(true);
				regCategories.add(category);
			}
			form.setCategories(regCategories);
			return new ModelAndView(listRegulatorCategoriesTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(listRegulatorCategoriesTemplate);
	}
	
	@SuppressWarnings("static-access")
	public ModelAndView editRegulatorCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
 
		RegulatorForm form = (RegulatorForm)command;
		form.setManageCustomField(null);
		List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>(); 
		if (form.getRegulator().getCustomfields() != null && form.getRegulator().getCustomfields().size() > 0) {
			for (CustomField customField : form.getRegulator().getCustomfields()) {
				ManageCustomField manageCustomField = new ManageCustomField();
				manageCustomField.setFieldName(customField.getFieldLabel());
				manageCustomField.setId(customField.getId());
				if (customField instanceof SingleLineTextCustomFiled) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
				} else if (customField instanceof DateTimeCustomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_DATE);
				} else if (customField instanceof MultipleLineTextCustomfield) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
				} else if (customField instanceof NumericCusomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_NUMBER);
				} else if (customField instanceof SSNCustomFiled) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
				} else if (customField instanceof SingleSelectCustomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_RADIO_BUTTON);
				} else if (customField instanceof MultiSelectCustomField) {
					if (((MultiSelectCustomField) customField).getCheckBox())
						manageCustomField.setFieldType(form.CUSTOMFIELD_CHECK_BOX);
					else 
						manageCustomField.setFieldType(form.CUSTOMFIELD_CHOOSE);
				} 
				manageCustomFieldList.add(manageCustomField);
			}
			form.setManageCustomField(manageCustomFieldList);
		}
		return new ModelAndView(editRegulatorCustomFieldTemplate);
	}

	public ModelAndView deleteRegulatorCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		RegulatorForm form = (RegulatorForm)command;
		String[] selectedCustomFieldId = request.getParameterValues("customField");
		Regulator reg = form.getRegulator();
		List<CustomField> customFields = reg.getCustomfields();
		for ( int i = 0 ; i < selectedCustomFieldId.length ; i++ ) {
			for ( int j = 0 ; j < customFields.size() ; j++ ) {
				if ( customFields.get(j).getId().compareTo(Long.parseLong(selectedCustomFieldId[i])) == 0 ) {
					customFields.remove(j);
					break;
				}
			}
		}
		reg.setCustomfields(customFields);
		accreditationService.saveRegulator(reg);

		context.put("target", "editRegulatorCustomField");
		return new ModelAndView(editRegulatorRedirectTemplate, "context", context);
	}

	public ModelAndView searchRegulatorCredential( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();

			RegulatorForm form = (RegulatorForm)command;
			List<Credential> credentials = form.getRegulator().getCredentials();
			String credentialName = (request.getParameter("credentialName") == null) ? "" : request.getParameter("credentialName");
			String credentialShortName = (request.getParameter("credentialShortName") == null) ? "" : request.getParameter("credentialShortName");

			credentialName = HtmlEncoder.escapeHtmlFull(credentialName).toString();
			context.put("credentialName", credentialName);
			credentialShortName = HtmlEncoder.escapeHtmlFull(credentialShortName).toString();
			context.put("credentialShortName", credentialShortName);

			String pageIndex = request.getParameter("pageIndex");
			if( pageIndex == null ) pageIndex = "0";

			PagerAttributeMap.put("pageIndex",pageIndex);
			if( credentials.size() <= 10 ) {
				PagerAttributeMap.put("pageIndex", "0");
			}
			List<RegulatorCredential> regCredentials = new ArrayList <RegulatorCredential>();
			List<Credential> searchedCredentials=null;
			if(credentials !=null && credentials.size()>0) {
				Long credentialIdArray[] = new Long[credentials.size()];
				int count=0;
				for(Credential credential:credentials ) {
					credentialIdArray[count]=credential.getId();
					count=count+1;
				}
				searchedCredentials = accreditationService.findCredentialInRegulator(credentialName, 
						credentialShortName, credentialIdArray);
			}else {
				context.put("sortDirection", 0);
				context.put("sortColumnIndex", 0);
				form.setRegCredential(null);
				return new ModelAndView(editRegulatorCredentialTemplate, "context", context);
			}
			
			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			String sortDirection = request.getParameter("sortDirection");
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			context.put("showAll", showAll);

			if( sortColumnIndex != null && sortDirection != null ) {

				request.setAttribute("PagerAttributeMap", PagerAttributeMap);

				// sorting against officialLicenseName name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CredentialSort credentialSort = new CredentialSort();
						credentialSort.setSortBy("officialLicenseName");
						credentialSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCredentials,credentialSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
					} else {
						CredentialSort credentialSort = new CredentialSort();
						credentialSort.setSortBy("officialLicenseName");
						credentialSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCredentials,credentialSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
					}
					// sorting against jurisdiction
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CredentialSort credentialSort = new CredentialSort();
						credentialSort.setSortBy("shortLicenseName");
						credentialSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCredentials,credentialSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 1);
					} else {
						CredentialSort credentialSort = new CredentialSort();
						credentialSort.setSortBy("shortLicenseName");
						credentialSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCredentials,credentialSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 1);
					}
				}
			}

			for( Credential cred : searchedCredentials ) {
				RegulatorCredential regCredential = new RegulatorCredential();
				regCredential.setCredential(cred);
				regCredential.setSelected(false);
				regCredentials.add(regCredential);
			}
			form.setRegCredential(regCredentials);
			return new ModelAndView(editRegulatorCredentialTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorCredentialTemplate);
	}


	public ModelAndView deleteRegulatorCredential( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			RegulatorForm form = (RegulatorForm)command;
			Regulator regulator = form.getRegulator();
			List<Credential> remainingCredentials = new ArrayList <Credential>();
			List<RegulatorCredential> selectedCredentials = form.getRegCredential();
			for( int i = 0 ; i < selectedCredentials.size() ; ) {
				if( selectedCredentials.get(i).isSelected() ) {
					selectedCredentials.remove(i);
				} else {
					i ++ ;
				}
			}
			for( RegulatorCredential cred : selectedCredentials ) {
				remainingCredentials.add(cred.getCredential());
			}
			regulator.setCredentials(remainingCredentials);
			accreditationService.saveRegulator(regulator);
			return new ModelAndView(editRegulatorCredentialTemplate);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorCredentialTemplate);
	}


	public ModelAndView editRegulatorContact( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			request.setAttribute("newPage","true");
			RegulatorForm form = (RegulatorForm)command;
			form.setRegContact(null);


		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorContactTemplate);
	}


	public ModelAndView searchRegulatorContact( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			RegulatorForm form = (RegulatorForm)command;
			String firstName = (request.getParameter("firstName") == null) ? "" : request.getParameter("firstName");
			String lastName = (request.getParameter("lastName") == null) ? "" : request.getParameter("lastName");
			String emailAdd = (request.getParameter("emailAdd") == null) ? "" : request.getParameter("emailAdd");
			String phone = (request.getParameter("phone") == null) ? "" : request.getParameter("phone");

			firstName = HtmlEncoder.escapeHtmlFull(firstName).toString();
			context.put("firstName", firstName);
			lastName = HtmlEncoder.escapeHtmlFull(lastName).toString();
			context.put("lastName", lastName);
			emailAdd = HtmlEncoder.escapeHtmlFull(emailAdd).toString();
			context.put("emailAdd", emailAdd);
			phone = HtmlEncoder.escapeHtmlFull(phone).toString();
			context.put("phone", phone);

			String pageIndex = request.getParameter("pageIndex");
			if( pageIndex == null ) pageIndex = "0";

			PagerAttributeMap.put("pageIndex",pageIndex);
			List<Contact> searchedContacts =null;
			if(form.getRegulator()!=null) {
				searchedContacts = accreditationService.findContactByRegulator( firstName, 
						lastName, emailAdd, phone, form.getRegulator().getId());
			}else {
				return new ModelAndView(searchRegulatorTemplate);
			}

			if( searchedContacts.size() <= 10 ) {
				PagerAttributeMap.put("pageIndex", "0");
			}

			List<RegulatorContact> regContacts = new ArrayList <RegulatorContact>();

			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			String sortDirection = request.getParameter("sortDirection");
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			context.put("showAll", showAll);

			if( sortColumnIndex != null && sortDirection != null ) {

				request.setAttribute("PagerAttributeMap", PagerAttributeMap);

				// sorting against regulator name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ContactSort ContactSort = new ContactSort();
						ContactSort.setSortBy("firstName");
						ContactSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedContacts,ContactSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
					} else {
						ContactSort ContactSort = new ContactSort();
						ContactSort.setSortBy("firstName");
						ContactSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedContacts,ContactSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
					}
					// sorting against alias
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ContactSort ContactSort = new ContactSort();
						ContactSort.setSortBy("lastName");
						ContactSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedContacts,ContactSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 1);
					} else {
						ContactSort ContactSort=new ContactSort();
						ContactSort.setSortBy("lastName");
						ContactSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedContacts,ContactSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 1);
					}
					// sorting against email - address
				} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ContactSort ContactSort=new ContactSort();
						ContactSort.setSortBy("emailAddress");
						ContactSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedContacts,ContactSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 2);
					} else {
						ContactSort ContactSort=new ContactSort();
						ContactSort.setSortBy("emailAddress");
						ContactSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedContacts,ContactSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 2);
					}
					// sorting against jurisdiction...
				} else if( sortColumnIndex.equalsIgnoreCase("3") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ContactSort ContactSort=new ContactSort();
						ContactSort.setSortBy("phone");
						ContactSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedContacts,ContactSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 3);
					} else {
						ContactSort ContactSort=new ContactSort();
						ContactSort.setSortBy("phone");
						ContactSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedContacts,ContactSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 3);
					}
				}
			}
			for( Contact cont : searchedContacts ) {
				RegulatorContact regContact = new RegulatorContact();
				regContact.setContact(cont);
				regContact.setSelected(false);
				regContacts.add(regContact);
			}

			form.setRegContact(regContacts);

			return new ModelAndView(editRegulatorContactTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorContactTemplate);
	}


	public ModelAndView deleteRegulatorContact( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			RegulatorForm form = (RegulatorForm)command;
			Regulator regulator = form.getRegulator();
			List<RegulatorContact> selectedContacts = form.getRegContact();
			List<Contact> deletedContacts = new ArrayList <Contact>();

			for( int i = 0 ; i < selectedContacts.size() ; ) {
				if( selectedContacts.get(i).isSelected() ) {
					deletedContacts.add(selectedContacts.get(i).getContact());
					selectedContacts.remove(i);
				} else {
					i ++ ;
				}
			}
			accreditationService.deleteContacts(deletedContacts);
			for( RegulatorContact cred : selectedContacts ) {
				cred.getContact().setRegulator(regulator);
				accreditationService.saveContact(cred.getContact());
			}
			accreditationService.saveRegulator(regulator);
			return new ModelAndView(editRegulatorContactTemplate);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorContactTemplate);
	}
	
	public ModelAndView deleteRegulatorCategories( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			RegulatorForm form = (RegulatorForm)command;
			List<RegulatorCategory> selectedRegCategories = form.getCategories();
			List<RegulatorCategory> regCategoriesToBeDel = new ArrayList <RegulatorCategory>();
			
			for( RegulatorCategory selectedCategory: selectedRegCategories) {
				if( selectedCategory.getSelected() ) {										
					accreditationService.unAssignCategoryFromAllCourseApprovals(selectedCategory);					
					regCategoriesToBeDel.add(selectedCategory);
					
					/*RegulatorCategory deletableCat =accreditationService.loadForUpdateRegulatorCategory(selectedCategory.getId());
					deletableCat.setReportingFields(new ArrayList<CreditReportingField>());//remove from credit reporting fields					
					deletableCat.getCustomFields().clear();
					deletableCat.setCustomFields(deletableCat.getCustomFields());//remove from custom fields
					accreditationService.saveRegulatorCategory(deletableCat);*/										
				} 
			}
			
			try {
				accreditationService.deleteRegulatorCategories(regCategoriesToBeDel);
			} catch (NullPointerException e) {
				log.error(e.getMessage(),e);
			}
			
			selectedRegCategories.removeAll(regCategoriesToBeDel);			
			form.setCategories(selectedRegCategories);
			return new ModelAndView(listRegulatorCategoriesTemplate);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(listRegulatorCategoriesTemplate);
	}

	public ModelAndView editRegulatorDocument( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			RegulatorForm form = (RegulatorForm)command;
			form.setRegDocument(null);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorDocumentTemplate);
	}


	public ModelAndView searchRegulatorDocument( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			RegulatorForm form = (RegulatorForm)command;
			Regulator regulator = form.getRegulator();
			List<Document> docs = regulator.getDocuments();
			String docName = (request.getParameter("docName") == null) ? "" : request.getParameter("docName");

			docName = HtmlEncoder.escapeHtmlFull(docName).toString();
			context.put("docName", docName);
			List<Document> searchedDocs = accreditationService.findDocumentInRegulator( docName, docs);
			List<DocumentForm> regDocs = new ArrayList <DocumentForm>();
			String pageIndex = request.getParameter("pageIndex");
			if( pageIndex == null ) pageIndex = "0";

			PagerAttributeMap.put("pageIndex",pageIndex);
			if( searchedDocs.size() <= 10 ) {
				PagerAttributeMap.put("pageIndex", "0");
			}
			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			String sortDirection = request.getParameter("sortDirection");
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			context.put("showAll", showAll);

			if( sortColumnIndex != null && sortDirection != null ) {

				request.setAttribute("PagerAttributeMap", PagerAttributeMap);

				// sorting against fileName
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						DocumentSort documentSort = new DocumentSort();
						documentSort.setSortBy("name");
						documentSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedDocs,documentSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
					} else {
						DocumentSort documentSort = new DocumentSort();
						documentSort.setSortBy("name");
						documentSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedDocs,documentSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
					}
					// sorting against description
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						DocumentSort documentSort = new DocumentSort();
						documentSort.setSortBy("description");
						documentSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedDocs,documentSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 1);
					} else {
						DocumentSort documentSort = new DocumentSort();
						documentSort.setSortBy("description");
						documentSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedDocs,documentSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 1);
					}
				}
			}
			for( Document doc : searchedDocs ) {
				DocumentForm regDoc = new DocumentForm();
				regDoc.setDocument(doc);
				regDoc.setSelected(false);
				regDocs.add(regDoc);
			}
			form.setRegDocument(regDocs);
			return new ModelAndView(editRegulatorDocumentTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorDocumentTemplate);
	}


	public ModelAndView deleteRegulatorDocument( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			RegulatorForm form = (RegulatorForm)command;
			Regulator regulator = form.getRegulator();
			List<Document> remainingDocs = new ArrayList <Document>();
			List<DocumentForm> selectedDocuments = form.getRegDocument();
			for( int i = 0 ; i < selectedDocuments.size() ; ) {
				if( selectedDocuments.get(i).isSelected() ) {
					selectedDocuments.remove(i);
				} else {
					i ++ ;
				}
			}
			for( DocumentForm doc : selectedDocuments ) {
				remainingDocs.add(doc.getDocument());
			}
			regulator.setDocuments(remainingDocs);
			accreditationService.saveRegulator(regulator);
			return new ModelAndView(editRegulatorDocumentTemplate);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editRegulatorDocumentTemplate);
	}


	protected void validate( HttpServletRequest request, Object command,
			BindException errors, String methodName ) throws Exception {

		RegulatorForm form = (RegulatorForm)command; 
		EditRegulatorValidator validator = (EditRegulatorValidator)this.getValidator();

		if( methodName.equals("saveRegulatorSummary")) {
			if( form.getEventSource().equalsIgnoreCase("true") ) {
				validator.validateSummaryPage(form, errors);
				if (form.getCustomFields().size()>0){
					validator.validateCustomFields(form.getCustomFields(), errors);
				}
			}
		} else if ( methodName.equals("editRegulatorSaveContact") ) {
			if( form.getEventSource().equalsIgnoreCase("true") ) {
				validator.validateContact(form, errors);
			}
		} else if( methodName.equals("editRegulatorSaveDocument") ) {
			validator.validateDocument(form, errors);
		}
	}


	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getSearchRegulatorTemplate() {
		return searchRegulatorTemplate;
	}

	public void setSearchRegulatorTemplate(String searchRegulatorTemplate) {
		this.searchRegulatorTemplate = searchRegulatorTemplate;
	}

	public String getEditRegulatorSummaryTemplate() {
		return editRegulatorSummaryTemplate;
	}

	public void setEditRegulatorSummaryTemplate(String editRegulatorSummaryTemplate) {
		this.editRegulatorSummaryTemplate = editRegulatorSummaryTemplate;
	}

	public String getEditRegulatorCredentialTemplate() {
		return editRegulatorCredentialTemplate;
	}

	public void setEditRegulatorCredentialTemplate(
			String editRegulatorCredentialTemplate) {
		this.editRegulatorCredentialTemplate = editRegulatorCredentialTemplate;
	}

	public String getEditRegulatorContactTemplate() {
		return editRegulatorContactTemplate;
	}

	public void setEditRegulatorContactTemplate(String editRegulatorContactTemplate) {
		this.editRegulatorContactTemplate = editRegulatorContactTemplate;
	}

	public String getEditRegulatorDocumentTemplate() {
		return editRegulatorDocumentTemplate;
	}

	public void setEditRegulatorDocumentTemplate(
			String editRegulatorDocumentTemplate) {
		this.editRegulatorDocumentTemplate = editRegulatorDocumentTemplate;
	}

	public String getEditRegulatorEditContactTemplate() {
		return editRegulatorEditContactTemplate;
	}

	public void setEditRegulatorEditContactTemplate(
			String editRegulatorEditContactTemplate) {
		this.editRegulatorEditContactTemplate = editRegulatorEditContactTemplate;
	}

	public String getEditRegulatorCustomFieldTemplate() {
		return editRegulatorCustomFieldTemplate;
	}

	public void setEditRegulatorCustomFieldTemplate(
			String editRegulatorCustomFieldTemplate) {
		this.editRegulatorCustomFieldTemplate = editRegulatorCustomFieldTemplate;
	}

	public String getRedirectEditRegulatorEditDocumentTemplate() {
		return redirectEditRegulatorEditDocumentTemplate;
	}

	public void setRedirectEditRegulatorEditDocumentTemplate(
			String redirectEditRegulatorEditDocumentTemplate) {
		this.redirectEditRegulatorEditDocumentTemplate = redirectEditRegulatorEditDocumentTemplate;
	}

	public String getEditRegulatorRedirectTemplate() {
		return editRegulatorRedirectTemplate;
	}

	public void setEditRegulatorRedirectTemplate(
			String editRegulatorRedirectTemplate) {
		this.editRegulatorRedirectTemplate = editRegulatorRedirectTemplate;
	}

	public String getEditRegulatorEditDocumentTemplate() {
		return editRegulatorEditDocumentTemplate;
	}

	public void setEditRegulatorEditDocumentTemplate(
			String editRegulatorEditDocumentTemplate) {
		this.editRegulatorEditDocumentTemplate = editRegulatorEditDocumentTemplate;
	}

	public String getRedirectEditRegulatorEditContactTemplate() {
		return redirectEditRegulatorEditContactTemplate;
	}

	public void setRedirectEditRegulatorEditContactTemplate(
			String redirectEditRegulatorEditContactTemplate) {
		this.redirectEditRegulatorEditContactTemplate = redirectEditRegulatorEditContactTemplate;
	}

	public String getListRegulatorCategoriesTemplate() {
		return listRegulatorCategoriesTemplate;
	}

	public void setListRegulatorCategoriesTemplate(
			String listRegulatorCategoriesTemplate) {
		this.listRegulatorCategoriesTemplate = listRegulatorCategoriesTemplate;
	}
	
}