package com.softech.vu360.lms.web.controller.accreditation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.Contact;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfigurationTemplate;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.RegulatoryAnalystService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.helper.InstructorSearchEnum;
import com.softech.vu360.lms.web.controller.model.accreditation.AddapprovalForm;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalCredential;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRegulatorCategory;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRequirement;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddApprovalValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ApprovalRegulatorCategorySort;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.CourseSort;
import com.softech.vu360.util.CredentialSort;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.DocumentSort;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.ProviderSort;
import com.softech.vu360.util.TemplateSort;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Dyutiman
 * created on 6th july-2009
 *
 */
public class AddApprovalWizardController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddApprovalWizardController.class.getName());
//	HttpSession session = null;

	private AccreditationService accreditationService;
	private CourseAndCourseGroupService courseCourseGroupService;
	@Inject
	private RegulatoryAnalystService regulatoryAnalystService;

	private String cancelTemplate = null;
	private String finishTemplate = null;

	public static final String CUSTOMFIELD_ENTITY_COURSEAPPROVAL = "CUSTOMFIELD_COURSEAPPROVAL";
	public static final String CUSTOMFIELD_ENTITY_PROVIDERAPPROVAL = "CUSTOMFIELD_PROVIDERAPPROVAL";
	public static final String CUSTOMFIELD_ENTITY_INSTRUCTORAPPROVAL = "CUSTOMFIELD_INSTRUCTORAPPROVAL";
	
	private static final int[] courseApprovalSteps = {0, 2, 4, 7, 20, 10, 12, 14, 15, 19, 8, 16};
	
	public AddApprovalWizardController() {
		super();
		setCommandName("approvalForm");
		setCommandClass(AddapprovalForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/approvals/addApproval/addApp_selMethod" /*Step 0*/
				/* NOT USED */ , "accreditation/approvals/addApproval/addApp_selRegulator" /*Step 1*/
				, "accreditation/approvals/addApproval/addApp_provider_selRegulator" /*Step 2*/
				/* NOT USED */ , "accreditation/approvals/addApproval/addApp_instructor_selRegulator" /*Step 3*/
				, "accreditation/approvals/addApproval/addApp_courseSummary" /*Step 4*/
				, "accreditation/approvals/addApproval/addApp_providerSummary" /*Step 5*/
				, "accreditation/approvals/addApproval/addApp_instructorSummary" /*Step 6*/
				, "accreditation/approvals/addApproval/addApp_selCourses" /*Step 7*/
				, "accreditation/approvals/addApproval/addApp_selProvider" /*Step 8*/
				, "accreditation/approvals/addApproval/addApp_selInstructor" /*Step 9*/
				, "accreditation/approvals/addApproval/addApp_selCredential" /*Step 10*/
				, "accreditation/approvals/addApproval/addApp_instructor_selCourse" /*Step 11*/
				, "accreditation/approvals/addApproval/addApp_selRequirement" /*Step 12*/
				, "accreditation/approvals/addApproval/addApp_instructor_selProvider" /*Step 13*/
				/* NOT USED */, "accreditation/approvals/addApproval/addApp_selTemplate" /*Step 14*/
				, "accreditation/approvals/addApproval/addApp_selAsset" /*Step 15 - (Actual Step 7 for Course Approval - Certificate)*/
				, "accreditation/approvals/addApproval/addApp_confirmation" /*Step 16*/
				, "accreditation/approvals/addApproval/addApp_providerConfirm" /*Step 17*/
				, "accreditation/approvals/addApproval/addApp_instructorConfirm" /*Step 18*/
				, "accreditation/approvals/addApproval/addApp_selAsset" /*Step 19 - (Actual Step 8 for Course Approval - Affidavit)*/
				, "accreditation/approvals/addApproval/addApp_selCourseGroup" /*Step 20 - (Actual Step 5 for Course Approval - Course Group)*/

		}); 
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		try {
			// do nothing
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}


	  protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		  AddapprovalForm form = (AddapprovalForm) command;
		  AddApprovalValidator validator = (AddApprovalValidator)this.getValidator();
		  
		  
		  // LMS-16004 - Now, Course group is not mandatory
		  /*if (currentPage == 20 && getTargetPage(request, currentPage) == 10 ) {
			  if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateCourseGroupPage(form, errors);
		  }*/
		  
		  
		  if (currentPage == 20 && getTargetPage(request, currentPage) == 10 && errors.hasErrors()) {
			  return 20;
		  }
		  
		
		  
		  
		  if(currentPage == 10 && getTargetPage(request, currentPage) == 12){
			  
			  if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateTenthPage(form, errors);
			  
			  
		  }

		  if (currentPage == 10 && getTargetPage(request, currentPage) == 20) {
			  return 20;
		  }
		  
		  return super.getTargetPage(request, command, errors, currentPage);
	  }
		
	  
	  
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {

//		Map <Object, Object> model = new HashMap <Object, Object>();
		
		Map <Object, Object> model = super.referenceData(request, page);
		
		if(model == null){
			model = new HashMap <Object, Object>();
		}
		
		AddapprovalForm form = (AddapprovalForm) command;
		
		if(form.getMethod().equals("courseApproval")){
			setCourseApprovalProperties(request, model, page);
		}
		
		switch(page){

		case 0:
			break;
		case 1:
			break;
		case 2:
			
			request.setAttribute("categoryTypes", RegulatorCategory.CATEGORY_TYPES);
			
			if( form.getMethod().equalsIgnoreCase("courseApproval") ) {

				if (!errors.hasErrors() && form.getCustomFields().size()==0){
					List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_COURSEAPPROVAL), "", "");
					CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
					List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();

					for(CustomField customField : globalCustomFieldList){
						if (customField instanceof SingleSelectCustomField || 
								customField instanceof MultiSelectCustomField ){

							List<CustomFieldValueChoice> customFieldValueChoices=this.getAccreditationService().getOptionsByCustomField(customField);
							fieldBuilder.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);

						} else {
							fieldBuilder.buildCustomField(customField,0,customFieldValues);
						}
					}
					List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();
					form.setCustomFields(customFieldList);
				}

			} else if( form.getMethod().equalsIgnoreCase("providerApproval") ) {

				if (!errors.hasErrors() && form.getCustomFields().size()==0){
					List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_PROVIDERAPPROVAL), "", "");
					CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
					List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();

					for(CustomField customField : globalCustomFieldList){
						if (customField instanceof SingleSelectCustomField || 
								customField instanceof MultiSelectCustomField ){

							List<CustomFieldValueChoice> customFieldValueChoices=this.getAccreditationService().getOptionsByCustomField(customField);
							fieldBuilder.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);

						} else {
							fieldBuilder.buildCustomField(customField,0,customFieldValues);
						}
					}
					List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();
					form.setCustomFields(customFieldList);
				}

			} else if( form.getMethod().equalsIgnoreCase("instructorApproval") ) {

				if (!errors.hasErrors() && form.getCustomFields().size()==0){
					List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_INSTRUCTORAPPROVAL), "", "");
					CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
					List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();

					for(CustomField customField : globalCustomFieldList){
						if (customField instanceof SingleSelectCustomField || 
								customField instanceof MultiSelectCustomField ){

							List<CustomFieldValueChoice> customFieldValueChoices=this.getAccreditationService().getOptionsByCustomField(customField);
							fieldBuilder.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);

						} else {
							fieldBuilder.buildCustomField(customField,0,customFieldValues);
						}
					}
					List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();
					form.setCustomFields(customFieldList);
				}
			}
			break;
		case 3:
			break;
		case 4:
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField :form.getCustomFields()){

				if(customField.getCustomFieldRef() instanceof MultiSelectCustomField){

					MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField.getCustomFieldRef();
					if (!multiSelectCustomField.getCheckBox()){

						if(customField.getSelectedChoices()!=null){

							for(String selectedChoiceIdString : customField.getSelectedChoices()){
								for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){
									if(selectedChoiceIdString.equalsIgnoreCase(customFieldValueChoice.getCustomFieldValueChoiceRef().getKey())){
										customFieldValueChoice.setSelected(true);
									}
								}
							}
						}
					}
				}
			}
			break;
		case 5:
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField :form.getCustomFields()){

				if(customField.getCustomFieldRef() instanceof MultiSelectCustomField){

					MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField.getCustomFieldRef();
					if (!multiSelectCustomField.getCheckBox()){

						if(customField.getSelectedChoices()!=null){

							for(String selectedChoiceIdString : customField.getSelectedChoices()){
								for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){
									if(selectedChoiceIdString.equalsIgnoreCase(customFieldValueChoice.getCustomFieldValueChoiceRef().getKey())){
										customFieldValueChoice.setSelected(true);
									}
								}
							}
						}
					}
				}
			}
			break;
		case 6:
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField :form.getCustomFields()){

				if(customField.getCustomFieldRef() instanceof MultiSelectCustomField){

					MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField.getCustomFieldRef();
					if (!multiSelectCustomField.getCheckBox()){

						if(customField.getSelectedChoices()!=null){

							for(String selectedChoiceIdString : customField.getSelectedChoices()){
								for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){
									if(selectedChoiceIdString.equalsIgnoreCase(customFieldValueChoice.getCustomFieldValueChoiceRef().getKey())){
										customFieldValueChoice.setSelected(true);
									}
								}
							}
						}
					}
				}
			}
			break;
		case 7:
			break;
		case 8:
			break;
		case 9:
			break;
		case 10:
			break;
		case 11:
			break;
		case 12:
			break;
		case 13:
			break;
		case 14:
			break;
		case 15:
			break;
		case 16:
			if( !StringUtils.isBlank(form.getSelectedCourseId()) ) {
				Course cou = courseCourseGroupService.getCourseById(Long.parseLong(form.getSelectedCourseId()));
				model.put("courseName", cou.getCourseTitle());
			}
			if( !StringUtils.isBlank(form.getSelectedCertificateId()) ) {
				Certificate cer = accreditationService.getCertificateById(Long.parseLong(form.getSelectedCertificateId()));
				model.put("certName", cer.getName());
			}
			if( !StringUtils.isBlank(form.getSelectedAffidavitId()) ) {
				Affidavit affidavit = accreditationService.getAffidavitById(Long.parseLong(form.getSelectedAffidavitId()));
				model.put("affidavitName", affidavit.getName());
			}
			if( !StringUtils.isBlank(form.getSelectedTemplateId())) {
				CourseConfigurationTemplate temp = accreditationService.getTemplateById(Long.parseLong(form.getSelectedTemplateId()));
				model.put("tempName", temp.getName());
			}else{
				model.put("tempName", "N/A");
			}
			if( !StringUtils.isBlank(form.getSelectedProviderId()) ) {
				Provider pro = accreditationService.getProviderById(Long.parseLong(form.getSelectedProviderId()));
				model.put("providerName", pro.getName());
			}
			if( !StringUtils.isBlank(form.getCourseGroupId()) && Long.parseLong(form.getCourseGroupId())>0 ) {
				CourseGroup objCourseGroup = courseCourseGroupService.getCourseGroupById(Long.parseLong(form.getCourseGroupId()));
				model.put("courseGroupName", objCourseGroup.getName());
			}
			return model;
		case 17:
			if( !StringUtils.isBlank(form.getSelectedProviderId()) ) {
				Provider pro = accreditationService.getProviderById(Long.parseLong(form.getSelectedProviderId()));
				model.put("providerName", pro.getName());
			}
			return model;
		case 18:
			if( !StringUtils.isBlank(form.getSelectedProviderId()) ) {
				Provider pro = accreditationService.getProviderById(Long.parseLong(form.getSelectedProviderId()));
				model.put("providerName", pro.getName());
			}
			if( !StringUtils.isBlank(form.getSelectedInstructorId()) ) {
				Instructor ins = accreditationService.getInstructorByID(Long.parseLong(form.getSelectedInstructorId()));
				model.put("instructorName", ins.getUser().getName());
			}
			if( !StringUtils.isBlank(form.getSelectedCourseId()) ) {
				Course cou = courseCourseGroupService.getCourseById(Long.parseLong(form.getSelectedCourseId()));
				model.put("courseName", cou.getCourseTitle());
			}
			return model;
		case 19:
			break;
		case 20:
			// new -------------------------------------------------------------
			log.debug("Case # 20");
			
			
			if(form.getPreviouslySelectedCourseId() != null && !form.getPreviouslySelectedCourseId().equals(form.getSelectedCourseId())){
				form.setPreviouslySelectedCourseId(form.getSelectedCourseId());
				form.setCourseGroupId("0");
				form.setCourseGroupTreeList(new ArrayList<List<TreeNode>>() );
			}
				
			if(form.getAction().equalsIgnoreCase("search")){
				log.debug("Search Called # 20");
				
				
				Long selectCourseId = Long.parseLong(form.getSelectedCourseId());
				String varCourseGroupName = request.getParameter("txtCommissionProduct");
				
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
				
				
				if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
					
					
					//VU360User loggedInUser = (VU360User)auth.getPrincipal();
					// getting content owner
					/*ContentOwner contentOwner = null;
					if( loggedInUser.getRegulatoryAnalyst() != null ) {
						contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
					}*/
					
					
					List<CourseGroup> courseGroupList = courseCourseGroupService.getAllCourseGroupsByCourseId(selectCourseId, varCourseGroupName);
	                List<List<TreeNode>> courseGroupTreeList = courseCourseGroupService.getCourseGroupTreeList2( courseGroupList, false );
	              
	               // model.put("courseGroupTreeList", courseGroupTreeList);
	                form.setCourseGroupTreeList(courseGroupTreeList);
	                model.put("contractType", "Course");
	                model.put("callMacroForChildren", "true");

				}else{
					throw new IllegalArgumentException("Distributor not found");
				}				
			}
			break;
			
		default:
			break;
		}
