package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialForm;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialTrainingCourses;
import com.softech.vu360.util.TrainingCourseByCredentialSort;



//AbstractWizardFormController
public class AddCoursesInCredentials extends AbstractWizardFormController{

	private static final Logger log = Logger.getLogger(AddCoursesInCredentials.class.getName());
	
	private String showTrainingCoursesForm;
	private String showTrainingCourses;
	private String showProctors;
	
	private String cancelTemplate = null;
	private String finishTemplate = null;
	
	private AccreditationService accreditationService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	
	public AddCoursesInCredentials() {
		super();
		setCommandName("credentialForm");
		setCommandClass(CredentialForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/Credential/trainingCourses/addCredentialTrainingCourses_step1" 
				, "accreditation/Credential/trainingCourses/addCredentialTrainingCourses_step2" 
		}); 
	}
	
	
	protected Object formBackingObject( HttpServletRequest request ) throws Exception {
		Object command = super.formBackingObject(request);
		CredentialForm form=(CredentialForm) command;

			long courseApprovalId = Long.parseLong(request.getParameter("credentialID"));
			Credential credential = accreditationService.loadForUpdateCredential(courseApprovalId);
			form.setCredential(credential);
		

		return command;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		super.onBindAndValidate(request, command, errors, page);
	}
	
	
	
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {
		CredentialForm form = (CredentialForm)command;
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		HttpSession session = null;
		session = request.getSession();
		
		String sortDirection="0",  pageIndex="0";
		
		List<Course> courseList = new ArrayList <Course>();
				
		String coursetitle = form.getTrainingCourseName();
		String businessKey = form.getTrainingCourseBusinessKey();
		
		if (page == 0) {
			if (!errors.hasErrors()){
				
				
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				 Long contentOwnerId = 0l;
				 if( loggedInUser.getLearner().getCustomer().getContentOwner().getId() != null ) 
					 contentOwnerId = loggedInUser.getLearner().getCustomer().getContentOwner().getId();
				 
				 // I add courses in Credential, training courses List wouldn't display which are already exists in Credential
				 // get all training courses Is from Credential and remove from List
				 int i = -1;
				 Long[] arrCoursesAlreadyExist = new Long[form.getCredential().getTrainingCourses().size()];
				 for (Course course : form.getCredential().getTrainingCourses())
					 arrCoursesAlreadyExist[++i]=course.getId();
				 // End --------------------------------------------------------------------------------
				 
				 courseList = courseAndCourseGroupService.getCourses(contentOwnerId, coursetitle, businessKey,arrCoursesAlreadyExist );
				 

						 
				 List<CredentialTrainingCourses> lstCredentialCourseMain = new ArrayList <CredentialTrainingCourses>();
				 List<CredentialTrainingCourses> appCredentialList =  form.getCredentialTrainingCourses();
					
					for (Course course : courseList) {
						boolean sel = false;
						for( CredentialTrainingCourses regCred : appCredentialList ) {
							if( course.getId().compareTo(regCred.getId()) == 0 ) {
								sel = regCred.isSelected();
								break;
							}
						}
						
						CredentialTrainingCourses credentialTrainingCourses = new CredentialTrainingCourses();
						credentialTrainingCourses.setId(course.getId());
						credentialTrainingCourses.setCourseTitle(course.getCourseTitle());
						credentialTrainingCourses.setBussinesskey(course.getBussinesskey());
						credentialTrainingCourses.setSelected(sel);
						lstCredentialCourseMain.add(credentialTrainingCourses);
				 }
							
							
							
							
					String sortColumnIndex = form.getSortColumnIndex();
					if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
						sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
					try{
						 sortDirection = String.valueOf(form.getSortDirection());
					}catch(Exception exp){
						 sortDirection= "0";
					}
					
					if( sortDirection == null && session.getAttribute("sortDirection") != null )
						sortDirection = session.getAttribute("sortDirection").toString();
					 pageIndex = form.getPageCurrIndex();
					if( pageIndex == null ) pageIndex = form.getPageIndex();
					
					if( sortColumnIndex != null && sortDirection != null ) {

						// sorting against regulator name
						if( sortColumnIndex.equalsIgnoreCase("0") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								TrainingCourseByCredentialSort trainingCourseSort = new TrainingCourseByCredentialSort();
								trainingCourseSort.setSortBy("courseTitle");
								trainingCourseSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredentialCourseMain,trainingCourseSort);
								form.setSortDirection(0);
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
							} else {
								TrainingCourseByCredentialSort trainingCourseSort = new TrainingCourseByCredentialSort();
								trainingCourseSort.setSortBy("courseTitle");
								trainingCourseSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredentialCourseMain,trainingCourseSort);
								form.setSortDirection(1);
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
							}
							// sorting against alias
						} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								TrainingCourseByCredentialSort trainingCourseSort = new TrainingCourseByCredentialSort();
								trainingCourseSort.setSortBy("bussinesskey");
								trainingCourseSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredentialCourseMain,trainingCourseSort);
								form.setSortDirection(0);
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 1);
							} else {
								
								TrainingCourseByCredentialSort trainingCourseSort = new TrainingCourseByCredentialSort();
								trainingCourseSort.setSortBy("bussinesskey");
								trainingCourseSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredentialCourseMain,trainingCourseSort);
								form.setSortDirection(1);
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 1);
							}
							// sorting against contact person
						} 
					}	
					
					//form.setTrainingCourses(courseList);
					pagerAttributeMap.put("pageIndex", pageIndex);
					//pagerAttributeMap.put("showAll", form.getShowAll());
					
					if(request.getParameter("showAll")!=null && (!request.getParameter("showAll").equals(""))){
						//context.put("showAll", request.getParameter("showAll"));
						pagerAttributeMap.put("showAll", request.getParameter("showAll"));
					}
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
					
					
						
					form.setCredentialTrainingCourses(lstCredentialCourseMain);
					
				 }
			    }
				return super.referenceData(request, page);
			}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		CredentialForm form = (CredentialForm)command;
		errors.setNestedPath("");
		switch(page) {
		
		case 0:
			if(!form.getGridAction().equalsIgnoreCase("search")){
				List<CredentialTrainingCourses> appCredentialList =  form.getCredentialTrainingCourses();
			
				boolean sel = true;
				for( CredentialTrainingCourses regCred : appCredentialList ) {
					if( regCred.isSelected()) {
						sel = false;
						break;
					}
				}
				
				if(sel)
					errors.rejectValue("trainingCourseName", "error.Credential.addTrainingCourse.notselected");
			}
			break;
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	
	protected void validatePage(Object command, Errors errors, int page) {

		//AddCredentialInRegulatorValidator validator = (AddCredentialInRegulatorValidator)this.getValidator();
		CredentialForm form = (CredentialForm)command;
		switch(page) {
		case 0:
			
				//validator.validateFirstPage(form, errors);
			
			break;
		case 1:
			break;
		default:
			break;
		}
		errors.setNestedPath("");
	}

	public String getShowTrainingCourses() {
		return showTrainingCourses;
	}

	public void setShowTrainingCourses(String showTrainingCourses) {
		this.showTrainingCourses = showTrainingCourses;
	}

	public String getShowProctors() {
		return showProctors;
	}

	public void setShowProctors(String showProctors) {
		this.showProctors = showProctors;
	}

	public String getShowTrainingCoursesForm() {
		return showTrainingCoursesForm;
	}

	public void setShowTrainingCoursesForm(String showTrainingCoursesForm) {
		this.showTrainingCoursesForm = showTrainingCoursesForm;
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
	
	@Override
	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(cancelTemplate);
	}
	
	@Override
	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
	
		CredentialForm form = (CredentialForm)command;
		Credential credential = form.getCredential();
		List<CredentialTrainingCourses> lstCourses = form.getCredentialTrainingCourses();
		
		for(CredentialTrainingCourses ctc : lstCourses){
			
			Course course = courseAndCourseGroupService.getCourseById(ctc.getId());
			
			
			if(ctc.isSelected()){
				credential.getTrainingCourses().add(course);
			}
		}
			
		
		accreditationService.saveCredential(credential);
		return new ModelAndView(finishTemplate);
	}
	
	@Override
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
	        return super.getTargetPage(request, command, errors, currentPage);
	}


	public AccreditationService getAccreditationService() {
		return accreditationService;
	}


	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}


	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}


	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	
    protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
	
		super.postProcessPage(request, command, errors, page);
	}

}