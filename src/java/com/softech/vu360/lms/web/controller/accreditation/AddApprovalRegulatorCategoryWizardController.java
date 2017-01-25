package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalForm;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRegulatorCategory;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRegulatorCategory;
import com.softech.vu360.lms.web.controller.validator.Accreditation.EditApprovalValidator;

/**
 * @author Saptarshi
 *
 */
public class AddApprovalRegulatorCategoryWizardController extends AbstractWizardFormController {

	public static final String COURSE_APPROVAL = "Course";
	public static final String PROVIDER_APPROVAL = "Provider";
	public static final String INSTRUCTOR_APPROVAL = "Instructor";
	public static final String SEARCH_REGULATOR = "search";

	private AccreditationService accreditationService = null;

	private String closeTemplate = null;

	public AddApprovalRegulatorCategoryWizardController() {
		super();
		setCommandName("approvalForm");
		setCommandClass(ApprovalForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
			"accreditation/approvals/editApproval/addRegulatorCategoryApproval/add_approval_regulatorCategory"
			, "accreditation/approvals/editApproval/addRegulatorCategoryApproval/add_approval_regulatorCategory_confirm"
		});
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		ApprovalForm form = (ApprovalForm)command;

		request.setAttribute("categoryTypes", RegulatorCategory.CATEGORY_TYPES);