//		return super.referenceData(request, page);
		return model;
	}

	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		AddapprovalForm form = (AddapprovalForm)command;
		
		// parameter type of findContentOwnerByRegulatoryAnalyst() is changed so creating second user object of type VO.
		// first user object is also necessary for further operation
		com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		ContentOwner contentOwner = null;
		if( loggedInUserVO.getRegulatoryAnalyst() != null )
			contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUserVO.getRegulatoryAnalyst());

		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
//		session = request.getSession();
		if(((page == 4)||(page == 7)||(page == 12)||(page == 14)||(page == 15)||(page == 19))&& (!form.getAction().equalsIgnoreCase("search"))){
			request.setAttribute("newPage","true");
		}

		if( page == 0 ) {
			request.setAttribute("newPage","true");
			if( form.getMethod().equalsIgnoreCase("courseApproval") ) {
				if(form.getCourseApproval() == null){
					CourseApproval cApproval = new CourseApproval();
					form.setCourseApproval(cApproval);
				}
			} else if( form.getMethod().equalsIgnoreCase("providerApproval") ) {
				ProviderApproval pApproval = new ProviderApproval();
				form.setProviderApproval(pApproval);
			} else if( form.getMethod().equalsIgnoreCase("instructorApproval") ) {
				InstructorApproval iApproval = new InstructorApproval();
				form.setInstructorApproval(iApproval);
			}	
		}
		//LMS-13987
		if((page == 2 && form.getAction().equalsIgnoreCase("searchSelected"))){
			if(StringUtils.isNumeric(form.getSelectedRegulatorCategoryId())){
				for( ApprovalRegulatorCategory regCat : form.getRegulatorCategories()) {
					if( regCat.getCategory().getId() == Integer.parseInt(form.getSelectedRegulatorCategoryId())) {					
						regCat.setSelected(true);
					}
				}
			}
		} 
		if( ( page == 2 || page == 1 || page == 3 )&& form.getAction().equalsIgnoreCase("search") ) {

			
			String categoryName = (form.getRegulatorCategoryName() == null) ? "" : form.getRegulatorCategoryName();
			String categoryType = (form.getRegulatorCategoryType() == null) ? "" : form.getRegulatorCategoryType();
			
			List<RegulatorCategory> categories = accreditationService.findRegulatorCategories(categoryName, categoryType,-1);									
			categoryName = HtmlEncoder.escapeHtmlFull(categoryName).toString();			

			List<ApprovalRegulatorCategory> appRegsCategory = new ArrayList<ApprovalRegulatorCategory>();

			for( RegulatorCategory regCat : categories ) {
				List<Contact> contacts = accreditationService.getContactsByRegulator(regCat.getId()); 
				ApprovalRegulatorCategory appRegCat = new ApprovalRegulatorCategory();
				appRegCat.setCategory(regCat);
				appRegCat.setRegulator(regCat.getRegulator());
				appRegCat.setSelected(false);
				/*if( !contacts.isEmpty()) {
					appRegCat.setContactPersonName(contacts.get(0).getFirstName());
				}*/
				appRegsCategory.add(appRegCat);
			}
			String sortColumnIndex = form.getSortColumnIndex();
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = form.getSortDirection();
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = form.getPageCurrIndex();
			if( pageIndex == null ) pageIndex = form.getPageIndex();

			if( sortColumnIndex != null && sortDirection != null ) {

				// sorting against regulator name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ApprovalRegulatorCategorySort sort = new ApprovalRegulatorCategorySort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(appRegsCategory,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						ApprovalRegulatorCategorySort sort = new ApprovalRegulatorCategorySort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(appRegsCategory,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
					// sorting against alias
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ApprovalRegulatorCategorySort sort = new ApprovalRegulatorCategorySort();
						sort.setSortBy("alias");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(appRegsCategory,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						ApprovalRegulatorCategorySort sort = new ApprovalRegulatorCategorySort();
						sort.setSortBy("alias");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(appRegsCategory,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
					// sorting against contact person
				} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ApprovalRegulatorCategorySort sort = new ApprovalRegulatorCategorySort();
						sort.setSortBy("contact");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(appRegsCategory,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("2");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 2);
					} else {
						ApprovalRegulatorCategorySort sort = new ApprovalRegulatorCategorySort();
						sort.setSortBy("contact");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(appRegsCategory,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("2");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 2);
					}
					// sorting against state...
				} else if( sortColumnIndex.equalsIgnoreCase("3") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ApprovalRegulatorCategorySort sort = new ApprovalRegulatorCategorySort();
						sort.setSortBy("state");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(appRegsCategory,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("3");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 3);
					} else {
						ApprovalRegulatorCategorySort sort = new ApprovalRegulatorCategorySort();
						sort.setSortBy("state");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(appRegsCategory,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("3");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 3);
					}
				}
			}
			form.setRegulatorCategories(appRegsCategory);
			pagerAttributeMap.put("pageIndex", pageIndex);
			pagerAttributeMap.put("showAll", form.getShowAll());
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
		}
		if( ( page == 7 || page == 11 )&& form.getAction().equalsIgnoreCase("search") ) {

			String courseName = form.getCourseName();
			String courseId = form.getBusinessKey();

			List<Course> courses = accreditationService.findCoursesByRegulatoryAnalyst(courseName, courseId, loggedInUserVO.getRegulatoryAnalyst());

			courseName = HtmlEncoder.escapeHtmlFull(courseName).toString();
			courseId = HtmlEncoder.escapeHtmlFull(courseId).toString();

			String sortColumnIndex = form.getSortColumnIndex();
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = form.getSortDirection();
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = form.getPageCurrIndex();
			if( pageIndex == null ) pageIndex = form.getPageIndex();

			if( sortColumnIndex != null && sortDirection != null ) {

				// sorting against regulator name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CourseSort sort = new CourseSort();
						sort.setSortBy("courseTitle");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(courses,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						CourseSort sort = new CourseSort();
						sort.setSortBy("courseTitle");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(courses,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
					// sorting against alias
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CourseSort sort = new CourseSort();
						sort.setSortBy("courseId");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(courses,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						CourseSort sort = new CourseSort();
						sort.setSortBy("courseId");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(courses,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
					// sorting against contact person
				} 
			}	
			form.setCourses(courses);
			pagerAttributeMap.put("pageIndex", pageIndex);
			pagerAttributeMap.put("showAll", form.getShowAll());
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
		}
		if( ( page == 8 || page == 13 ) && form.getAction().equalsIgnoreCase("search") ) {

			String provName = (request.getParameter("provName") == null) ? "" : request.getParameter("provName");
			String provNumber = (request.getParameter("provNumber") == null) ? "" : request.getParameter("provNumber");


			List<Provider> providers = accreditationService.findProviders(provName, regulatoryAnalystService.getRegulatoryAnalystById(loggedInUserVO.getRegulatoryAnalyst().getId()));

			provName = HtmlEncoder.escapeHtmlFull(provName).toString();
			provNumber = HtmlEncoder.escapeHtmlFull(provNumber).toString();

			String sortColumnIndex = form.getSortColumnIndex();
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = form.getSortDirection();
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = form.getPageCurrIndex();
			if( pageIndex == null ) pageIndex = form.getPageIndex();

			if( sortColumnIndex != null && sortDirection != null ) {

				// sorting against provider name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ProviderSort sort = new ProviderSort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(providers,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						ProviderSort sort = new ProviderSort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(providers,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
					// sorting against alias
				}
			}	
			form.setProviders(providers);
			pagerAttributeMap.put("pageIndex", pageIndex);
			pagerAttributeMap.put("showAll", form.getShowAll());
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
		}
		if( page == 9 && form.getAction().equalsIgnoreCase("search") ) {

			String insName = (request.getParameter("insName") == null) ? "" : request.getParameter("insName");
			String insLastName = (request.getParameter("insLastName") == null) ? "" : request.getParameter("insLastName");
			String insEmail = (request.getParameter("insEmail") == null) ? "" : request.getParameter("insEmail");
			insName = HtmlEncoder.escapeHtmlFull(insName).toString();
			insLastName = HtmlEncoder.escapeHtmlFull(insLastName).toString();
			insEmail = HtmlEncoder.escapeHtmlFull(insEmail).toString();

			String pageIndex = form.getPageCurrIndex();
			if( pageIndex == null ) pageIndex = form.getPageIndex();

			String sortColumnIndex = form.getSortColumnIndex();
			String sortDirection = form.getSortDirection();
			String sortBy = null;
			if( sortColumnIndex != null && sortDirection != null ) {
				sortBy = InstructorSearchEnum.INSTRUCTOR.getSortBy(sortColumnIndex);
				form.setSortDirection(sortDirection);
				form.setSortColumnIndex(sortColumnIndex);
			}

			List<Instructor> instructors = accreditationService.findInstructor(insName, insLastName, insEmail, sortBy, sortDirection);

			form.setInstructors(instructors);
			pagerAttributeMap.put("pageIndex", pageIndex);
			pagerAttributeMap.put("showAll", form.getShowAll());
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
		}
		if( page == 10 && form.getAction().equalsIgnoreCase("search") ) {

			String credName = (request.getParameter("credName") == null) ? "" : request.getParameter("credName");
			String credType = (request.getParameter("credType") == null) ? "" : request.getParameter("credType");
			String credValidation = (request.getParameter("credValidation") == null) ? "" : request.getParameter("credValidation");

			List<Credential> creds = accreditationService.findCredential(credName, credType, loggedInUserVO.getRegulatoryAnalyst());

			credName = HtmlEncoder.escapeHtmlFull(credName).toString();
			credType = HtmlEncoder.escapeHtmlFull(credType).toString();
			credValidation = HtmlEncoder.escapeHtmlFull(credValidation).toString();

			String sortColumnIndex = form.getSortColumnIndex();
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = form.getSortDirection();
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = form.getPageCurrIndex();
			if( pageIndex == null ) pageIndex = form.getPageIndex();

			if( sortColumnIndex != null && sortDirection != null ) {

				// sorting against provider name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CredentialSort sort = new CredentialSort();
						sort.setSortBy("officialLicenseName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(creds,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						CredentialSort sort = new CredentialSort();
						sort.setSortBy("officialLicenseName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(creds,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CredentialSort sort = new CredentialSort();
						sort.setSortBy("shortLicenseName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(creds,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						CredentialSort sort = new CredentialSort();
						sort.setSortBy("shortLicenseName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(creds,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
				} 
			}
			pagerAttributeMap.put("pageIndex", pageIndex);
			pagerAttributeMap.put("showAll", form.getShowAll());
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);

			List<ApprovalCredential> appCreds = new ArrayList<ApprovalCredential>();
			List<ApprovalRequirement> requirements;

			for( Credential cred : creds ) {
				ApprovalCredential appCred = new ApprovalCredential();
				List <CredentialCategoryRequirement> rqurmnts = accreditationService.getCredentialCategoryRequirementsByCredential(cred.getId());
				if( rqurmnts != null ) {
					requirements = new ArrayList <ApprovalRequirement>();
					for( CredentialCategoryRequirement cr : rqurmnts ) {
						ApprovalRequirement ar = new ApprovalRequirement();
						ar.setRequirement(cr);
						ar.setSelected(false);
						requirements.add(ar);
					}
					appCred.setRequirements(requirements);
				}
				appCred.setCredential(cred);
				appCred.setSelected(false);
				appCreds.add(appCred);
			}
			form.setCredentials(appCreds);
		}
		if( page == 14 && form.getAction().equalsIgnoreCase("search") ) {

			String tempName = form.getTemplateName() == null ? "" : form.getTemplateName();
			String tempUpdated = (form.getTemplateLastUpdatedDate() == null) ? "" : form.getTemplateLastUpdatedDate();


			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date date = null;
			try {
				if( !StringUtils.isBlank(tempUpdated) )
					date = new Date();
				else 
					date = formatter.parse(tempUpdated);
			} catch (Exception e) {
				log.debug("Exception: "+ e);
			}
			List<CourseConfigurationTemplate> templates = accreditationService.findTemplates(tempName, date, contentOwner.getId());

			tempName = HtmlEncoder.escapeHtmlFull(tempName).toString();
			tempUpdated = HtmlEncoder.escapeHtmlFull(tempUpdated).toString();

			String sortColumnIndex = form.getSortColumnIndex();
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = form.getSortDirection();
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = form.getPageCurrIndex();
			if( pageIndex == null ) pageIndex = form.getPageIndex();

			if( sortColumnIndex != null && sortDirection != null ) {

				// sorting against provider name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						TemplateSort sort = new TemplateSort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(templates,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						TemplateSort sort = new TemplateSort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(templates,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						TemplateSort sort = new TemplateSort();
						sort.setSortBy("lastUpdatedDate");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(templates,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						TemplateSort sort = new TemplateSort();
						sort.setSortBy("lastUpdatedDate");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(templates,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
				} 
			}
			pagerAttributeMap.put("searchKeyTemplateName", tempName);
			pagerAttributeMap.put("searchKeyTemplateUpdated", tempUpdated);
			pagerAttributeMap.put("pageIndex", pageIndex);
			pagerAttributeMap.put("showAll", form.getShowAll());
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			form.setTemplates(templates);
		}
		if( (page == 15 || page == 19) && form.getAction().equalsIgnoreCase("search") ) {

			String assetName = (request.getParameter("assetName") == null) ? "" : request.getParameter("assetName");

			assetName = HtmlEncoder.escapeHtmlFull(assetName).toString();

			List assets = null;
			
			if(page == 15){
				assets = accreditationService.getCertficatesByName(assetName);
			}else if(page == 19){
				assets = accreditationService.getAffidavitsByName(assetName);
			}

			String sortColumnIndex = form.getSortColumnIndex();
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = form.getSortDirection();
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = form.getPageCurrIndex();
			if( pageIndex == null ) pageIndex = form.getPageIndex();

			if( sortColumnIndex != null && sortDirection != null ) {

				// sorting against provider name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						DocumentSort sort = new DocumentSort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(assets,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						DocumentSort sort = new DocumentSort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(assets,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
				}
			}
			if(page == 15){
				form.setCertificates(assets);
			}else if(page == 19){
				form.setAffidavits(assets);
			}
			pagerAttributeMap.put("searchKeyAssetName", assetName);
			pagerAttributeMap.put("pageIndex", pageIndex);
			pagerAttributeMap.put("showAll", form.getShowAll());
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
		}
		
		if(page == 20 && !form.getAction().equalsIgnoreCase("search")){
			String idStr = request.getParameter("rdoCourseGroups")==null?"0":request.getParameter("rdoCourseGroups");
			form.setCourseGroupId(idStr);
		}
		
		request.setAttribute("PagerAttributeMap", pagerAttributeMap);
	}	

	protected void validatePage(Object command, Errors errors, int page,
			boolean finish) {
		AddApprovalValidator validator = (AddApprovalValidator)this.getValidator();
		AddapprovalForm form = (AddapprovalForm)command;
		log.debug("Page num IN VALIDATOR --- "+page);
		// validation will not be done for process finish
		if( !finish ) {
			switch(page) {

			case 0:
				break;
			case 1:
				break;
			case 2:
				if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateSecondPage(form, errors);
//					validateCourseApprovalisAlreadyCreated(form, errors, "regulator");
				break;
			case 3:
				if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateSecondPage(form, errors);
				break;
			case 4:
				if(!form.getAction().equalsIgnoreCase("search")){
					validator.validateFourthPage(form, errors);
					if (form.getCustomFields().size()>0){
						validator.validateCustomFields(form.getCustomFields(), errors);
					}
					validateCourseApprovalisAlreadyCreated(form, errors, "summary");
				}
				break;
			case 5:
				validator.validateFifthPage(form, errors);
				if (form.getCustomFields().size()>0){
					validator.validateCustomFields(form.getCustomFields(), errors);
				}
				break;
			case 6:
				validator.validateSixthPage(form, errors);
				if (form.getCustomFields().size()>0){
					validator.validateCustomFields(form.getCustomFields(), errors);
				}
				break;
			case 7:
				if(!form.getAction().equalsIgnoreCase("search"))
				{
					//Check whether this course is associated to another course approval or not. If associated then throw error
					validateCourseApprovalisAlreadyCreated(form, errors, "course");					

//					if(accreditationService.isCourseAlreadyAssociatedWithCourseApproval(form.getSelectedCourseId()))
//					{
//						errors.rejectValue("selectedCourseId", "error.approval.course.alreadyAssociated","");
//					}
					validator.validateEleventhPage(form, errors);
				}
					
				break;
			case 8:
				if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateEightPage(form, errors);
				break;
			case 9:
				if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateNinthPage(form, errors);
				break;
			case 10:
				if(!form.getAction().equalsIgnoreCase("search"))
					//validator.validateTenthPage(form, errors);
				break;
			case 11:
				if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateEleventhPage(form, errors);
				break;
			case 12:
				if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateTwelvthPage(form, errors);
				break;
			case 13:
				if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateEightPage(form, errors);
				break;
			case 14:
				if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateFourteenthPage(form, errors);
				break;
			case 15:
				if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateSelectAssetPage(form, errors, Asset.ASSET_TYPE_CERTIFICATE);
				break;
			case 16:
				break;
			case 17:
				break;
			case 18:
				break;
			case 19:
				if(!form.getAction().equalsIgnoreCase("search"))
					validator.validateSelectAssetPage(form, errors, Asset.ASSET_TYPE_AFFIDAVIT);
				break;
			case 20:
				if(!form.getAction().equalsIgnoreCase("search"))
					//validator.validateCourseGroupPage(form, errors);
				break;	
			default:
				break;
			}
		}
		super.validatePage(command, errors, page, finish);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {

		log.debug("IN PROCESS FINISH --");
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
		getPrincipal();
		// getting content owner
		ContentOwner contentOwner = null;
		if( loggedInUser.getRegulatoryAnalyst() != null ) {
			contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(
					loggedInUser.getRegulatoryAnalyst());
		}
		AddapprovalForm form = (AddapprovalForm)command;

		if( form.getMethod().equalsIgnoreCase("courseApproval") ) {

			CourseApproval courseApproval = form.getCourseApproval();
			// setting dates...
			if( !StringUtils.isBlank(form.getEffectiveStartDate()) ) {
				courseApproval.setCourseApprovalEffectivelyStartDate(formatter.parse(form.getEffectiveStartDate()));
			}
			if( !StringUtils.isBlank(form.getEffectiveEndDate()) ) {
				courseApproval.setCourseApprovalEffectivelyEndsDate(this.setTimeToDayend(formatter.parse(form.getEffectiveEndDate())));
			}
			if( !StringUtils.isBlank(form.getRescentSubmittedDate()) ) {
				courseApproval.setMostRecentlySubmittedForApprovalDate(formatter.parse(form.getRescentSubmittedDate()));
			}
			if( !StringUtils.isBlank(form.getSubmissionReminderDate()) ) {
				courseApproval.setCourseSubmissionReminderDate(formatter.parse(form.getSubmissionReminderDate()));
			}
			if( !StringUtils.isBlank(form.getCertificateExpirationPeriod()) ) {
				courseApproval.setCertificateExpirationPeriod(Integer.parseInt(form.getCertificateExpirationPeriod()));
			}

			if(!StringUtils.isBlank(form.getSelectedRegulatorCategoryId())){
				RegulatorCategory regulatorCategory = getAccreditationService().getRegulatorCategory(Long.parseLong(form.getSelectedRegulatorCategoryId()));

				if (form.getMethod().equalsIgnoreCase("courseApproval")){ 
					form.getCourseApproval().setRegulatorCategory(regulatorCategory);
				}else if (form.getMethod().equalsIgnoreCase("providerApproval")){
					form.getProviderApproval().setRegulatorCategory(regulatorCategory);
				}else if (form.getMethod().equalsIgnoreCase("instructorApproval")){
					form.getInstructorApproval().setRegulatorCategory(regulatorCategory);
				}
				
			}
			
			// setting course...
			if( !StringUtils.isBlank(form.getSelectedCourseId()) ) {
				Course cou = courseCourseGroupService.getCourseById(Long.parseLong(form.getSelectedCourseId()));
				courseApproval.setCourse(cou);
			}
			
			// setting course group...
			if( !StringUtils.isBlank(form.getCourseGroupId()) ) {
				 List<CourseGroup> courseGroups =new ArrayList<CourseGroup>();
				CourseGroup objCG = courseCourseGroupService.getCourseGroupById(Long.valueOf(form.getCourseGroupId()));
				courseGroups.add(objCG);
				courseApproval.setCourseGroups(courseGroups);
			}
			
			// setting requirements...
			List<CredentialCategoryRequirement> requirements = new ArrayList <CredentialCategoryRequirement>();
			for( ApprovalCredential reg : form.getCredentials() ) {
				if( reg.isSelected() ) {
					for( ApprovalRequirement req : reg.getRequirements() ) {
						if( req.isSelected() ) {
							requirements.add(req.getRequirement());
						}
					}
				}
			}
			courseApproval.setRequirements(requirements);
			// setting template...
			if( !StringUtils.isBlank(form.getSelectedTemplateId()) ) {
				CourseConfigurationTemplate temp = accreditationService.getTemplateById(Long.parseLong(form.getSelectedTemplateId()));
				courseApproval.setTemplate(temp);
			}
			// setting certificate...
			if( !StringUtils.isBlank(form.getSelectedCertificateId()) ) {
				Certificate cer = accreditationService.getCertificateById(Long.parseLong(form.getSelectedCertificateId()));
				courseApproval.setCertificate(cer);
			}

			// setting affidavit...
			if( !StringUtils.isBlank(form.getSelectedAffidavitId()) ) {
				Affidavit affidavit = accreditationService.getAffidavitById(Long.parseLong(form.getSelectedAffidavitId()));
				courseApproval.setAffidavit(affidavit);
			}
			
			// setting provider...
			if( !StringUtils.isBlank(form.getSelectedProviderId()) ) {
				Provider pro = accreditationService.getProviderById(Long.parseLong(form.getSelectedProviderId()));
				if( contentOwner != null ) {
					pro.setContentOwner(contentOwner);
				}
				courseApproval.setProvider(pro);
			}
			
			//set certificate next number
			if(StringUtils.isNotBlank(form.getCertificateNumberGeneratorNextNumberString())){
				courseApproval.setCertificateNumberGeneratorNextNumber(Long.parseLong(form.getCertificateNumberGeneratorNextNumberString()));
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
							Object value = customFieldValue.getValue();
							customFieldValue.setCustomField(customField.getCustomFieldRef());
							customFieldValue.setValueItems(customFieldValueChoices);
							/*
							 *  for Encryption...
							 */
							if( customField.getCustomFieldRef().getFieldEncrypted() ) {
								customFieldValue.setValue(value);
							}
							customFieldValues.add(customFieldValue);

						}else {

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
							Object value = customFieldValue.getValue();
							customFieldValue.setCustomField(customField.getCustomFieldRef());
							customFieldValue.setValueItems(customFieldValueChoices);
							/*
							 *  for Encryption...
							 */
							if( customField.getCustomFieldRef().getFieldEncrypted() ) {
								customFieldValue.setValue(value);
							}
							customFieldValues.add(customFieldValue);
						}

					} else {
						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
						Object value = customFieldValue.getValue();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						/*
						 *  for Encryption...
						 */
						if( customField.getCustomFieldRef().getFieldEncrypted() ) {
							customFieldValue.setValue(value);
						}
						customFieldValues.add(customFieldValue);
					}
				}
				if (customFieldValues.size()>0){
					courseApproval.setCustomFieldValues(customFieldValues);
				}
			}

			// saving course approval -
			accreditationService.saveCourseApproval(courseApproval);

		} else if ( form.getMethod().equalsIgnoreCase("providerApproval") ) {

			ProviderApproval providerApproval = form.getProviderApproval();
			// setting dates...
			if( !StringUtils.isBlank(form.getEffectiveStartDate()) ) {
				providerApproval.setProviderApprovalEffectivelyStartDate(formatter.parse(form.getEffectiveStartDate()));
			}
			if( !StringUtils.isBlank(form.getEffectiveEndDate()) ) {
				providerApproval.setCourseApprovalEffectivelyEndDate(this.setTimeToDayend(formatter.parse(form.getEffectiveEndDate())));
			}
			if( !StringUtils.isBlank(form.getRescentSubmittedDate()) ) {
				providerApproval.setProviderMostRecentlySubmittedForApprovalDate(formatter.parse(form.getRescentSubmittedDate()));
			}
			if( !StringUtils.isBlank(form.getOriginalApprovadDate()) ) {
				providerApproval.setProviderOriginallyApprovedDate(formatter.parse(form.getOriginalApprovadDate()));
			}
			if( !StringUtils.isBlank(form.getSubmissionReminderDate()) ) {
				providerApproval.setProviderSubmissionReminderDate(formatter.parse(form.getSubmissionReminderDate()));
			}
			// setting provider...
			if( !StringUtils.isBlank(form.getSelectedProviderId()) ) {
				Provider pro = accreditationService.getProviderById(Long.parseLong(form.getSelectedProviderId()));
				if( contentOwner != null ) {
					pro.setContentOwner(contentOwner);
				}
				providerApproval.setProvider(pro);
			}

			// setting regulator category
			for( ApprovalRegulatorCategory regCat : form.getRegulatorCategories()) {
				if( regCat.isSelected() ) {					
					providerApproval.setRegulatorCategory(regCat.getCategory());
				}
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
							Object value = customFieldValue.getValue();
							customFieldValue.setCustomField(customField.getCustomFieldRef());
							customFieldValue.setValueItems(customFieldValueChoices);
							/*
							 *  for Encryption...
							 */
							if( customField.getCustomFieldRef().getFieldEncrypted() ) {
								customFieldValue.setValue(value);
							}
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
							Object value = customFieldValue.getValue();
							customFieldValue.setCustomField(customField.getCustomFieldRef());
							customFieldValue.setValueItems(customFieldValueChoices);
							/*
							 *  for Encryption...
							 */
							if( customField.getCustomFieldRef().getFieldEncrypted() ) {
								customFieldValue.setValue(value);
							}
							customFieldValues.add(customFieldValue);
						}

					} else {
						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
						Object value = customFieldValue.getValue();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						/*
						 *  for Encryption...
						 */
						if( customField.getCustomFieldRef().getFieldEncrypted() ) {
							customFieldValue.setValue(value);
						}
						customFieldValues.add(customFieldValue);
					}
				}
				if (customFieldValues.size()>0){
					providerApproval.setCustomFieldValues(customFieldValues);
				}
			}

			// now saving -
			accreditationService.saveProviderApproval(providerApproval);

		} else if( form.getMethod().equalsIgnoreCase("instructorApproval")) {
			InstructorApproval insApproval = form.getInstructorApproval();
			// setting dates...
			if( !StringUtils.isBlank(form.getEffectiveStartDate()) ) {
				insApproval.setInstructorApprovalEffectivelyStartDate(formatter.parse(form.getEffectiveStartDate()));
			}
			if( !StringUtils.isBlank(form.getEffectiveEndDate()) ) {
				insApproval.setInstructorApprovalEffectivelyEndsDate(this.setTimeToDayend(formatter.parse(form.getEffectiveEndDate())));
			}
			if( !StringUtils.isBlank(form.getRescentSubmittedDate()) ) {
				insApproval.setMostRecentlySubmittedForApprovalDate(formatter.parse(form.getRescentSubmittedDate()));
			}
			if( !StringUtils.isBlank(form.getOriginalApprovadDate()) ) {
				insApproval.setInspectorOriginallyApprovalDate(formatter.parse(form.getOriginalApprovadDate()));
			}
			if( !StringUtils.isBlank(form.getSubmissionReminderDate()) ) {
				insApproval.setInstructorSubmissionReminderDate(formatter.parse(form.getSubmissionReminderDate()));
			}
			// setting provider...
			if( !StringUtils.isBlank(form.getSelectedProviderId()) ) {
				Provider pro = accreditationService.getProviderById(Long.parseLong(form.getSelectedProviderId()));
				if( contentOwner != null ) {
					pro.setContentOwner(contentOwner);
				}
				insApproval.setProvider(pro);
			}
			// setting instructor...
			if( !StringUtils.isBlank(form.getSelectedInstructorId()) ) {
				Instructor ins = accreditationService.getInstructorByID(Long.parseLong(form.getSelectedInstructorId()));
				insApproval.setInstructor(ins);
			}
			// setting course...
			if( !StringUtils.isBlank(form.getSelectedCourseId()) ) {
				Course cou = courseCourseGroupService.getCourseById(Long.parseLong(form.getSelectedCourseId()));
				insApproval.setCourse(cou);
			}

			// setting regulator category
			for( ApprovalRegulatorCategory regCat : form.getRegulatorCategories()) {
				if( regCat.isSelected() ) {					
					insApproval.setRegulatorCategory(regCat.getCategory());
				}
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
							Object value = customFieldValue.getValue();
							customFieldValue.setCustomField(customField.getCustomFieldRef());
							customFieldValue.setValueItems(customFieldValueChoices);
							/*
							 *  for Encryption...
							 */
							if( customField.getCustomFieldRef().getFieldEncrypted() ) {
								customFieldValue.setValue(value);
							}
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
							Object value = customFieldValue.getValue();
							customFieldValue.setCustomField(customField.getCustomFieldRef());
							customFieldValue.setValueItems(customFieldValueChoices);
							/*
							 *  for Encryption...
							 */
							if( customField.getCustomFieldRef().getFieldEncrypted() ) {
								customFieldValue.setValue(value);
							}
							customFieldValues.add(customFieldValue);
						}

					} else {
						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
						Object value = customFieldValue.getValue();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						/*
						 *  for Encryption...
						 */
						if( customField.getCustomFieldRef().getFieldEncrypted() ) {
							customFieldValue.setValue(value);
						}
						customFieldValues.add(customFieldValue);
					}
				}
				if (customFieldValues.size()>0){
					insApproval.setCustomFieldValues(customFieldValues);
				}
			}

			// now saving...
			accreditationService.saveInstructorApproval(insApproval);
		}
		return new ModelAndView(finishTemplate);
	}
	private Date setTimeToDayend(Date date){		
		GregorianCalendar cal= new GregorianCalendar();
		cal.setTime(date);
		cal.set(GregorianCalendar.HOUR_OF_DAY, 23);
		cal.set(GregorianCalendar.MINUTE, 59);
		cal.set(GregorianCalendar.SECOND, 59);
		return cal.getTime();
	}
	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(cancelTemplate);
	}

	private void setCourseApprovalProperties(HttpServletRequest request,  Map<Object, Object> model, int page){
		model.put("courseApprovalSteps", courseApprovalSteps);
		for(int i=0; i<courseApprovalSteps.length; i++){
			if(page == courseApprovalSteps[i]){
				int prev = (i==0 ? 0 : i-1);
				int next = (i==(courseApprovalSteps.length - 1) ? i : i+1);
				model.put("currentStep", i);
				model.put("previousStep", prev);
				model.put("nextStep", next);
				model.put("previousStepValue", courseApprovalSteps[prev]);
				model.put("nextStepValue", courseApprovalSteps[next]);
				model.put("currentStepValue", courseApprovalSteps[i]);
				Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
				model.put("showing", brander.getBrandElement("lms.accraditation.addApproval.course.caption.wizardHeader.showing", brander.getBrandElement("lms.constant."+i), brander.getBrandElement("lms.constant."+courseApprovalSteps.length)));
				break;
			}
		}		
	}
	
	/*
	 *  GETTER & SETTERS...
	 */
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	public String getCancelTemplate() {
		return cancelTemplate;
	}
	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}
	public String getFinishTemplate() {
		return finishTemplate;
	}
	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public CourseAndCourseGroupService getCourseCourseGroupService() {
		return courseCourseGroupService;
	}

	public void setCourseCourseGroupService(
			CourseAndCourseGroupService courseCourseGroupService) {
		this.courseCourseGroupService = courseCourseGroupService;
	}
	
	private void validateCourseApprovalisAlreadyCreated(AddapprovalForm form, Errors errors, String entity){
		
		if(!form.getMethod().equals("courseApproval")){
			return;
		}
		long regulatorCategoryId = 0, courseId = 0;
		Date startDate = null, endDate = null;
		
		if(StringUtils.isNotBlank(form.getSelectedRegulatorCategoryId())){
			regulatorCategoryId = Long.parseLong(form.getSelectedRegulatorCategoryId());
		}
		
		if(StringUtils.isNotBlank(form.getSelectedCourseId())){
			courseId = Long.parseLong(form.getSelectedCourseId());
		}

		if(StringUtils.isNotBlank(form.getEffectiveStartDate())){
			startDate = DateUtil.getDateObject(form.getEffectiveStartDate());
		}

		if(StringUtils.isNotBlank(form.getEffectiveEndDate())){
			endDate = DateUtil.getDateObject(form.getEffectiveEndDate());
		}
		
		if(regulatorCategoryId > 0 && courseId > 0 && startDate != null && endDate != null){
			if(getAccreditationService().isCourseAlreadyAssociatedWithRegulatorAuthority(courseId, regulatorCategoryId, startDate, endDate)){
				if(entity.equals("regulator")){
					errors.rejectValue("regulatorCategories", "error.approval."+entity+".alreadyAssociated","");
				}else{
					errors.rejectValue("selectedCourseId", "error.approval."+entity+".alreadyAssociated","");
				}
			}
		}
		
	}
}