		if(request.getParameter("entity") != null ) {
			if (request.getParameter("entity").equalsIgnoreCase(PROVIDER_APPROVAL)) {
				if (request.getParameter("approvalId") != null ) {
					ProviderApproval providerApproval = accreditationService.loadForUpdateProviderApproval(Long.parseLong(request.getParameter("approvalId")));
					form.setProviderApproval(providerApproval);
				}
				form.setEntity(PROVIDER_APPROVAL);
			} else if (request.getParameter("entity").equalsIgnoreCase(INSTRUCTOR_APPROVAL)) {
				if (request.getParameter("approvalId") != null ) {
					InstructorApproval instructorApproval = accreditationService.loadForUpdateInstructorApproval(Long.parseLong(request.getParameter("approvalId")));
					form.setInstructorApproval(instructorApproval);
				}
				form.setEntity(INSTRUCTOR_APPROVAL);
			} else if (request.getParameter("entity").equalsIgnoreCase(COURSE_APPROVAL)) {
				if (request.getParameter("approvalId") != null ) {
					CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(Long.parseLong(request.getParameter("approvalId")));
					form.setCourseApproval(courseApproval);
				}
				form.setEntity(COURSE_APPROVAL);
			} 
		}
		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		// Auto-generated method stub
		super.onBindAndValidate(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		// Auto-generated method stub
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		
		ApprovalForm form = (ApprovalForm)command;
		EditApprovalValidator validator = (EditApprovalValidator)this.getValidator();
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		HttpSession session = null;
		session = request.getSession();
		
		if ( page == 0 ) {

			request.setAttribute("categoryTypes", RegulatorCategory.CATEGORY_TYPES);

			if (request.getParameter("action") != null) {
				if (request.getParameter("action").equalsIgnoreCase(SEARCH_REGULATOR) 
						&& this.getTargetPage(request, page) != 1) {
//					VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					List<RegulatorCategory> categories = accreditationService.findRegulatorCategories(form.getRegulatorCategoryName(), form.getRegulatorCategoryType(),-1);
					
//					List<RegulatorCategory> duplicates= new ArrayList<RegulatorCategory>(); 

//					for(RegulatorCategory cat: categories){
//						for(ApprovalRegulatorCategory catForm: form.getRegulatorCategories())
//							if(cat.getId() == catForm.getCategory().getId())
//								duplicates.add(cat);						
//					}
					
//					categories.removeAll(duplicates);
//					categories.remove(formRegulatorCategory);
					
					String sortColumnIndex = form.getSortColumnIndex();
					if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
						sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
					String sortDirection = form.getSortDirection();
					if( sortDirection == null && session.getAttribute("sortDirection") != null )
						sortDirection = session.getAttribute("sortDirection").toString();
					String pageIndex = form.getPageCurrIndex();
					if( pageIndex == null ) pageIndex = form.getPageIndex();
					
					/*if( sortColumnIndex != null && sortDirection != null ) {
						if( sortColumnIndex.equalsIgnoreCase("0") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								RegulatorSort RegulatorSort = new RegulatorSort();
								RegulatorSort.setSortBy("name");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(categories,RegulatorSort);
								form.setSortDirection("0");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
								
							} else {
								RegulatorSort RegulatorSort = new RegulatorSort();
								RegulatorSort.setSortBy("name");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(categories,RegulatorSort);
								form.setSortDirection("1");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
								
							}
							// sorting against alias
						} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								RegulatorSort RegulatorSort = new RegulatorSort();
								RegulatorSort.setSortBy("alias");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(categories,RegulatorSort);
								form.setSortDirection("0");
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 1);
							} else {
								RegulatorSort RegulatorSort=new RegulatorSort();
								RegulatorSort.setSortBy("alias");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(categories,RegulatorSort);
								form.setSortDirection("1");
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 1);
							}
							// sorting against email - address
						} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								RegulatorSort RegulatorSort=new RegulatorSort();
								RegulatorSort.setSortBy("emailAddress");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(categories,RegulatorSort);
								form.setSortDirection("0");
								form.setSortColumnIndex("2");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 2);
							} else {
								RegulatorSort RegulatorSort=new RegulatorSort();
								RegulatorSort.setSortBy("emailAddress");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(categories,RegulatorSort);
								form.setSortDirection("1");
								form.setSortColumnIndex("2");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 2);
							}
							// sorting against jurisdiction...
						} else if( sortColumnIndex.equalsIgnoreCase("3") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								RegulatorSort RegulatorSort=new RegulatorSort();
								RegulatorSort.setSortBy("jurisdiction");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(categories,RegulatorSort);
								form.setSortDirection("0");
								form.setSortColumnIndex("3");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 3);
							} else {
								RegulatorSort RegulatorSort=new RegulatorSort();
								RegulatorSort.setSortBy("jurisdiction");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(categories,RegulatorSort);
								form.setSortDirection("1");
								form.setSortColumnIndex("3");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 3);
							}
						}
					}*/
					
					pagerAttributeMap.put("pageIndex", pageIndex);
					pagerAttributeMap.put("showAll", form.getShowAll());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
					List<ApprovalRegulatorCategory> regulatorCategoryList = new ArrayList<ApprovalRegulatorCategory>();
					
					for (RegulatorCategory regulatorCategory : categories) {
						ApprovalRegulatorCategory appRegulator = new ApprovalRegulatorCategory();
						appRegulator.setCategory(regulatorCategory);
						appRegulator.setRegulator(regulatorCategory.getRegulator());
						appRegulator.setSelected((false));
						regulatorCategoryList.add(appRegulator);
					}
					form.setRegulatorCategories(regulatorCategoryList);
				}
			}else{

				if(!StringUtils.isBlank(form.getSelectedRegulatorCategoryId())){
					RegulatorCategory regulatorCategory = getAccreditationService().getRegulatorCategory(Long.parseLong(form.getSelectedRegulatorCategoryId()));

					if (form.getEntity().equalsIgnoreCase(COURSE_APPROVAL)){ 
						form.setRegulatorCategory(regulatorCategory);
					}else if (form.getEntity().equalsIgnoreCase(PROVIDER_APPROVAL)){
						form.setRegulatorCategory(regulatorCategory);
					}else if (form.getEntity().equalsIgnoreCase(INSTRUCTOR_APPROVAL)){
						form.setRegulatorCategory(regulatorCategory);
					}
					
				}
				
				request.setAttribute("newPage","true");
			}
			if (this.getTargetPage(request, page) == 1) {
				validator.validateRegulatorCategory(form, errors);
			}
		}
		super.postProcessPage(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		if (form.getEntity().equalsIgnoreCase(PROVIDER_APPROVAL)) {
			context.put("target", "showProviderApprovalSummary");
		} else if (form.getEntity().equalsIgnoreCase(INSTRUCTOR_APPROVAL)) {
			context.put("target", "showInstructorApprovalSummary");
		} else if (form.getEntity().equalsIgnoreCase(COURSE_APPROVAL)) {
			context.put("target", "showCourseApprovalSummary");
		}
		return new ModelAndView(closeTemplate, "context", context);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3)
	throws Exception {
		
		ApprovalForm form = (ApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		
		if (form.getEntity().equalsIgnoreCase(PROVIDER_APPROVAL)) {
			ProviderApproval providerApproval = form.getProviderApproval();
			if (providerApproval != null) {
//				List<RegulatorCategory> regulatorCategories = providerApproval.getRegulatorCategories();
//				if (form.getRegulatorCategories() != null)
//					for (ApprovalRegulatorCategory approvalRegulatorCategory : form.getRegulatorCategories()) {
//						if (approvalRegulatorCategory.isSelected()){
//							regulatorCategories.add(approvalRegulatorCategory.getCategory());
//							providerApproval.setRegulatorCategory(approvalRegulatorCategory.getCategory());
//						}
//					}
//				providerApproval.setRegulatorCategories(regulatorCategories);
				providerApproval.setRegulatorCategory(form.getRegulatorCategory());
				accreditationService.saveProviderApproval(providerApproval);
			}
//			context.put("target", "showApprovalRegulatorCategories");
			context.put("target", "showProviderApprovalSummary");
		} else if (form.getEntity().equalsIgnoreCase(INSTRUCTOR_APPROVAL)) {
			InstructorApproval instructorApproval = form.getInstructorApproval();
			if (instructorApproval != null) {
//				List<RegulatorCategory> regulatorCategories = instructorApproval.getRegulatorCategories();
//				if (form.getRegulatorCategories() != null)
//					for (ApprovalRegulatorCategory approvalRegulatorCategory : form.getRegulatorCategories()) {
//						if (approvalRegulatorCategory.isSelected()){
//							regulatorCategories.add(approvalRegulatorCategory.getCategory());
//							instructorApproval.setRegulatorCategory(approvalRegulatorCategory.getCategory());
//						}
//					}
//				instructorApproval.setRegulatorCategories(regulatorCategories);
				instructorApproval.setRegulatorCategory(form.getRegulatorCategory());
				accreditationService.saveInstructorApproval(instructorApproval);
			}
//			context.put("target", "showApprovalRegulatorCategories");
			context.put("target", "showInstructorApprovalSummary");
		} else if (form.getEntity().equalsIgnoreCase(COURSE_APPROVAL)) {
			CourseApproval courseApproval = form.getCourseApproval();
			if (courseApproval != null) {
//				List<RegulatorCategory> regulatorsCategories = courseApproval.getRegulatorCategories();
// 				if (form.getRegulatorCategories() != null)
//					for (ApprovalRegulatorCategory approvalRegulatorCategory : form.getRegulatorCategories()) {
//						if (approvalRegulatorCategory.isSelected()) {
//							regulatorsCategories.add(approvalRegulatorCategory.getCategory());
//							courseApproval.setRegulatorCategory(approvalRegulatorCategory.getCategory());
//						}
//					}
// 				courseApproval.setRegulatorCategories(regulatorsCategories);
				courseApproval.setRegulatorCategory(form.getRegulatorCategory());
				accreditationService.saveCourseApproval(courseApproval);
			}
//			context.put("target", "showApprovalRegulatorCategories");
			context.put("target", "showCourseApprovalSummary");
		}
		return new ModelAndView(closeTemplate, "context", context);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		switch(page){
		case 0:
			break;
		case 1:
			break;
		}
		return super.referenceData(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		EditApprovalValidator validator = (EditApprovalValidator)this.getValidator();
		ApprovalForm form = (ApprovalForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			if(form.getEntity().equals(COURSE_APPROVAL)){
				validateCourseApprovalCreated(form, errors);
			}
			break;
		case 1:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	/**
	 * @return the accreditationService
	 */
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	/**
	 * @param accreditationService the accreditationService to set
	 */
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	/**
	 * @return the closeTemplate
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

	/**
	 * @param closeTemplate the closeTemplate to set
	 */
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}
	
	private void validateCourseApprovalCreated(ApprovalForm form, Errors errors){
		long regulatorCategoryId = 0;		
		long courseId = 0;
		form.setCourseApproval(accreditationService.loadForUpdateCourseApproval(form.getCourseApproval().getId()));
		if(form.getCourseApproval().getCourse() != null){
			courseId = form.getCourseApproval().getCourse().getId();
		}
		
		Date startDate = form.getCourseApproval().getCourseApprovalEffectivelyStartDate();
		Date endDate = form.getCourseApproval().getCourseApprovalEffectivelyEndsDate();

		if(StringUtils.isNotBlank(form.getSelectedRegulatorCategoryId())){
			regulatorCategoryId = Long.parseLong(form.getSelectedRegulatorCategoryId());
		}
		
		if(regulatorCategoryId > 0 && courseId > 0 && startDate != null && endDate != null){
			if(getAccreditationService().isCourseAlreadyAssociatedWithRegulatorAuthority(courseId, regulatorCategoryId, startDate, endDate, form.getCourseApproval().getId())){
				errors.rejectValue("selectedRegulatorCategoryId", "error.approval.regulator.alreadyAssociated","");
			}
		}
		
	}

}